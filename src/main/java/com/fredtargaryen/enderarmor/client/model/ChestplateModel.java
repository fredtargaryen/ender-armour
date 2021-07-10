package com.fredtargaryen.enderarmor.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class ChestplateModel extends BipedModel<LivingEntity> {

    public ModelRenderer shape2;
    public ModelRenderer shape6;
    public ModelRenderer shape3;
    public ModelRenderer shape1;

    public ChestplateModel(float modelSize) {
        super(modelSize);
        this.texWidth = 16;
        this.texHeight = 16;
        this.head = new ModelRenderer(this, 0, 0);
        this.hat = new ModelRenderer(this, 32, 0);
        this.shape2 = new ModelRenderer(this, 0, 0);
        this.shape2.setPos(0.0F, 0.0F, 0.0F);
        this.shape2.addBox(-5.0F, -4.0F, 0.0F, 2, 1, 1, 0.0F);
        this.shape6 = new ModelRenderer(this, 5, 2);
        this.shape6.setPos(0.0F, 0.0F, 0.0F);
        this.shape6.addBox(-8.0F, -5.0F, 0.0F, 3, 3, 1, 0.0F);
        this.shape3 = new ModelRenderer(this, 12, 0);
        this.shape3.setPos(0.0F, 0.0F, 0.0F);
        this.shape3.addBox(-9.0F, -4.0F, 0.0F, 1, 1, 1, 0.0F);
        this.shape1 = new ModelRenderer(this, 6, 6);
        this.shape1.setPos(0.0F, 0.0F, 0.0F);
        this.shape1.addBox(-8.0F, -4.0F, 1.0F, 2, 2, 1, 0.0F);
        this.head.addChild(this.shape2);
        this.head.addChild(this.shape6);
        this.head.addChild(this.shape3);
        this.head.addChild(this.shape1);
    }
}
