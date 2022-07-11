package net.dec4234.commands;

import net.dec4234.files.PlayerStats;
import net.dec4234.files.YmlConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jefferies.core.economy.Economy;

import static org.jefferies.core.utils.chat.Format.translate;

public class BountyCommand extends org.jefferies.core.utils.command.Command {

	public BountyCommand() {
		super("bounty");
	}

	@Override
	public void onExecute(Player player, String[] args) {
		if(args.length < 2) {
			player.sendMessage(translate("&cPlease check the syntax &b/bounty &a<player> &6<amount>"));
			return;
		} else {
			OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
			PlayerStats ps = new PlayerStats();
			Integer i;
			if(Economy.hasData(op.getUniqueId())) {
				int currentBounty = ps.getStatInt(op.getUniqueId(), "bounty");
				try {
					i = Integer.parseInt(args[1]);
					if(Economy.canAfford(player.getUniqueId(), i)) {
						Economy.subtractBalance(player.getUniqueId(), i);
						String format;
						if(currentBounty != 0) {
							format = YmlConfig.getFC().getString("bounty-put-announcement");
							format = format.replace("$player_name$", player.getDisplayName());
							format = format.replace("$bounty_amount$", i + "");
							format = format.replace("$bountyd_name$", op.getName());
							format = format.replace("$total_bounty$", (currentBounty + i) + "");

							// Announce to the server that their is now a bounty on that player
						} else {
							format = YmlConfig.getFC().getString("bounty-put-announcement-when-0");
							format = format.replace("$player_name$", player.getDisplayName());
							format = format.replace("$bounty_amount$", "" + i);
							format = format.replace("$bountyd_name$", op.getName());

							// Announce to the server that their is now a bounty on that player
						}
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', format));
						ps.setBounty(op.getUniqueId(), i + currentBounty);
					} else {
						player.sendMessage(translate("&cYou cannot afford to pay &d" + i + " &cyour balance is &a" + Economy.getBalance(player.getUniqueId())));
					}
				} catch (NumberFormatException exception) {
					player.sendMessage(translate("&d" + args[1] + " &cis not a valid number. Try again."));
				}
			} else {
				player.sendMessage(translate("&cIt appears that the user &6" + args[0] + " &dhas never logged in."));
				return;
			}
		}
	}

}
