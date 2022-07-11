package net.dec4234.files;

import net.dec4234.events.PlayerBountyChangeEvent;
import net.dec4234.events.PlayerLevelupEvent;
import net.dec4234.src.KitPvPCoreMain;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class PlayerStats extends MongoFramework {

	public PlayerStats() {
		super(KitPvPCoreMain.getMongoClient(),
			  KitPvPCoreMain.getMongoDatabase("sCore"),
			  KitPvPCoreMain.getMongoDatabase("sCore").getCollection("stats"));
	}

	public int getStatInt(UUID uuid, String statName) {
		insertDefaultStats(uuid);
		Document doc = getDoc(uuid.toString());
		if(!doc.containsKey(statName)) {
			return 0;
		}
		return doc.getInteger(statName);
	}

	public String getStatString(UUID uuid, String statName) {
		insertDefaultStats(uuid);
		Document doc = getDoc(uuid.toString());
		if(!doc.containsKey(statName)) {
			return "";
		}
		return doc.getString(statName);
	}

	/**
	 * Change a stat with the following name to this value
	 */
	public void adjustStat(UUID uuid, String statName, Object newValue) {
		insertDefaultStats(uuid);
		Document doc = getDoc(uuid.toString());

		if(doc.containsKey(statName)) {
			doc.replace(statName, newValue);
		} else {
			doc.put(statName, newValue);
		}

		replaceData(uuid, doc);
	}

	/**
	 * Update the user's highest killStreak to match the current killStreak if it is higher then their previous
	 */
	public void updateHighestKillstreak(Player p) {
		int killStreak = getStatInt(p.getUniqueId(), "killStreak");
		if(killStreak > getStatInt(p.getUniqueId(), "highestKillStreak")) {
			adjustStat(p.getUniqueId(), "highestKillStreak", killStreak);
		}
	}

	public void updateLeaderboard() {
		HashMap<String, Integer> leaderboard = new HashMap<>();

		// Load data into the hashmap
		for(Document doc : collection.find()) {
			int levelXp = doc.getInteger("levelXp") == null ? 0 :  doc.getInteger("levelXp");
			if(levelXp > 0) {
				leaderboard.put(doc.getString("UUID"), levelXp);
			}
		}

		if(!leaderboard.isEmpty()) {
			ArrayList<String> sorted = new ArrayList<>(sortByValue(leaderboard).keySet());

			for(int i = 1; i < 10; i++) {
				if(sorted.size() >= i) {
					YmlConfig.set("number" + i, sorted.get(i - 1));
				} else {
					YmlConfig.set("number" + i, "");
				}
			}
		}
	}

	private HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<String, Integer>> list =
				new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
							   Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

		/** Add an amount to an exisiting stat */
	public void addStat(UUID uuid, String statName, int toAdd) {
		adjustStat(uuid, statName, getStatInt(uuid, statName) + toAdd);
	}

	public void setDisplayname(Player player) {
		String bountyString = "";
		int bounty = getStatInt(player.getUniqueId(), "bounty");
		if(bounty != 0) {
			bountyString = " &6&l$" + bounty + "&f";
		}

		String last = ChatColor.translateAlternateColorCodes('&',
															 "&8[" + getLevelColor(player.getUniqueId()) + getStatInt(player.getUniqueId(), "level")
																	 + "&8] &f" + player.getName() + bountyString);

		player.setDisplayName(last); // Change chat name
		player.setPlayerListName(last); // Change tab list name

		Scoreboard scoreboard = player.getScoreboard();

		if(scoreboard.getTeam(player.getName()) != null){
			scoreboard.getTeam(player.getName()).unregister();
		}

		// Change name above head
		Team team = scoreboard.registerNewTeam(player.getName());
		team.setPrefix(ChatColor.translateAlternateColorCodes('&', "&8[" + getLevelColor(player.getUniqueId()) + getStatInt(player.getUniqueId(), "level") + "&8] &f"));
		team.setSuffix(ChatColor.translateAlternateColorCodes('&', bountyString));
		team.addEntry(player.getName());
	}

	/**
	 * Set a player's bounty value in the databse
	 */
	public void setBounty(UUID uuid, Number amount) {
		int oldAmount = getStatInt(uuid, "bounty");

		// Change bounty value in database
		adjustStat(uuid, "bounty", amount);
		// Call Bounty Change Event
		int newAmount = getStatInt(uuid, "bounty");
		Bukkit.getPluginManager().callEvent(new PlayerBountyChangeEvent(Bukkit.getPlayer(uuid), oldAmount, newAmount));
		// Change displayname
		setDisplayname(Bukkit.getPlayer(uuid));
	}

	public void setLevel(UUID uuid) {
		int oldLevel = getStatInt(uuid, "level");
		int newLevel = calculateLevel(uuid);
		if(oldLevel != newLevel && newLevel != 0) {
			// Change their level in the database
			adjustStat(uuid, "level", newLevel);

			// Call Level Up Event
			Bukkit.getPluginManager().callEvent(new PlayerLevelupEvent(Bukkit.getPlayer(uuid), oldLevel, newLevel));
		}
	}

	/**
	 * Used to calculate the user's level
	 * Probably not very efficient
	 */
	private int calculateLevel(UUID uuid) {
		int xp = getStatInt(uuid, "levelXp");
		int level = 0;

		// Need to optimize in the future
		if(xp > 0) {
			while(xp >= 150 && level < 5) {
				xp -= 150;
				level++;
			}
			while(xp >= 300 && level < 9) {
				xp -= 300;
				level++;
			}
			while(xp >= 350 && level < 19) {
				xp -= 350;
				level++;
			}
			while(xp >= 450 && level < 29) {
				xp -= 450;
				level++;
			}
			while(xp >= 600 && level < 39) {
				xp -= 600;
				level++;
			}
			while(xp >= 650 && level < 49) {
				xp -= 650;
				level++;
			}
			while(xp >= 750 && level < 59) {
				xp -= 750;
				level++;
			}
			while(xp >= 800 && level < 69) {
				xp -= 800;
				level++;
			}
			while(xp >= 850 && level < 79) {
				xp -= 850;
				level++;
			}
			while(xp >= 900 && level < 89) {
				xp -= 900;
				level++;
			}
			while(xp >= 950 && level < 99) {
				xp -= 950;
				level++;
			}
			while(xp >= 1000 && level >= 99) { // Cycle through all of their levels after 100
				xp -= 1000;
				level++;
			}
		}

		return level;
	}

	/**
	 * Calculate how much more xp this user needs in order to level up
	 */
	public int xpForNextLevel(UUID uuid) {
		int level = calculateLevel(uuid);
		int xp = getStatInt(uuid, "levelXp");

		for(int i = 0; i < level; i++) {
			xp -= getXpForLevel(i);
		}

		return getXpForLevel(level) - xp;
	}

	/**
	 * Calculate the required amount of xp per level
	 */
	public int getXpForLevel(int level) {
		int xp = 150;

		if(level > 0) {
			if (level < 5) {
			} else if (level < 9) {
				xp = 300;
			} else if (level < 19) {
				xp = 350;
			} else if (level < 29) {
				xp = 450;
			} else if (level < 39) {
				xp = 600;
			} else if (level < 49) {
				xp = 650;
			} else if (level < 59) {
				xp = 750;
			} else if (level < 69) {
				xp = 800;
			} else if (level < 79) {
				xp = 850;
			} else if (level < 89) {
				xp = 900;
			} else if (level < 99) {
				xp = 950;
			} else {
				xp = 1000;
			}
		}

		return xp;
	}

	/**
	 * Returns a color based on the provided user's level
	 */
	public String getLevelColor(UUID uuid) {
		int level = getStatInt(uuid, "level");

		if(level >= 0 && level <= 9) {
			return "&7";
		} else if (level >= 10 && level <= 29) {
			return "&f";
		} else if (level >= 30 && level <= 49) {
			return "&e";
		} else if (level >= 50 && level <= 59) {
			return "&6";
		} else if (level >= 60 && level <= 69) {
			return "&5";
		} else if (level >= 70 && level <= 79) {
			return "&9";
		} else if (level >= 80 && level <= 89) {
			return "&3";
		} else if (level >= 90 && level <= 99) {
			return "&2";
		} else if (level >= 100) {
			return "&c";
		}

		return "&7";
	}
}
