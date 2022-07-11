package net.dec4234.guis.content.upgrades;

import lombok.Getter;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public enum Upgrades {

	SPEED("§eSpeed", Arrays.asList("§6Receive §dSpeed I §6upon every kill"), "Aa", 0, 9,0, Material.POTION),
	REGEN("§cRegeneration", Arrays.asList("§6Receive §bRegeneration II §6upon every kill"), "Bb", 6, 9,1, Material.GHAST_TEAR),
	STRENGTH("§5Strength", Arrays.asList("§6Receive §cStrength I §6upon every kill"), "Cc", 10, 9,2, Material.BLAZE_POWDER),
	DAMAGE_REDUCTION("§9Reduced Environmental Damage", Arrays.asList("§6Receive reduced environmental damage"), "Dd", 20, 9,3, Material.BEACON),
	MONOPOLY("§3Monopoly", Arrays.asList("§6Increasing chances to receive §ddouble coins §6on every kill"),"Ee", 30, 9,4, Material.EMERALD),
	FROSTBITE("§fFrostbite", Arrays.asList("§6Increasing chances to give opponets §dSlowness I", "§6when they are hit with your arrows"), "Ff", 40, 5,5, Material.SNOW_BALL),
	ABSORPTION("§eAbsorption", Arrays.asList("§6Receive increasing stacks of §2Absorption §6upon every kill"), "Gg", 50, 5,6, Material.GOLDEN_APPLE),
	FLAMING("§4Flaming Arrows", Arrays.asList("§6Your arrows have a chance to catch §cFire"), "Hh", 60, 9,7, Material.ARROW),
	REFUND("§aRefund", Arrays.asList("§6You have a chance to ", "§dGet some of your items back on death"), "Ii", 70, 9,8, Material.GOLD_INGOT);

	@Getter private String name;
	@Getter private List<String> desc;
	@Getter private String identifier;
	@Getter private int minLevel;
	@Getter private int maxLevel;
	@Getter private int defaultSlot;
	@Getter private Material material;

	Upgrades(String name, List<String> desc, String identifier, int minLevel, int maxLevel, int defaultSlot, Material mat) {
		this.name = name;
		this.desc = desc;
		this.identifier = identifier;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		this.defaultSlot = defaultSlot;
		this.material = mat;
	}
}
