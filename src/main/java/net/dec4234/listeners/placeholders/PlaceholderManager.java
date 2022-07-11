package net.dec4234.listeners.placeholders;

import net.dec4234.files.PlayerStats;
import net.dec4234.listeners.placeholders.top10.Top10Framework;
import net.dec4234.src.KitPvPCoreMain;
import org.bukkit.Bukkit;

public class PlaceholderManager {

	public PlaceholderManager() {
		registerAll();
		startTimer();
	}

	/**
	 * Register all of the
	 */
	private void registerAll() {
		new BountyPlaceholder().register();
		new RemainingKillsPlaceholder().register();
		new RequiredEXPPlaceholder().register();

		// The top 10 users with the highest level XP
		new Top10Framework(1);
		new Top10Framework(2);
		new Top10Framework(3);
		new Top10Framework(4);
		new Top10Framework(5);
		new Top10Framework(6);
		new Top10Framework(7);
		new Top10Framework(8);
		new Top10Framework(9);
		new Top10Framework(10);
	}

	private void startTimer() {
		PlayerStats ps = new PlayerStats();
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(KitPvPCoreMain.getInstance(), ps::updateLeaderboard, 0, (5 * 60 * 20));
	}
}
