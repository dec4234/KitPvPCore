package net.dec4234.listeners;

import net.dec4234.guis.content.upgrades.Upgrades;
import net.dec4234.guis.content.upgrades.UpgradesRolling;
import net.dec4234.src.KitPvPCoreMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player p = event.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(KitPvPCoreMain.getInstance(), () -> {
			if (PlayerDeathListener.invContents.containsKey(p.getUniqueId())) {
				if (refundChance(p)) {
					p.getInventory().setContents(PlayerDeathListener.invContents.get(p.getUniqueId()));
					p.sendMessage("§aYour §6refund §aperk has spared your items§c!");
				}
				PlayerDeathListener.invContents.remove(p.getUniqueId());
			}
		}, 5);
	}

	public boolean refundChance(Player player) {
		int level = new UpgradesRolling(player).getLevel(Upgrades.REFUND);
		double chance = 0;
		if (level != 0) {
			switch (level) {
				case 1:
					chance = 0.01;
					break;
				case 2:
					chance = 0.02;
					break;
				case 3:
					chance = 0.03;
					break;
				case 4:
					chance = 0.04;
					break;
				case 5:
					chance = 0.05;
					break;
				case 6:
					chance = 0.075;
					break;
				case 7:
					chance = 0.1;
					break;
				case 8:
					chance = 0.15;
					break;
				case 9:
					chance = 0.175;
					break;
			}

			return Math.random() <= chance;
		}

		return false;
	}

}
