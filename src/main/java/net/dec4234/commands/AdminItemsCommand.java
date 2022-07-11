package net.dec4234.commands;

import net.dec4234.files.PlayerStats;
import net.dec4234.guis.content.boosters.BoosterGUI;
import net.dec4234.guis.content.boosters.BoosterHandling;
import net.dec4234.guis.content.items.CustomItem;
import net.dec4234.guis.content.items.ItemType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminItemsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("adminitems")) {
			if (!sender.isOp()) {
				sender.sendMessage("§cYou do not have permission to use this command!");
				return true;
			}
			if (args.length >= 3) {
				Player op = Bukkit.getPlayer(args[0]);
				PlayerStats ps = new PlayerStats();
				int amount;
				String s = args[2];
				if (!ps.hasData(op.getUniqueId())) {
					sender.sendMessage("§d" + args[0] + " §cis not recognized as an online player");
					return true;
				}
				try {
					amount = Integer.parseInt(args[1]);
				} catch (NumberFormatException exception) {
					sender.sendMessage("§cThe amount of items to give must be a number!");
					return false;
				}

				// Check that all of the numbers meet the minimum requirements
				if (amount <= 0) {
					sender.sendMessage("§cThe amount of item(s) to give must be greater than 0");
					return false;
				}

				ItemType type = ItemType.parseFromIdentifier(s);

				if(type == null) {
					sender.sendMessage("§d" + args[2] + " §cmust be §ecobbleBlock§c, §egrapplingHook §cor §ethorHammer");
					return false;
				}

				// Give the custom item to the player
				new CustomItem(type, amount).giveToPlayer(op);
			} else {
				sender.sendMessage("§cPlease check the syntax for this command!");
				return false;
			}
		}
		return true;
	}
}
