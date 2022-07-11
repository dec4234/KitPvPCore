package net.dec4234.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerBountyChangeEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();
	@Getter private Player player;
	@Getter int oldAmount;
	@Getter int newAmount;
	@Getter int amountDifference;

	public PlayerBountyChangeEvent(Player player, int oldAmount, int newAmount) {
		this.player = player;
		this.oldAmount = oldAmount;
		this.newAmount = newAmount;
		this.amountDifference = newAmount - oldAmount;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
}
