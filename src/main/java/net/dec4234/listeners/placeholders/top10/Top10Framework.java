package net.dec4234.listeners.placeholders.top10;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.dec4234.files.PlayerStats;
import net.dec4234.files.YmlConfig;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Top10Framework extends PlaceholderExpansion {

	private String identifier;
	private int place;

	public Top10Framework(int place) {
		this.identifier = "topXp" + place;
		this.place = place;
		register();
	}

	@Override
	public @NotNull String getIdentifier() {
		return identifier;
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

		if(identifier.equals(this.identifier)) {
			if(YmlConfig.getFC().getString("number" + place).equals("")) {
				return "";
			}

			PlayerStats ps = new PlayerStats();
			OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(YmlConfig.getFC().getString("number" + place)));
			return op.getName() + " - Level " + ps.getStatInt(op.getUniqueId(), "level");
		}

		return null;
	}
}
