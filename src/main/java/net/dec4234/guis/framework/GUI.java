package net.dec4234.guis.framework;

import lombok.Getter;
import net.dec4234.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public abstract class GUI implements GUInterface {

	@Getter private String title;
	@Getter private Inventory inventory;
	@Getter private static HashMap<String, GUI> classes = new HashMap<>();

	public GUI(String title, int slots) {
		inventory = Bukkit.createInventory(null, slots, title);
		this.title = title;
	}

	public void setClass(GUI gui) {
		classes.put(title, gui);
	}

	public void open(Player player) {
		player.openInventory(inventory);
	}

	/**
	 * Set this inventory slot to a gray stained glass pane with no attributes
	 */
	public void setToFiller(int slot) {
		ItemBuilder ib = new ItemBuilder(Material.STAINED_GLASS_PANE, " ", (short) 7).hideDetails();
		inventory.setItem(slot, ib.build());
	}

	/**
	 * Refresh the inventory for all viewers to reflect new changes
	 */
	public void refresh() {
		getInventory().getViewers().forEach(humanEntity -> {
			setContents((Player) humanEntity);
			((Player) humanEntity).updateInventory();
		});
	}
}
