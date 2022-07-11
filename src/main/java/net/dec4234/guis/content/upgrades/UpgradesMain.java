package net.dec4234.guis.content.upgrades;

import net.dec4234.files.PlayerStats;
import net.dec4234.guis.framework.GUI;
import net.dec4234.utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class UpgradesMain extends GUI {

	public UpgradesMain(Player player) {
		super("§d§lUpgrades", 9);
		setClass(this);
		setContents(player);
	}

	@Override
	public void call(Player player, int slot) {
		UpgradesRolling ur = new UpgradesRolling(player);
		for(Upgrades u : Upgrades.values()) {
			if(u.getDefaultSlot() == slot) {
				if(ur.hasUnlocked(u)) {
					// Open the sub-GUI for that upgrade
					new SubGUI(player, "§d§lUpgrades §b➔ " + u.getName(), u).open(player);
				}
				break;
			}
		}
	}

	/**
	 * Set the contents of this GUI
	 */
	@Override
	public void setContents(Player player) {
		UpgradesRolling ur = new UpgradesRolling(player);

		for (Upgrades u : Upgrades.values()) {
			ItemBuilder ib;
			if(u == Upgrades.SPEED) {
				ib = new ItemBuilder(u.getMaterial(), u.getName(), PotionEffectType.SPEED);
			} else {
				ib = new ItemBuilder(u.getMaterial(), u.getName());
			}
			ib.setLore(u.getDesc());
			ib.addToLore("");
			ib.hideDetails();
			if(ur.hasUnlocked(u)) {
				ib.addToLore("§aClick to view levels");
			} else {
				ib.addToLore("§cUnlocked at level §6" + u.getMinLevel());
			}

			getInventory().setItem(u.getDefaultSlot(), ib.build());
		}
	}
}
