package net.dec4234.guis.content.items;

import lombok.Getter;
import net.dec4234.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomItem {

	@Getter private ItemType type;
	@Getter private ItemStack itemStack;

	/**
	 * Create an itemstack of a customItem
	 */
	public CustomItem(ItemType type, int amount) {
		this.type = type;
		switch(type) {
			case THOR_HAMMER:
				this.itemStack = new ItemBuilder(Material.IRON_AXE, type.getName()).hideDetails().setLore(type.getDescription()).setAmount(amount).addNbtIdentifier(type.getIdentifier()).build();
				break;
			case GRAPPLING_HOOK:
				this.itemStack = new ItemBuilder(Material.FISHING_ROD, type.getName()).hideDetails().setLore(type.getDescription()).setAmount(amount).addNbtIdentifier(type.getIdentifier()).build();
				break;
			case BLOCKS:
				this.itemStack = new ItemBuilder(Material.COBBLESTONE, type.getName()).hideDetails().setLore(type.getDescription()).setAmount(amount).addNbtIdentifier(type.getIdentifier()).build();
				break;
		}
	}

	/**
	 * Give this user the itemStack belonging to this customitem
	 */
	public void giveToPlayer(Player player) {
		player.getInventory().addItem(itemStack);
	}
}
