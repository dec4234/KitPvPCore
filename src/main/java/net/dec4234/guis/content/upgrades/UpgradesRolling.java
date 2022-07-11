package net.dec4234.guis.content.upgrades;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.dec4234.files.PlayerStats;
import net.dec4234.guis.content.boosters.BoosterGUI;
import net.dec4234.guis.content.boosters.BoosterHandling;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class UpgradesRolling {

	private Player player;

	public UpgradesRolling(Player player) {
		this.player = player;
	}

	public void onKill() {
		effect(Upgrades.SPEED, PotionEffectType.SPEED, 0, 4, 5, 6, 7, 8, 9, 10, 12, 15);
		effect(Upgrades.REGEN, PotionEffectType.REGENERATION, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9);
		effect(Upgrades.STRENGTH, PotionEffectType.INCREASE_DAMAGE, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
		absorption();
	}

	public int getXp() {
		int multiplier = 1;
		// Return an amount of xp based upon their active xp boosters
		if (BoosterHandling.hasActiveBooster(player)) {
			BoosterGUI.BoosterType type = BoosterHandling.getActiveBooster(player);
			if(type != null) {
				multiplier = type.getMultiplier();
			}
		}

		return (10 + (int) (Math.random() * ((25 - 10) + 1))) * multiplier;
	}

	/**
	 * Return the amount of money this user should receive when they kill someone
	 */
	public int getMoney() {
		if (chance() && hasUnlocked(Upgrades.MONOPOLY)) {
			player.sendMessage("double");
			return 80;
		}

		return 40;
	}

	public boolean chance() {
		int level = getLevel(Upgrades.MONOPOLY);
		double chance = 0;
		if (level != 0) {
			switch (level) {
				case 1:
					chance = 0.05;
					break;
				case 2:
					chance = 0.1;
					break;
				case 3:
					chance = 0.15;
					break;
				case 4:
					chance = 0.25;
					break;
				case 5:
					chance = 0.4;
					break;
				case 6:
					chance = 0.5;
					break;
				case 7:
					chance = 0.65;
					break;
				case 8:
					chance = 0.8;
					break;
				case 9:
					chance = 1.0;
					break;
			}

			return Math.random() <= chance;
		}

		return false;
	}

	public void effect(Upgrades u, PotionEffectType pet, int amplifier, int first, int second, int third, int fourth, int fifth, int sixth, int seventh, int eighth, int ninth) {
		// Check player's speed level in the database
		int level = getLevel(u);
		int time = 0;
		if (level != 0) {
			switch (level) {
				case 1:
					time = first;
					break;
				case 2:
					time = second;
					break;
				case 3:
					time = third;
					break;
				case 4:
					time = fourth;
					break;
				case 5:
					time = fifth;
					break;
				case 6:
					time = sixth;
					break;
				case 7:
					time = seventh;
					break;
				case 8:
					time = eighth;
					break;
				case 9:
					time = ninth;
					break;
			}
			if (time != 0) {
				if (player.hasPotionEffect(pet)) {
					player.removePotionEffect(pet);
				}
				player.addPotionEffect(new PotionEffect(pet, time * 20, amplifier));
			}
		}
	}

	public void absorption() {
		// Check player's speed level in the database
		int level = getLevel(Upgrades.ABSORPTION);
		int time = 0;
		if (level != 0) {
			switch (level) {
				case 1:
					time = 10;
					break;
				case 2:
					time = 15;
					break;
				case 3:
					time = 20;
					break;
				case 4:
				case 5:
					time = 30;
					break;
			}
			if (time != 0) {
				int amplifier = 1;
				if (level == 5) {
					amplifier = 3;
				}
				player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, time * 20, amplifier));
			}
		}
	}

	/**
	 * Return the level of that upgrade for the user
	 * Returns 0 if they haven't leveled that perk up
	 */
	public int getLevel(Upgrades u) {
		return new PlayerStats().getStatInt(player.getUniqueId(), u.getIdentifier());
	}

	/**
	 * Sets the level in the database for this perk
	 */
	public void setLevel(Upgrades u, int newValue) {
		new PlayerStats().adjustStat(player.getUniqueId(), u.getIdentifier(), newValue);
	}

	/**
	 * Returns if the user's xp level is high enough for them to have unlocked this perk
	 */
	public boolean hasUnlocked(Upgrades u) {
		PlayerStats ps = new PlayerStats();
		return ps.getStatInt(player.getUniqueId(), "level") >= u.getMinLevel();
	}

	/**
	 * Provide the number of the slot in an inventory where a user clicks
	 * You will then receive the Upgrade corresponding to that inventory slot
	 */
	public Upgrades parseFromSlot(int slot) {
		for (Upgrades u : Upgrades.values()) {
			if (u.getDefaultSlot() == slot) {
				return u;
			}
		}

		return null;
	}

	public int getCost(Upgrades u, int level) {
		switch (u) {
			case SPEED:
				switch (level) {
					case 2:
						return 300;
					case 3:
						return 350;
					case 4:
						return 400;
					case 5:
						return 500;
					case 6:
						return 600;
					case 7:
						return 700;
					case 8:
						return 800;
					case 9:
						return 1000;
					default:
						return 250;
				}
			case REGEN:
				switch (level) {
					case 2:
						return 350;
					case 3:
						return 450;
					case 4:
						return 600;
					case 5:
						return 700;
					case 6:
						return 850;
					case 7:
						return 1000;
					case 8:
						return 1250;
					case 9:
						return 1500;
					default:
						return 200;
				}
			case STRENGTH:
				switch (level) {
					case 2:
						return 500;
					case 3:
						return 600;
					case 4:
						return 750;
					case 5:
						return 1000;
					case 6:
						return 1250;
					case 7:
						return 1500;
					case 8:
						return 2000;
					case 9:
						return 2500;
					default:
						return 400;
				}
			case DAMAGE_REDUCTION:
				switch (level) {
					case 2:
						return 250;
					case 3:
						return 400;
					case 4:
						return 650;
					case 5:
						return 700;
					case 6:
						return 750;
					case 7:
						return 800;
					case 8:
						return 850;
					case 9:
						return 900;
					default:
						return 150;
				}
			case MONOPOLY:
				switch (level) {
					case 2:
						return 300;
					case 3:
						return 400;
					case 4:
						return 600;
					case 5:
						return 750;
					case 6:
						return 800;
					case 7:
						return 900;
					case 8:
						return 1000;
					case 9:
						return 1500;
					default:
						return 200;
				}
			case FROSTBITE:
				switch (level) {
					case 2:
						return 400;
					case 3:
						return 500;
					case 4:
						return 750;
					case 5:
						return 1000;
					default:
						return 300;
				}
			case ABSORPTION:
				switch (level) {
					case 2:
						return 500;
					case 3:
						return 600;
					case 4:
						return 750;
					case 5:
						return 1500;
					default:
						return 400;
				}
			case FLAMING:
				switch (level) {
					case 2:
						return 200;
					case 3:
						return 250;
					case 4:
						return 300;
					case 5:
						return 350;
					case 6:
						return 400;
					case 7:
						return 500;
					case 8:
						return 650;
					case 9:
						return 800;
					default:
						return 150;
				}
			case REFUND:
				switch (level) {
					case 2:
						return 200;
					case 3:
						return 300;
					case 4:
						return 400;
					case 5:
						return 500;
					case 6:
						return 600;
					case 7:
						return 700;
					case 8:
						return 800;
					case 9:
						return 900;
					default:
						return 100;

				}
		}

		return -1;
	}
}
