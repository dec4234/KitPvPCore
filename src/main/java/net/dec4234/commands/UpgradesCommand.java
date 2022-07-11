package net.dec4234.commands;

import net.dec4234.guis.content.upgrades.UpgradesMain;
import org.bukkit.entity.Player;
import org.jefferies.core.utils.command.Command;

public class UpgradesCommand extends Command {

	public UpgradesCommand() {
		super("upgrades");
	}

	@Override
	public void onExecute(Player player, String[] args) {
		UpgradesMain um = new UpgradesMain(player);
		um.setContents(player);
		um.open(player);
	}
}
