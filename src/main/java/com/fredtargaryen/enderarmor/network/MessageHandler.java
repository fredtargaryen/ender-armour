package com.fredtargaryen.enderarmor.network;

import com.fredtargaryen.enderarmor.DataReference;
import com.fredtargaryen.enderarmor.network.message.MessageEnderArmourHitTeleportClient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MessageHandler {
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(DataReference.MODID, "channel"),
            () -> "1.0", //version that will be offered to the server
            (String s) -> s.equals("1.0"), //client accepted versions
            (String s) -> s.equals("1.0"));//server accepted versions

    public static void init() {
        INSTANCE.registerMessage(0, MessageEnderArmourHitTeleportClient.class, MessageEnderArmourHitTeleportClient::toBytes, MessageEnderArmourHitTeleportClient::new, MessageEnderArmourHitTeleportClient::onMessage);
    }
}
