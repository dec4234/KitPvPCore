package net.dec4234.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Map;

public class EnchantmentDragAndDropListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent ice) {
		ItemStack is = ice.getCurrentItem();
		ItemStack item = ice.getCursor();

		if (is != null && item != null) {
			if (item.getType() == Material.ENCHANTED_BOOK && item.getItemMeta() instanceof EnchantmentStorageMeta) {
				try {
					ice.setCancelled(true);
					EnchantmentStorageMeta esm = (EnchantmentStorageMeta) item.getItemMeta();
					Map<Enchantment, Integer> enchants = esm.getStoredEnchants();
					boolean temp = true;
					for(Enchantment enchant : enchants.keySet()) {
						if(!is.containsEnchantment(enchant) || is.getEnchantmentLevel(enchant) < enchants.get(enchant)) {
							is.addEnchantment(enchant, enchants.get(enchant));
							ice.getInventory().remove(item);
							temp = false;
						}
					}
					if(temp) ice.setCancelled(false);

				} catch (NullPointerException | IllegalArgumentException exception) {
					ice.setCancelled(false);
					return;
				}
			}
		}
	}

}
