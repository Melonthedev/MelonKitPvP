package wtf.melonthedev.melonkitpvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;
import org.checkerframework.checker.units.qual.K;

import java.util.*;

public class KitPvP {

    public static FileConfiguration config = Main.getPlugin().getConfig();

    public static void enableKitPvp(Player player) {
        //PlayerUtils.makeInvBackup(player);
        Main.kitPvpPlayers.add(player.getUniqueId());
        player.getInventory().clear();
        giveKit(player);
        prepareForPvp(player);
        setupScoreboard(player);
        updateScoreboard();
        if (getSetting("useoldpvp")) oldAttackSpeed(player);
        Bukkit.broadcastMessage(ChatColor.GREEN + ">> KitPvP " + ChatColor.GRAY + player.getName());
        Main.hits.put(player.getName(), config.getInt(player.getName() + ".kitpvp.hits"));
    }

    public static void disableKitPvp(Player player) {
        Main.kitPvpPlayers.remove(player.getUniqueId());
        player.getInventory().clear();
        hideScoreboard(player);
        //if (PlayerUtils.getInvBackup(player) != null) player.getInventory().setContents(PlayerUtils.getInvBackup(player));
        newAttackSpeed(player);
        Bukkit.broadcastMessage(ChatColor.RED + "<< KitPvP " + ChatColor.GRAY + player.getName());
        if (Main.hits.get(player.getName()) != null) {
            config.set(player.getName() + ".kitpvp.hits", Main.hits.get(player.getName()));
            Main.getPlugin().saveConfig();
        }
    }

    public static void setupScoreboard(Player player) {
        Scoreboard scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        Objective pvpStats = scoreboard.registerNewObjective("KitPvpScoreboard", "dummy", ChatColor.AQUA + ChatColor.BOLD.toString() + "<< KitPVP Statistics>>");
        pvpStats.setDisplaySlot(DisplaySlot.SIDEBAR);
        pvpStats.setDisplayName(ChatColor.AQUA + ChatColor.BOLD.toString() + "<< KitPVP Statistics >>");
        pvpStats.getScore(ChatColor.AQUA + ChatColor.BOLD.toString() + "-----------------------").setScore(12);
        pvpStats.getScore(ChatColor.BOLD + "Your Kit: " + (getKit(player) == null ? "None" : getKit(player).getName())).setScore(11);
        pvpStats.getScore("  ").setScore(10);
        pvpStats.getScore(ChatColor.WHITE + "Kills: " + ChatColor.BOLD + config.getInt(player.getName() + ".kitpvp.kills")).setScore(9);
        pvpStats.getScore(ChatColor.WHITE + "Kill Streak: " + ChatColor.BOLD + getKillStreak(player.getName())).setScore(8);
        pvpStats.getScore(ChatColor.WHITE + "Best Kill Streak: " + ChatColor.BOLD + getMaxKillStreak(player.getName())).setScore(7);
        pvpStats.getScore(ChatColor.GOLD + "Coins: " + ChatColor.BOLD + getCoins(player) + "$").setScore(6);
        pvpStats.getScore(" ").setScore(5);
        pvpStats.getScore(" ").setScore(4);
        HashMap<String, Integer> kills = new HashMap<>();
        for (String name : config.getKeys(false)) kills.put(name, getKills(name));
        int[] mostKills = new int[3];
        String[] mostKiller = new String[3];
        for (Map.Entry<String, Integer> kill : kills.entrySet()) {
            if (kill.getValue() > mostKills[0]) {
                mostKills[2] = mostKills[1];
                mostKiller[2] = mostKiller[1];
                mostKills[1] = mostKills[0];
                mostKiller[1] = mostKiller[0];
                mostKills[0] = kill.getValue();
                mostKiller[0] = kill.getKey();
            } else if (kill.getValue() > mostKills[1]) {
                mostKills[2] = mostKills[1];
                mostKiller[2] = mostKiller[1];
                mostKills[1] = kill.getValue();
                mostKiller[1] = kill.getKey();
            } else if (kill.getValue() > mostKills[2]) {
                mostKills[2] = kill.getValue();
                mostKiller[2] = kill.getKey();
            }
        }
        pvpStats.getScore(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Top 3 killers:").setScore(3);
        pvpStats.getScore(ChatColor.DARK_AQUA + "1. " + (mostKiller[0] == null ? "None" : mostKiller[0]) + " - " + mostKills[0]).setScore(2);
        pvpStats.getScore(ChatColor.DARK_AQUA + "2. " + (mostKiller[1] == null ? "None" : mostKiller[1]) + " - " + mostKills[1]).setScore(1);
        pvpStats.getScore(ChatColor.DARK_AQUA + "3. " + (mostKiller[2] == null ? "None" : mostKiller[2]) + " - " + mostKills[2]).setScore(0);

        //Health
        Objective heathDisplay = scoreboard.registerNewObjective("KitPvpHeath", "health", ChatColor.RED + "❤");
        heathDisplay.setDisplaySlot(DisplaySlot.BELOW_NAME);
        heathDisplay.setRenderType(RenderType.HEARTS);
        player.setScoreboard(scoreboard);
    }

    public static void updateScoreboard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!isInPVP(player)) continue;
            setupScoreboard(player);
        }
    }

    public static void hideScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        scoreboard.clearSlot(DisplaySlot.BELOW_NAME);
    }

    public static void prepareForPvp(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.setHealthScale(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setInvisible(false);
        player.setInvulnerable(false);
    }

    public static void oldAttackSpeed(Player player) {
        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)).setBaseValue(100);
    }
    public static void newAttackSpeed(Player player) {
        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)).setBaseValue(4);
    }

    public static int getKills(String playerName) {
        return config.getInt(playerName + ".kitpvp.kills");
    }

    public static void setKills(String playerName, int kills) {
        config.set(playerName + ".kitpvp.kills", kills);
        Main.getPlugin().saveConfig();
    }

    public static int getKillStreak(String playerName) {
        return config.getInt(playerName + ".kitpvp.killstreak");
    }

    public static void setKillStreak(String playerName, int kills) {
        config.set(playerName + ".kitpvp.killstreak", kills);
        Main.getPlugin().saveConfig();
        if (kills > getMaxKillStreak(playerName)) setMaxKillStreak(playerName, kills);
    }

    public static int getMaxKillStreak(String playerName) {
        return config.getInt(playerName + ".kitpvp.maxkillstreak");
    }

    public static void setMaxKillStreak(String playerName, int kills) {
        config.set(playerName + ".kitpvp.maxkillstreak", kills);
        Main.getPlugin().saveConfig();
    }

    public static int addKills(String playerName, int kills) {
        setKills(playerName, getKills(playerName) + kills);
        return getKills(playerName);
    }

    public static boolean isInPVP(Player player) {
        return Main.kitPvpPlayers.contains(player.getUniqueId());
    }

    public static int getCoins(Player player) {
        return config.getInt(player.getName() + ".kitpvp.coins");
    }

    public static int addCoins(Player player, int coins) {
        return setCoins(player, getCoins(player) + coins);
    }

    public static int setCoins(Player player, int coins) {
        config.set(player.getName() + ".kitpvp.coins", coins);
        Main.getPlugin().saveConfig();
        return getCoins(player);
    }

    public static void refreshSettings() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!isInPVP(player)) continue;
            if (getSetting("useoldpvp")) oldAttackSpeed(player);
            else newAttackSpeed(player);
        }
    }

    public static boolean getSetting(String setting) {
        return config.getBoolean("settings." + setting);
    }

    public static void setSetting(String setting, boolean flag) {
        config.set("settings." + setting, flag);
        Main.getPlugin().saveConfig();
    }

    public static String getMultipleChoiceSetting(String setting) {
        return config.getString("settings." + setting);
    }

    public static void setMultipleChoiceSetting(String setting, String value) {
        config.set("settings." + setting, value);
        Main.getPlugin().saveConfig();
    }

    public static void giveKit(Player player) {
        System.out.println("Giving Kit " + getKit(player).getName());
        getKit(player).loadKitInventory(player);
    }

    public static Kit getKit(Player player) {
        if (!Main.selectedKits.containsKey(player.getUniqueId())) Main.selectedKits.put(player.getUniqueId(), Kit.STANDARD);
        return Main.selectedKits.get(player.getUniqueId());
    }

    public static void restockAllKits() {
        for (UUID uuid : Main.kitPvpPlayers) {
            if (Bukkit.getPlayer(uuid) == null) continue;
            giveKit(Bukkit.getPlayer(uuid));
        }
    }
}
