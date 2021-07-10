package com.fredtargaryen.enderarmor.item;

import com.fredtargaryen.enderarmor.DataReference;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public abstract class EnderArmourItem extends ArmorItem {
    private String layer;
    private static String LAYER1 = DataReference.MODID + ":textures/models/armor/ender_layer_1.png";
    private static String LAYER2 = DataReference.MODID + ":textures/models/armor/ender_layer_2.png";

    public EnderArmourItem(EquipmentSlotType slotType, boolean layer1) {
        super(ArmorMaterial.LEATHER, slotType, new Item.Properties().tab(ItemGroup.TAB_COMBAT).stacksTo(1));
        this.layer = layer1 ? LAYER1 : LAYER2;
    }

    /**
     * Called by RenderBiped and RenderPlayer to determine the armor texture that
     * should be use for the currently equipped item. This will only be called on
     * instances of ItemArmor.
     *
     * Returning null from this function will use the default value.
     *
     * @param stack  ItemStack for the equipped armor
     * @param entity The entity wearing the armor
     * @param slot   The slot the armor is in
     * @param type   The subtype, can be null or "overlay"
     * @return Path of texture to bind, or null to use default
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return this.layer;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.enderarmorft.effect"));
    }
}
