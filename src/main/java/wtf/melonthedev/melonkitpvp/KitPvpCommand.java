package wtf.melonthedev.melonkitpvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

public class KitPvpCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            KitPvpInterfaces.openMainMenu(player);
            return true;
        } else if (!player.isOp())
            return true;

        //ADMIN COMMANDS
        if (args.length == 1) {
            switch (args[0]) {
                case "resetMap":
                    KitPvP.resetMap();
                    return true;
                case "settingsMappings":
                    sender.sendMessage(ChatColor.AQUA + "Settings Mappings:");
                    sender.sendMessage(ChatColor.AQUA + "Default kill bonus (id: defaultkillbonus): 1 - Full HP, 2 - Gapple, 3 - Gapple Effect");
                    sender.sendMessage(ChatColor.AQUA + "Restock on kill (id: restockonkill): 1 - Food & Blocks, 2 - Everything, 3 - Off");
                    sender.sendMessage(ChatColor.AQUA + "Breakable blocks (id: breakableblocks): 1 - Kit Blocks, 2 - All, 3 - None");
                    sender.sendMessage(ChatColor.AQUA + "Not-In-PvP-Players (id: neutralplayers): 1 - Survival, 2 - No Damage, 3 - No Damage & Fly Mode");
                    sender.sendMessage(ChatColor.AQUA + "Map Reset (id: neutralplayers): 1 - Alle 5 Minuten, 2 - Alle 15 Minuten, 3 - Alle 30 Minuten, 4 - Jede Stunde, 5 - Nie");
                    sender.sendMessage(ChatColor.AQUA + "-------------------------------------------");
                    return true;
            }
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Dieser spieler ist nicht online.");
                return true;
            }
            if (args[0].equalsIgnoreCase("addPlayer")) {
                KitPvP.enableKitPvp(target);
                player.sendMessage(ChatColor.AQUA + "Du hast den Spieler " + target.getName() + " zu der PVP Liste hinzugefügt.");
            } else if (args[0].equalsIgnoreCase("removePlayer")) {
                KitPvP.disableKitPvp(target);
                player.sendMessage(ChatColor.AQUA + "Du hast den Spieler " + target.getName() + " von der PVP Liste entfernt.");
            } else if (args[0].equalsIgnoreCase("setKit")) {
                KitPvpInterfaces.openKitSelector(player, target, 1);
            } else if (args[0].equalsIgnoreCase("resetKit")) {
                KitPvP.enableKitPvp(target);
                KitPvP.giveKit(target);
            } else if (args[0].equalsIgnoreCase("getCoins")) {
                player.sendMessage(ChatColor.AQUA + "Balance from Player " + target.getName() + ": " + ChatColor.BOLD + KitPvP.getCoins(target));
            }
        } else if (args.length == 3) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Dieser spieler ist nicht online.");
                return true;
            }
            if (args[0].equalsIgnoreCase("setCoins")) {
                player.sendMessage(ChatColor.AQUA + "New balance from Player " + target.getName() + ": " + ChatColor.BOLD + KitPvP.setCoins(target, Integer.parseInt(args[2])));
            }
        }
        KitPvP.updateScoreboard();
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new ArrayList<>();
        if (!sender.isOp()) return tab;
        if (args.length == 1) {
            tab.add("addPlayer");
            tab.add("removePlayer");
            tab.add("setKit");
            tab.add("setCoins");
            tab.add("getCoins");
            tab.add("resetKit");
            tab.add("resetMap");
        } else if (args.length == 2)
            Bukkit.getOnlinePlayers().forEach(player -> tab.add(player.getName()));
        return tab;
    }
}
