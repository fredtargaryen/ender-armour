package com.fredtargaryen.enderarmor.network.message;

import com.fredtargaryen.enderarmor.EnderArmourBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageEnderArmourHitTeleportClient {
    private Vector3d startCentre;
    private Vector3d endCentre;

    public MessageEnderArmourHitTeleportClient() {}

    public MessageEnderArmourHitTeleportClient(Vector3d startCentre, Vector3d endCentre) {
        this.startCentre = startCentre;
        this.endCentre = endCentre;
    }

    public void onMessage(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> EnderArmourBase.proxy.doTeleportEffect(this.startCentre, this.endCentre));
        ctx.get().setPacketHandled(true);
    }

    public MessageEnderArmourHitTeleportClient(ByteBuf buf) {
        this.startCentre = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.endCentre = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public void toBytes(ByteBuf buf) {
        buf.writeDouble(this.startCentre.x);
        buf.writeDouble(this.startCentre.y);
        buf.writeDouble(this.startCentre.z);
        buf.writeDouble(this.endCentre.x);
        buf.writeDouble(this.endCentre.y);
        buf.writeDouble(this.endCentre.z);
    }
}
