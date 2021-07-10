package com.fredtargaryen.enderarmor;

import com.fredtargaryen.enderarmor.config.Config;
import com.fredtargaryen.enderarmor.item.*;
import com.fredtargaryen.enderarmor.loot_tables.EnderArmourLootModifier;
import com.fredtargaryen.enderarmor.network.MessageHandler;
import com.fredtargaryen.enderarmor.network.message.MessageEnderArmourHitTeleportClient;
import com.fredtargaryen.enderarmor.proxy.ClientProxy;
import com.fredtargaryen.enderarmor.proxy.IProxy;
import com.fredtargaryen.enderarmor.proxy.ServerProxy;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;

@Mod(value = DataReference.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnderArmourBase {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    //Declare all items here
    @ObjectHolder("helmet")
    public static Item HELMET;
    @ObjectHolder("chestplate")
    public static Item CHESTPLATE;
    @ObjectHolder("leggings")
    public static Item LEGGINGS;
    @ObjectHolder("boots")
    public static Item BOOTS;

    @ObjectHolder("modifier")
    public static GlobalLootModifierSerializer<EnderArmourLootModifier> MODIFIER;

    private static final ArrayList<LivingEntity> knockbackEntities = new ArrayList<>();
	
    /**   
     * Says where the client and server 'proxy' code is loaded.
     */
    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    public EnderArmourBase() {
        //Register the config
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG_SPEC);

        //Event bus
        IEventBus loadingBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the setup method for modloading
        loadingBus.addListener(this::postRegistration);
        loadingBus.addListener(this::clientSetup);

        // Register ourselves for server, registry and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        //Register and load the config
        Config.loadConfig(FMLPaths.CONFIGDIR.get().resolve(DataReference.MODID + ".toml"));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new ItemHelmet()
                        .setRegistryName("helmet"),
                new ItemChestplate()
                        .setRegistryName("chestplate"),
                new ItemLeggings()
                        .setRegistryName("leggings"),
                new ItemBoots()
                        .setRegistryName("boots")
        );
    }

    @SubscribeEvent
    public static void registerGlobalLootModifierSerializers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().register(
            new EnderArmourLootModifier.Serializer()
                .setRegistryName("modifier")
        );
    }

    public void clientSetup(FMLClientSetupEvent event) {
        proxy.registerRenderers();
        proxy.registerRenderTypes();
    }

    /**
     * Called after all registry events. Runs in parallel with other SetupEvent handlers.
     * @param event
     */
    public void postRegistration(FMLCommonSetupEvent event) {
        MessageHandler.init();
    }

    @SubscribeEvent
    public void onLivingDamageTaken(LivingAttackEvent lae)
    {
        LivingEntity victim = lae.getEntityLiving();
        if(victim.level != null && !victim.level.isClientSide) {
            boolean foundEnderArmor = false;
            Iterator<ItemStack> armourIterator = victim.getArmorSlots().iterator();
            while (armourIterator.hasNext()) {
                if (armourIterator.next().getItem() instanceof EnderArmourItem) {
                    foundEnderArmor = true;
                    break;
                }
            }
            if (foundEnderArmor) {
                Entity attacker = lae.getSource().getEntity();
                if (attacker != null && attacker.isAlive()) {
                    // Teleport the entity behind the attacker
                    Vector3d attackerFeetPos = attacker.position();
                    float distanceAway = attacker.getBbWidth() / 2f;
                    double rotationRadians = Math.toRadians(attacker.yRot);
                    double x = attackerFeetPos.x();
                    double y = attackerFeetPos.y();
                    double z = attackerFeetPos.z();
                    double xOffset = distanceAway * Math.sin(rotationRadians);
                    double zOffset = distanceAway * Math.cos(rotationRadians);
                    //Aim for behind the attacker
                    boolean tpWorked = checkAndTeleportIfValid(victim, attackerFeetPos, x + xOffset, y, z - zOffset)
                            //Aim for to the attacker's left
                            || checkAndTeleportIfValid(victim, attackerFeetPos, x + zOffset, y, z + xOffset)
                            //Aim for to the attacker's right
                            || checkAndTeleportIfValid(victim, attackerFeetPos, x - zOffset, y, z - xOffset);
                    // If you're going to teleport, cancel any knockback or you'll just bounce in front of the enemy again
                    if(tpWorked) knockbackEntities.add(victim);
                }
            }
        }
    }

    private static boolean checkAndTeleportIfValid(LivingEntity victim, Vector3d attackerPos, double x, double y, double z) {
        BlockPos feetPos = new BlockPos(x, y, z);
        if(!victim.level.getBlockState(feetPos).getMaterial().blocksMotion()
                && !victim.level.getBlockState(feetPos.above()).getMaterial().blocksMotion()) {
            Vector3d victimPos = victim.position();
            double centreY = victim.getEyeHeight() / 2;
            victim.stopRiding();
            victim.moveTo(Math.floor(x) + 0.5, y, Math.floor(z) + 0.5);
            victim.lookAt(EntityAnchorArgument.Type.FEET, attackerPos); // EYES looks at the floor instead for some reason
            MessageEnderArmourHitTeleportClient meahtc = new MessageEnderArmourHitTeleportClient(
                    new Vector3d(victimPos.x, victimPos.y + centreY, victimPos.z),
                    new Vector3d(x, y + centreY, z)
            );
            MessageHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(x, y, z, 64.0, victim.level.dimension())), meahtc);
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public void onKnockback(LivingKnockBackEvent lkbe) {
        LivingEntity le = lkbe.getEntityLiving();
        if(knockbackEntities.contains(le)) {
            lkbe.setCanceled(true);
            knockbackEntities.remove(le);
        }
    }

    //////////////////
    //LOGGER METHODS//
    //////////////////
    public static void info(String message) { LOGGER.info(message); }
    public static void warn(String message) {
        LOGGER.warn(message);
    }
}