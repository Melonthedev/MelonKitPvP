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
        //} else if (args.length <= 1) {
        //    player.sendMessage(ChatColor.RED + "Syntaxerror: /kitpvp");
        //    return true;
        } else if (!player.isOp())
            return true;

        //ADMIN COMMANDS
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("resetMap")) {
                KitPvP.resetMap();
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
