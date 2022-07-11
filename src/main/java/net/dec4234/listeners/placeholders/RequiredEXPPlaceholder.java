package net.dec4234.listeners.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.dec4234.files.PlayerStats;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RequiredEXPPlaceholder extends PlaceholderExpansion {

	@Override
	public @NotNull String getIdentifier() {
		return "requiredXp";
	}

	@Override
	public boolean persist(){
		return true;
	}

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public @NotNull String getAuthor() {
		return "dec4234";
	}

	@Override
	public @NotNull String getVersion() {
		return "1.0.0";
	}

	@Override
	public String onPlaceholderRequest(Player player, String identifier) {
		if(player == null) {
			return "";
		}

		if(identifier.equals("requiredXp")) {
			PlayerStats ps = new PlayerStats();
			return "" + ps.xpForNextLevel(player.getUniqueId());
		}

		return null;
	}
}