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
    public static HashMap<String, Integer> hits = new HashMap<>();
    public static HashMap<UUID, List<LivingEntity>> playerDefenders = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().log(Level.INFO, "MelonKitPvP is starting...");
        getCommand("kitpvp").setExecutor(new KitPvpCommand());
        Bukkit.getPluginManager().registerEvents(new KitPvpListeners(), this);
        KitPvpSettings.initSettings();
    }

    @Override
    public void onDisable() {
        List<UUID> players = new ArrayList<>(kitPvpPlayers);
        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            KitPvP.disableKitPvp(player);
        }
        for (Map.Entry<UUID, List<LivingEntity>> entry : playerDefenders.entrySet())
            entry.getValue().forEach(LivingEntity::remove);
    }

    public static Main getPlugin() {
        return plugin;
    }
}
