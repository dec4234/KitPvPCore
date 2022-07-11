package net.dec4234.guis.content.upgrades;

import net.dec4234.guis.framework.GUI;
import net.dec4234.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jefferies.core.economy.Economy;

public class SubGUI extends GUI {

	private Upgrades u;

	public SubGUI(Player player, String title, Upgrades u) {
		super(title, 9);
		this.u = u;
		setClass(this);
		setContents(player);
	}

	@Override
	public void call(Player player, int slot) {
		UpgradesRolling ur = new UpgradesRolling(player);

		// If the user clicks the "back to main menu" button
		if (slot == 13) {
			new UpgradesMain(player).open(player);
			return;
		}

		// Evaluate the clicked upgrade
		if (ur.getLevel(u) < u.getMaxLevel()) {
			int cost = ur.getCost(u, ur.getLevel(u) + 1);
			if (Economy.canAfford(player.getUniqueId(), cost)) {
				Economy.subtractBalance(player.getUniqueId(), cost);
				ur.setLevel(u, ur.getLevel(u) + 1);
			} else {
				player.sendMessage("§cYou cannot afford to purchase this!");
			}
			refresh();
		}
	}

	@Override
	public void setContents(Player player) {
		UpgradesRolling ur = new UpgradesRolling(player);
		for (int i = 1; i < u.getMaxLevel() + 1; i++) {
			String name = u.getName() + " §6: §bLevel " + (i);
			if (ur.getLevel(u) >= i) {
				ItemBuilder ib = new ItemBuilder(Material.STAINED_GLASS_PANE, name, (short) 5).hideDetails();
				ib.setLore(u.getDesc());
				ib.addToLore("");
				ib.addToLore("§aUNLOCKED");
				ib.setName(name);
				getInventory().setItem(i - 1, ib.build());
			} else {
				// If the user can afford to purchase that level
				if (Economy.canAfford(player.getUniqueId(), ur.getCost(u, i))) {
					ItemBuilder ib = new ItemBuilder(Material.STAINED_GLASS_PANE, name, (short) 4).hideDetails(); // Yellow Stained Glass Pane
					ib.setLore(u.getDesc());
					ib.addToLore("");
					ib.addToLore("§a$" + ur.getCost(u, i));
					ib.addToLore("§6Click to buy");
					ib.setName(name);
					getInventory().setItem(i - 1, ib.build());
				} else { // If the user is NOT able to afford to purchase that level
					ItemBuilder ib = new ItemBuilder(Material.STAINED_GLASS_PANE, name, (short) 14).hideDetails(); // Red Stained Glass Pane
					ib.setLore(u.getDesc());
					ib.addToLore("");
					ib.addToLore("§a$" + ur.getCost(u, i));
					ib.addToLore("§cCannot afford");
					ib.setName(name);
					getInventory().setItem(i - 1, ib.build());
				}
			}
		}
	}
}
