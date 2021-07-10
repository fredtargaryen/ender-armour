package com.fredtargaryen.enderarmor.proxy;


import com.fredtargaryen.enderarmor.client.model.BootsModel;
import com.fredtargaryen.enderarmor.client.model.ChestplateModel;
import com.fredtargaryen.enderarmor.client.model.HelmetModel;
import com.fredtargaryen.enderarmor.client.model.LeggingsModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientProxy implements IProxy {
    @Override
    public void registerRenderers() {

    }

    @Override
    public void registerRenderTypes() {

    }

    @Override
    public void doTeleportEffect(Vector3d startCentre, Vector3d endCentre) {
        World world = Minecraft.getInstance().level;
        double randomX, randomY, randomZ;
        // Teleportation effect - going in
        for(int i = 0; i < 32; i++) {
            randomX = world.random.nextDouble() * 2 - 1;
            randomY = world.random.nextDouble() * 2 - 1;
            randomZ = world.random.nextDouble() * 2 - 1;
            world.addParticle(ParticleTypes.PORTAL,
                    startCentre.x + randomX,
                    startCentre.y + randomY,
                    startCentre.z + randomZ,
                    randomX * 0.75, randomY * 0.75, randomZ * 0.75);
        }
        // Teleportation effect - going out
        for(int i = 0; i < 32; i++) {
            randomX = world.random.nextDouble() * 2 - 1;
            randomY = world.random.nextDouble() * 2 - 1;
            randomZ = world.random.nextDouble() * 2 - 1;
            world.addParticle(ParticleTypes.REVERSE_PORTAL,
                    endCentre.x, endCentre.y, endCentre.z,
                    randomX * 0.04, randomY * 0.04, randomZ * 0.04);
        }
        world.playLocalSound(endCentre.x, endCentre.y, endCentre.z, SoundEvents.ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f, false);
    }

    @Override
    public BipedModel getHelmetModel() {
        return new HelmetModel(1f);
    }

    @Override
    public BipedModel getChestplateModel() {
        return new ChestplateModel(1f);
    }

    @Override
    public BipedModel getLeggingsModel() {
        return new LeggingsModel(1f);
    }

    @Override
    public BipedModel getBootsModel() {
        return new BootsModel(1f);
    }
}