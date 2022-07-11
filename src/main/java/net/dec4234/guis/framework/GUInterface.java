package net.dec4234.guis.framework;

import org.bukkit.entity.Player;

public interface GUInterface {

	public void call(Player player, int slot);

	public void setContents(Player player);

}
