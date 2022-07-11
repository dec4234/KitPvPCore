package net.dec4234.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBar {

	private PacketPlayOutChat packet;

	public ActionBar(String text) {
		PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}"), (byte) 2);

		this.packet = packet;
	}

	public void send(Player player) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
}
