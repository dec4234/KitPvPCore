package net.dec4234.guis.framework;

import net.dec4234.guis.content.upgrades.UpgradesMain;
import net.dec4234.src.KitPvPCoreMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class GUIListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent ice) {
		Inventory inv = ice.getClickedInventory();
		if (inv == null || ice.getCursor() == null) {
			return;
		}

		if (GUI.getClasses().containsKey(inv.getTitle())) {
			GUI.getClasses().get(inv.getTitle()).call((Player) ice.getWhoClicked(), ice.getSlot());
			ice.setCancelled(true);
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if (event.getView().getTitle().contains("§d§lUpgrades §b➔")) {
			Player p = (Player) event.getPlayer();

			Bukkit.getScheduler().runTaskLater(KitPvPCoreMain.getInstance(), () -> {
				new UpgradesMain(p).open(p);
			}, 1);
		}
	}
}
