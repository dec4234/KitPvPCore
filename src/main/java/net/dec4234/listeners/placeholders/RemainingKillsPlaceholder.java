package net.dec4234.listeners.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.dec4234.files.PlayerStats;
import org.bukkit.entity.Player;
import org.jefferies.core.economy.Economy;
import org.jetbrains.annotations.NotNull;

public class RemainingKillsPlaceholder extends PlaceholderExpansion {

	@Override
	public @NotNull String getIdentifier() {
		return "killsForCrate";
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

		if(identifier.equals("killsForCrate")) {
			PlayerStats ps = new PlayerStats();
			int kills = ps.getStatInt(player.getUniqueId(), "kills");
			return kills % 25 == 0 ? 25 + "" : kills % 25 + "";
		}

		return null;
	}
}