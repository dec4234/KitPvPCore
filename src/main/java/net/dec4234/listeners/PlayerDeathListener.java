package net.dec4234.listeners;

import net.dec4234.files.PlayerStats;
import net.dec4234.files.YmlConfig;
import net.dec4234.guis.content.upgrades.UpgradesRolling;
import net.dec4234.src.KitPvPCoreMain;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jefferies.core.economy.Economy;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDeathListener implements Listener {

	private PlayerStats ps = new PlayerStats();
	public static HashMap<UUID, ItemStack[]> invContents = new HashMap<>();

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();

		// Insta-respawn the player
		respawn(p, 5);

		if(p.getKiller() != null) {
			Player k = p.getKiller();
			UpgradesRolling ur = new UpgradesRolling(k);

			// Increase killer's kill count by 1
			ps.adjustStat(k.getUniqueId(), "kills", ps.getStatInt(k.getUniqueId(), "kills") + 1);
			// Increase current kill streak by 1
			ps.adjustStat(k.getUniqueId(), "killStreak", ps.getStatInt(k.getUniqueId(), "killStreak") + 1);
			ps.updateHighestKillstreak(k);
			// Grant a random amount of xp to killer
			int xp = ur.getXp();
			ps.adjustStat(k.getUniqueId(), "levelXp", ps.getStatInt(k.getUniqueId(), "levelXp") + xp);
			// Check if they need their level changed in the database
			ps.setLevel(k.getUniqueId());
			// If kill count is divisible by 25, award crate key
			if(ps.getStatInt(k.getUniqueId(), "kills") % 25 == 0) {
				// If the CrateReloaded plugin is enabled, continue
				if(Bukkit.getServer().getPluginManager().getPlugin("CrateReloaded") != null) {
					String crateID = YmlConfig.getFC().getString("kill-key-crate");

					// If the adminsitrator of the server has defined the id of the crate
					if(!crateID.equals("")) {
						com.hazebyte.crate.api.crate.Crate crate = com.hazebyte.crate.api.CrateAPI.getCrateRegistrar().getCrate(crateID);
						if(crate != null) {
							// Award the crate key to the user
							k.getInventory().addItem(crate.getItem());
						}
					}
				}
			}
			// Play a "ding" sound effect upon kill
			k.playSound(k.getLocation(), Sound.NOTE_PLING, 2, 2);


			// Announce to the server every time a user achieves a killstreak divisble by 10
			int killS = ps.getStatInt(k.getUniqueId(), "killStreak");
			if(killS % 10 == 0) {
				processBounty(k);
				ps.setDisplayname(k);

				String format = YmlConfig.getFC().getString("killstreak-announcement");
				format = format.replace("$player_name$", k.getDisplayName());
				format = format.replace("$killstreak$", killS + "");

				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', format));
			}

			// Give player their perks-on-kill
			ur.onKill();

			// Give killer $40
			Economy.addBalance(k.getUniqueId(), ur.getMoney());
			// If the killed user has a bounty, reward the killer
			int bounty = ps.getStatInt(p.getUniqueId(), "bounty");
			if(bounty != 0) {
				redeemBounty(p, k, bounty);
			}
		}

		// increase player's death count by 1
		ps.adjustStat(p.getUniqueId(), "deaths", ps.getStatInt(p.getUniqueId(), "deaths") + 1);
		// Reset current kill streak
		ps.adjustStat(p.getUniqueId(), "killStreak", 0);
		// Add user's current inventory to the list to be refunded if they get lucky
		invContents.put(p.getUniqueId(), p.getInventory().getContents());
	}

	private void processBounty(Player player) {
		int amount = 250;
		ps.setBounty(player.getUniqueId(), ps.getStatInt(player.getUniqueId(), "bounty") + amount);
	}

	private void redeemBounty(Player dead, Player killer, int bounty) {
		// Reward bounty amount to killer
		Economy.addBalance(killer.getUniqueId(), bounty);
		// Reset dead player's bounty
		ps.setBounty(dead.getUniqueId(), 0);
		// Announce to the server
		String format = YmlConfig.getFC().getString("bounty-claim-announcement");
		format = format.replace("$killer_name$", killer.getName());
		format = format.replace("$bounty$", bounty + "");
		format = format.replace("$player_name$", dead.getName());
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', format));
		killer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a+$" + bounty));
	}

	/**
	 * Insta-respawn the player (Skips the death screen)
	 */
	private void respawn(Player player, int time) {
		Bukkit.getScheduler().runTaskLater(KitPvPCoreMain.getInstance(), () -> {
			((CraftPlayer) player).getHandle().playerConnection.a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN));
		}, time);
	}
}
