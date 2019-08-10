package com.tfar.randomenchants.network;


import com.tfar.randomenchants.RandomEnchants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;


public class Message {

  public static SimpleChannel INSTANCE;

  public static void registerMessages(String channelName) {
    INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(RandomEnchants.MODID, channelName), () -> "1.0", s -> true, s -> true);
    INSTANCE.registerMessage(0, PacketComboReset.class,
            PacketComboReset::encode,
            PacketComboReset::new,
            PacketComboReset::handle);
  }
}
