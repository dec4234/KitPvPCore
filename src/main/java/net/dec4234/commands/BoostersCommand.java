package net.dec4234.commands;

import net.dec4234.guis.content.boosters.BoosterGUI;
import net.dec4234.guis.content.boosters.BoosterHandling;
import net.dec4234.guis.content.upgrades.UpgradesMain;
import org.bukkit.entity.Player;
import org.jefferies.core.utils.command.Command;

import static org.jefferies.core.utils.chat.Format.translate;

public class BoostersCommand extends Command {

	public BoostersCommand() {
		super("boosters");
	}

	@Override
	public void onExecute(Player player, String[] args) {
		if(BoosterHandling.hasAnyBoosters(player)) {
			BoosterGUI gui = new BoosterGUI(player);
			gui.open(player);
		} else {
			player.sendMessage(translate("&cYou don't have any boosters!"));
		}
	}
}
