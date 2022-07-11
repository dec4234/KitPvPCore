package net.dec4234.guis.content.boosters;

import lombok.Getter;
import net.dec4234.files.PlayerStats;
import net.dec4234.guis.framework.GUI;
import net.dec4234.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class BoosterGUI extends GUI {

	private Player player;

	public BoosterGUI(Player player) {
		super("§d§lBoosters", 9);
		setClass(this);
		this.player = player;
		setContents(player);
	}

	@Override
	public void call(Player player, int slot) {
		BoosterType type = parseFromSlot(player, slot);
		if(player != null && type != null) {
			if(!BoosterHandling.hasActiveBooster(player)) {
				if(BoosterHandling.hasBoosterInInventory(player, type)) {
					BoosterHandling.activate(player, type);
					player.sendMessage("§aActivated §bx" + type.getMultiplier() + " §abooster");
					BoosterHandling.decrementFromInventory(player, type);
					player.closeInventory();
				} else {
					player.sendMessage("§cYou do not have any boosters of that type!");
				}
			} else {
				player.sendMessage("§cYou already have an active booster!");
			}
		}
	}

	@Override
	public void setContents(Player player) {

		// 2.0x Boosters
		ItemBuilder m2 = new ItemBuilder(Material.IRON_INGOT).enchant().hideDetails();

		if(BoosterHandling.hasBoosterInInventory(player, BoosterType.BOOSTER21)) {
			addBooster(m2, "§6x2.0 Booster", BoosterType.BOOSTER21);
		}
		if(BoosterHandling.hasBoosterInInventory(player, BoosterType.BOOSTER22)) {
			addBooster(m2, "§6x2.0 Booster", BoosterType.BOOSTER22);
		}
		if(BoosterHandling.hasBoosterInInventory(player, BoosterType.BOOSTER24)) {
			addBooster(m2, "§6x2.0 Booster", BoosterType.BOOSTER24);
		}

		// 3.0x Boosters
		ItemBuilder m3 = new ItemBuilder(Material.GOLD_INGOT).enchant().hideDetails();
		if(BoosterHandling.hasBoosterInInventory(player, BoosterType.BOOSTER31)) {
			addBooster(m3, "§9x3.0 Booster", BoosterType.BOOSTER31);
		}
		if(BoosterHandling.hasBoosterInInventory(player, BoosterType.BOOSTER32)) {
			addBooster(m3, "§9x3.0 Booster", BoosterType.BOOSTER32);
		}
		if(BoosterHandling.hasBoosterInInventory(player, BoosterType.BOOSTER34)) {
			addBooster(m3, "§9x3.0 Booster", BoosterType.BOOSTER34);
		}

		// 4.0x Boosters
		ItemBuilder m4 = new ItemBuilder(Material.EMERALD).enchant().hideDetails();
		if(BoosterHandling.hasBoosterInInventory(player, BoosterType.BOOSTER41)) {
			addBooster(m4, "§ex4.0 Booster", BoosterType.BOOSTER41);
		}
		if(BoosterHandling.hasBoosterInInventory(player, BoosterType.BOOSTER42)) {
			addBooster(m4, "§ex4.0 Booster", BoosterType.BOOSTER42);
		}
		if(BoosterHandling.hasBoosterInInventory(player, BoosterType.BOOSTER44)) {
			addBooster(m4, "§ex4.0 Booster", BoosterType.BOOSTER44);
		}
	}

	public void addBooster(ItemBuilder ib, String name, BoosterType type) {
		int amount = new PlayerStats().getStatInt(player.getUniqueId(), type.getIdentifier());
		ib.setLore(Arrays.asList(""));

		switch(type.getHours()) {
			case 1:
				ib.setLore(Arrays.asList("§b1 hour"));
				break;
			case 2:
				ib.setLore(Arrays.asList("§a2 hours"));
				break;
			case 4:
				ib.setLore(Arrays.asList("§c4 hours"));
				break;
		}

		ib.addToLore(" ");

		if(amount == 0) {
			ib.addToLore("§40 §cowned");
		} else {
			ib.addToLore("§6" + amount + " §downed");
			ib.addToLore("§6Click to use");
		}
		ib.setName(name);
		if(amount > 0) {
			ib.setAmount(Math.min(amount, 64));
			getInventory().addItem(ib.build());
		}
	}

	public BoosterType parseFromSlot(Player player, int slot) {
		ArrayList<BoosterType> types = new ArrayList<>();

		for(BoosterType type : BoosterType.values()) {
			if(BoosterHandling.hasBoosterInInventory(player, type)) {
				types.add(type);
			}
		}

		try {
			return types.get(slot);
		} catch (IndexOutOfBoundsException exception) {

		}

		return null;
	}

	/**
	 * The different types of boosters available to players
	 */
	public enum BoosterType {

		// 2x Boosters
		BOOSTER21(2, 1, "booster-2-1", 3),
		BOOSTER22(2, 2, "booster-2-2", 4),
		BOOSTER24(2, 4, "booster-2-4", 5),

		// 3x Boosters
		BOOSTER31(3, 1, "booster-3-1", 12),
		BOOSTER32(3, 2, "booster-3-2", 13),
		BOOSTER34(3, 4, "booster-3-4", 14),

		// 4x Boosters
		BOOSTER41(4, 1, "booster-4-1", 21),
		BOOSTER42(4, 2, "booster-4-2", 22),
		BOOSTER44(4, 4, "booster-4-4", 23);

		@Getter private int multiplier;
		@Getter private int hours;
		@Getter private String identifier;
		@Getter private int slot;

		BoosterType(int multiplier, int hours, String identifier, int slot) {
			this.multiplier = multiplier;
			this.hours = hours;
			this.identifier = identifier;
			this.slot = slot;
		}

		public static BoosterType parseFromSlot(int slot) {
			for(BoosterType type : BoosterType.values()) {
				if(type.getSlot() == slot) {
					return type;
				}
			}

			return null;
		}

		public static BoosterType parseFromDetails(int multiplier, int length) {
			for(BoosterType type : BoosterType.values()) {
				if(type.getMultiplier() == multiplier && type.getHours() == length) {
					return type;
				}
			}

			return null;
		}
	}
}
