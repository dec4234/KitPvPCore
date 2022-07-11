package net.dec4234.listeners;

import net.dec4234.files.PlayerStats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerJoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		PlayerStats ps = new PlayerStats();
		Player p = event.getPlayer();

		// If a player does not have default data, then add that data
		if(!ps.hasData(p.getUniqueId())) {
			ps.insertDefaultStats(p.getUniqueId());
		}

		ps.setDisplayname(p);
	}
}
