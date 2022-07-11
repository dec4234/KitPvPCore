package net.dec4234.guis.content.items;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum ItemType {

	THOR_HAMMER("§cThor's Hammer", "thorHammer", Arrays.asList("§bDo §62x §bdamage when you hit", "§ba player in the back", "§dPermanent §cStrength I §dwhen holding", "", "§65s §cAbility Cooldown")),
	GRAPPLING_HOOK("§eGrappling Hook", "grapplingHook", Arrays.asList("§dGrapple to ledges!", "", "§65s §cCooldown")),
	BLOCKS("§dBlock", "cobbleBlock", Arrays.asList("§eUse these to build to new places!", "", "§cDespawns After §62 mins"));

	@Getter private String name;
	@Getter private String identifier;
	@Getter private List<String> description;

	ItemType(String name, String identifier, List<String> description) {
		this.name = name;
		this.identifier = identifier;
		this.description = description;
	}

	public static ItemType parseFromIdentifier(String identifier) {
		for(ItemType type : ItemType.values()) {
			if(type.getIdentifier().equals(identifier)) {
				return type;
			}
		}

		return null;
	}
}
