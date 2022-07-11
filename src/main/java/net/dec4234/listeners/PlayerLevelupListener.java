package net.dec4234.listeners;

import net.dec4234.events.PlayerLevelupEvent;
import net.dec4234.files.PlayerStats;
import net.dec4234.files.YmlConfig;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLevelupListener implements Listener {

	@EventHandler
	public void onLevelup(PlayerLevelupEvent event) {
		PlayerStats ps = new PlayerStats();
		Player p = event.getPlayer();

		// Update their displayname to match their new level
		ps.setDisplayname(p);

		// Notify the user that they have leveled up
		String fromConfig = YmlConfig.getFC().getString("level-up-announcement");
		fromConfig = fromConfig.replace("$player_name$", p.getName());
		fromConfig = fromConfig.replace("$level_number$", "" + event.getNewLevel());

		// Send player message in chat
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', fromConfig));

		// Play Level-Up sound
		p.playSound(p.getLocation(), Sound.LEVEL_UP, 2, 2);

		// Flash message on their screen
		p.sendTitle(" ", "§dLevel up§6!");
	}
}
