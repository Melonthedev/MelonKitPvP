package wtf.melonthedev.melonkitpvp;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    private static Main plugin;
    public static List<UUID> kitPvpPlayers = new ArrayList<>();
    public static List<UUID> inInventory = new ArrayList<>();
    public static HashMap<UUID, Kit> selectedKits = new HashMap<>();
    public static HashMap<String, Integer> hits = new HashMap<>(); //TODO
    public static HashMap<UUID, List<LivingEntity>> defenders = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().log(Level.INFO, "MelonKitPvP is starting...");
        getCommand("kitpvp").setExecutor(new KitPvpCommand());
        Bukkit.getPluginManager().registerEvents(new KitPvpListeners(), this);

        if (KitPvP.getMultipleChoiceSetting("defaultkillbonus") == null
                || (!Objects.equals(KitPvP.getMultipleChoiceSetting("defaultkillbonus"), "fullhp")
                && !Objects.equals(KitPvP.getMultipleChoiceSetting("defaultkillbonus"), "gapple")
                && !Objects.equals(KitPvP.getMultipleChoiceSetting("defaultkillbonus"), "gappleeffect")))
            KitPvP.setMultipleChoiceSetting("defaultkillbonus", "gappleeffect");
        if (KitPvP.getMultipleChoiceSetting("restockonkill") == null
                || (!Objects.equals(KitPvP.getMultipleChoiceSetting("restockonkill"), "foodblocks")
                && !Objects.equals(KitPvP.getMultipleChoiceSetting("restockonkill"), "everything")
                && !Objects.equals(KitPvP.getMultipleChoiceSetting("restockonkill"), "off")))
            KitPvP.setMultipleChoiceSetting("restockonkill", "foodblocks");
        if (KitPvP.getMultipleChoiceSetting("breakableblocks") == null
                || (!Objects.equals(KitPvP.getMultipleChoiceSetting("breakableblocks"), "kitblocks")
                && !Objects.equals(KitPvP.getMultipleChoiceSetting("breakableblocks"), "all")
                && !Objects.equals(KitPvP.getMultipleChoiceSetting("breakableblocks"), "none")))
            KitPvP.setMultipleChoiceSetting("breakableblocks", "kitblocks");
        if (KitPvP.getMultipleChoiceSetting("neutralplayers") == null
                || (!Objects.equals(KitPvP.getMultipleChoiceSetting("neutralplayers"), "survival")
                && !Objects.equals(KitPvP.getMultipleChoiceSetting("neutralplayers"), "disabledamage")
                && !Objects.equals(KitPvP.getMultipleChoiceSetting("neutralplayers"), "disabledamageandfly")))
            KitPvP.setMultipleChoiceSetting("neutralplayers", "neutralplayers");
        if (!getConfig().contains("settings.enableshields")) KitPvP.setSetting("enableshields", true);
    }

    @Override
    public void onDisable() {
        List<UUID> players = new ArrayList<>(kitPvpPlayers);
        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            KitPvP.disableKitPvp(player);
        }
        for (Map.Entry<UUID, List<LivingEntity>> entry : defenders.entrySet()) {
            for (LivingEntity entity : entry.getValue())
                entity.remove();
        }
    }

    public static Main getPlugin() {
        return plugin;
    }
}
