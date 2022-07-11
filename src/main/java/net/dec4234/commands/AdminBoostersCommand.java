package net.dec4234.commands;

import net.dec4234.files.PlayerStats;
import net.dec4234.guis.content.boosters.BoosterGUI;
import net.dec4234.guis.content.boosters.BoosterHandling;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminBoostersCommand implements CommandExecutor {

	/**
	 * We cannot use the command system that the other commands in this plugin use because
	 * those commands are only triggered when a player uses them, and we need this to be able
	 * to be used by both the console and the server itself.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		if (cmd.getName().equalsIgnoreCase("adminbooster")) {
			if (!sender.isOp()) {
				sender.sendMessage("§cYou do not have permission to use this command!");
				return true;
			}
			if (args.length >= 4) {
				OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
				PlayerStats ps = new PlayerStats();
				int amount;
				int multiplier;
				int length;
				if (!ps.hasData(op.getUniqueId())) {
					sender.sendMessage("§d" + args[0] + " §cis not recognized as a player that has logged on before.");
					return true;
				}
				try {
					amount = Integer.parseInt(args[1]);
					multiplier = Integer.parseInt(args[2]);
					length = Integer.parseInt(args[3]);
				} catch (NumberFormatException exception) {
					sender.sendMessage("§cThe amount, multiplier and length of the booster all must be a number!");
					return false;
				}

				// Check that all of the numbers meet the minimum requirements
				if (amount <= 0) {
					sender.sendMessage("§cThe amount of booster(s) to give must be greater than 0");
					return false;
				}
				if (multiplier != 2 && multiplier != 3 && multiplier != 4) {
					sender.sendMessage("§cThe multiplier of the booster type you are trying to give must be 2, 3, or 4");
					return false;
				}
				if (length != 1 && length != 2 && length != 4) {
					sender.sendMessage("§cThe length of the booster (in hours) must be 1, 2, or 4");
					return false;
				}

				BoosterGUI.BoosterType typeToGive = BoosterGUI.BoosterType.parseFromDetails(multiplier, length);
				sender.sendMessage("§aGave §6" + args[0] + " §aboosters");

				if (typeToGive != null) {
					BoosterHandling.addBoosterToInventory(op.getPlayer(), amount, typeToGive);
				} else {
					sender.sendMessage("§cSomething went wrong when giving a booster to that player");
				}

			} else {
				sender.sendMessage("§cPlease check the syntax for this command!");
				return false;
			}
		}
		return true;
	}
}
