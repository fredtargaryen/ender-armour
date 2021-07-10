package com.fredtargaryen.enderarmor.proxy;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.math.vector.Vector3d;

public interface IProxy
{
    void registerRenderers();

    void registerRenderTypes();

    void doTeleportEffect(Vector3d startCentre, Vector3d endCentre);

    BipedModel getHelmetModel();

    BipedModel getChestplateModel();

    BipedModel getLeggingsModel();

    BipedModel getBootsModel();
}
