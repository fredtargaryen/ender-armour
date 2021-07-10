package com.fredtargaryen.enderarmor.proxy;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.math.vector.Vector3d;

public class ServerProxy implements IProxy {
    @Override
    public void registerRenderers(){}

    @Override
    public void registerRenderTypes() {

    }

    @Override
    public void doTeleportEffect(Vector3d startCentre, Vector3d endCentre) {

    }

    @Override
    public BipedModel getHelmetModel() {
        return null;
    }

    @Override
    public BipedModel getChestplateModel() {
        return null;
    }

    @Override
    public BipedModel getLeggingsModel() {
        return null;
    }

    @Override
    public BipedModel getBootsModel() {
        return null;
    }
}
