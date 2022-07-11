package net.dec4234.listeners;

import net.dec4234.guis.content.upgrades.Upgrades;
import net.dec4234.guis.content.upgrades.UpgradesRolling;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ArrowHitListener implements Listener {

	/**
	 * When a player shoots another player with a bow and arrow, give the user that was hit Slowness
	 * based on the level of the shooter's perk.
	 * @param event
	 */
	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player) {
			if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
				if (event.getDamager() instanceof Arrow) {
					Arrow a = (Arrow) event.getDamager();
					Player hitPlayer = (Player) event.getEntity();
					if (a.getShooter() instanceof Player) {
						Player shooter = (Player) a.getShooter();
						if (slownessChance(shooter)) {
							hitPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 0));
						}
					}
				}
			} else if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
				event.setDamage((1 - environmentalChance((Player) event.getEntity())) * event.getDamage());
			}
		}
	}

	@EventHandler
	public void onShoot(ProjectileLaunchEvent ple) {
		if(ple.getEntity() instanceof Arrow) {
			Arrow a = (Arrow) ple.getEntity();
			if(a.getShooter() instanceof Player && fireArrowChance((Player) a.getShooter())) {
				a.setFireTicks(Integer.MAX_VALUE);
			}
		}
	}

	private boolean slownessChance(Player player) {
		int level = new UpgradesRolling(player).getLevel(Upgrades.FROSTBITE);
		double chance = 0;
		if (level != 0) {
			switch (level) {
				case 1:
					chance = 0.025;
					break;
				case 2:
					chance = 0.05;
					break;
				case 3:
					chance = 0.1;
					break;
				case 4:
					chance = 0.15;
					break;
				case 5:
					chance = 0.25;
					break;
			}

			return Math.random() <= chance;
		}

		return false;
	}

	private boolean fireArrowChance(Player player) {
		int level = new UpgradesRolling(player).getLevel(Upgrades.FLAMING);
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

	private double environmentalChance(Player player) {
		int level = new UpgradesRolling(player).getLevel(Upgrades.DAMAGE_REDUCTION);
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

		}

		return chance;
	}
}
