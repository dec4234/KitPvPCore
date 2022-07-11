package net.dec4234.guis.content.boosters;

import net.dec4234.files.PlayerStats;
import net.dec4234.files.YmlConfig;
import net.dec4234.src.KitPvPCoreMain;
import net.dec4234.utils.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.jefferies.core.utils.chat.Format.translate;

public class BoosterHandling {

	private static HashMap<UUID, Long> activeBoosters = new HashMap<>();
	private static HashMap<UUID, BoosterGUI.BoosterType> boosterInfo = new HashMap<>();
	private static int i = 0;
	private static boolean isRunning = false;

	private static String boosterTimer = translate(YmlConfig.getFC().getString("booster-timer"));
	private static String hoursPrefix = translate(YmlConfig.getFC().getString("booster-hours-prefix"));
	private static String hoursSuffix = translate(YmlConfig.getFC().getString("booster-hours-suffix"));
	private static String minutesSuffix = translate(YmlConfig.getFC().getString("booster-minutes-suffix"));
	private static String secondsSuffix = translate(YmlConfig.getFC().getString("booster-seconds-suffix"));

	/**
	 * Activate a specific type of booster for this player
	 * It will prevent this from occuring if a user already has an active booster
	 */
	public static void activate(Player player, BoosterGUI.BoosterType type) {
		if(!hasActiveBooster(player)) {
			activeBoosters.put(player.getUniqueId(), (long) (60 * 60 * 1000 * type.getHours()) + System.currentTimeMillis());
			boosterInfo.put(player.getUniqueId(), type);

			// If the timer was not already running, then initialize it
			if(!isRunning) {
				init();
			}
		}
	}

	/**
	 * Deactivate the current booster for this user
	 */
	public static void deActivate(UUID id) {
		if(activeBoosters.containsKey(id)) {
			activeBoosters.remove(id);
			boosterInfo.remove(id);
		}
		if(activeBoosters.isEmpty()) {
			cancel();
		}
	}

	/**
	 * Deactivate all currently running boosters
	 */
	public static void deactivateAll() {
		for(UUID id : activeBoosters.keySet()) {
			deActivate(id);
		}
	}

	public static boolean hasActiveBooster(Player player) {
		return activeBoosters.containsKey(player.getUniqueId());
	}

	public static BoosterGUI.BoosterType getActiveBooster(Player player) {
		if(hasActiveBooster(player)) {
			return boosterInfo.get(player.getUniqueId());
		}

		return null;
	}

	private static void init() {
		isRunning = true;
		i = Bukkit.getScheduler().scheduleAsyncRepeatingTask(KitPvPCoreMain.getInstance(), BoosterHandling::cycle, 0, 10);
	}

	public static void cancel() {
		isRunning = false;
		Bukkit.getScheduler().cancelTask(i);
		activeBoosters.clear();
		boosterInfo.clear();
	}

	private static void cycle() {
		if(activeBoosters.isEmpty()) {
			cancel();
			return;
		}

		for(UUID id : activeBoosters.keySet()) {
			long time = activeBoosters.get(id);
			if(time > System.currentTimeMillis()) {
				long diff = time - System.currentTimeMillis();
				if(diff > 0) {
					// String info = "§bx" + boosterInfo.get(id).getMultiplier() + " §cXP Booster";
					String info = boosterTimer.replace("$booster_multiplier$", boosterInfo.get(id).getMultiplier() + "");
					info += neatDate(diff);

					// Send an actionbar message to the user alerting them of the remaining time of the booster
					Player player = Bukkit.getPlayer(id);
					if(player != null) {
						new ActionBar(info).send(player);
					}
				} else {
					deActivate(id);
				}
			} else {
				deActivate(id);
			}
		}
	}

	/**
	 * Used to update a user's database document to include a certain number of boosters
	 */
	public static void addBoosterToInventory(Player player, int amount, BoosterGUI.BoosterType type) {
		PlayerStats ps = new PlayerStats();
		ps.adjustStat(player.getUniqueId(), type.getIdentifier(), ps.getStatInt(player.getUniqueId(), type.getIdentifier()) + amount);
	}

	public static void decrementFromInventory(Player player, BoosterGUI.BoosterType type) {
		PlayerStats ps = new PlayerStats();
		int amount = ps.getStatInt(player.getUniqueId(), type.getIdentifier());
		if(amount != 0) {
			amount--;
			ps.adjustStat(player.getUniqueId(), type.getIdentifier(), amount);
		}
	}

	public static boolean hasAnyBoosters(Player player) {
		for(BoosterGUI.BoosterType type : BoosterGUI.BoosterType.values()) {
			if(hasBoosterInInventory(player, type)) {
				return true;
			}
		}

		return false;
	}

	public static boolean hasBoosterInInventory(Player player, BoosterGUI.BoosterType type) {
		PlayerStats ps = new PlayerStats();
		return ps.getStatInt(player.getUniqueId(), type.getIdentifier()) != 0;
	}

	private static String neatDate(long millis) {
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		StringBuilder sb = new StringBuilder(64);
		if(hours > 0) {
			sb.append(hoursPrefix);
			sb.append(hours);
			sb.append(hoursSuffix);
		}
		if(minutes > 0) {
			sb.append(minutes);
			sb.append(minutesSuffix);
		}
		sb.append(seconds);
		sb.append(secondsSuffix);

		return(sb.toString());
	}
}
