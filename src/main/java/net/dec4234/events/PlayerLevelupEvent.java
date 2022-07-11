package net.dec4234.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLevelupEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();
	@Getter private Player player;
	@Getter int oldLevel;
	@Getter int newLevel;
	@Getter int levelDifference;

	public PlayerLevelupEvent(Player player, int oldLevel, int newLevel) {
		this.player = player;
		this.oldLevel = oldLevel;
		this.newLevel = newLevel;
		this.levelDifference = newLevel - oldLevel;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
}
