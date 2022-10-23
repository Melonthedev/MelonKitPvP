package wtf.melonthedev.melonkitpvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class KitPvpInterfaces {

    public static void openMainMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE + "KitPVP - Main Menu");
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, Utils.createItem(Material.CYAN_STAINED_GLASS_PANE, " ", null, 1));
        ItemStack stats = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) stats.getItemMeta();
        assert meta != null;
        meta.setOwningPlayer(player);
        meta.setDisplayName(ChatColor.GREEN + "Your Stats");
        List<String> lore = new ArrayList<>();
        //STATS
        lore.add(ChatColor.RED + "  Kills: " + KitPvP.getKills(player.getName()));
        lore.add(ChatColor.GOLD + "  Coins: " + KitPvP.getCoins(player));
        lore.add(ChatColor.YELLOW + "  Hits: " + (Main.hits.get(player.getName()) == null ? "0" : String.valueOf(Main.hits.get(player.getName()))));
        lore.add(ChatColor.YELLOW + "  Killstreak: " + KitPvP.getKillStreak(player.getName()));
        lore.add(ChatColor.YELLOW + "  Best Killstreak: " + KitPvP.getMaxKillStreak(player.getName()));
        meta.setLore(lore);
        stats.setItemMeta(meta);
        inv.setItem(4, stats);
        inv.setItem(10, Utils.createItem(Material.CHEST, ChatColor.AQUA + "Select Kit", null, 1));
        inv.setItem(12, Utils.createItem(Material.REDSTONE, ChatColor.LIGHT_PURPLE + "Perks & Boosts", null, 1));
        inv.setItem(14, Utils.createItem(Material.COMPARATOR, ChatColor.GREEN + "Settings", null, 1));
        inv.setItem(22, Utils.createItem(Material.BARRIER, ChatColor.RED + "Close", null, 1));
        if (KitPvP.isInPVP(player)) inv.setItem(16, Utils.createItem(Material.RED_CONCRETE, ChatColor.RED + "Leave KitPvP", null, 1));
        else inv.setItem(16, Utils.createItem(Material.LIME_CONCRETE, ChatColor.GREEN + "Start KitPvP", null, 1));
        player.openInventory(inv);
        Main.inInventory.add(player.getUniqueId());
    }

    public static void openSettingsInterface(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27,  ChatColor.LIGHT_PURPLE + "KitPvP Settings");
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, Utils.createItem(Material.CYAN_STAINED_GLASS_PANE, " ", null, 1));
        inv.setItem(10, Utils.createItem(Material.IRON_SWORD, ChatColor.WHITE + "PvP System: " + (KitPvP.getSetting("useoldpvp") ? "1.8" : "1.9+"), ChatColor.GRAY + "Click to toggle between 1.8/1.9+", 1));
        inv.setItem(11, Utils.createItem(Material.GOLDEN_APPLE, ChatColor.WHITE + "Default Kill Bonus: " + (Objects.equals(KitPvP.getMultipleChoiceSetting("defaultkillbonus"), "gappleeffect") ? "Golden Apple Effect" : (Objects.equals(KitPvP.getMultipleChoiceSetting("defaultkillbonus"), "gapple") ? "Golden Apple" : "Full HP")), ChatColor.GRAY + "Click to toggle between Full HP/Golden Apple/Golden Apple Effects", 1));
        inv.setItem(12, Utils.createItem(Material.BARREL, ChatColor.WHITE + "Restock Kit On Kill: " + (Objects.equals(KitPvP.getMultipleChoiceSetting("restockonkill"), "foodblocks") ? "Only Food & Blocks" : (Objects.equals(KitPvP.getMultipleChoiceSetting("restockonkill"), "everything") ? "Everything" : "Off")), ChatColor.GRAY + "Click to toggle between Everything/Only Food & Blocks/Off", 1));
        inv.setItem(13, Utils.createItem(Material.IRON_PICKAXE, ChatColor.WHITE + "Breakable Blocks: " + (Objects.equals(KitPvP.getMultipleChoiceSetting("breakableblocks"), "kitblocks") ? "Blocks contained in kits" : (Objects.equals(KitPvP.getMultipleChoiceSetting("breakableblocks"), "all") ? "All" : "None")), ChatColor.GRAY + "Click to toggle between All/Blocks contained in kits/None", 1));
        inv.setItem(14, Utils.createItem(Material.SHIELD, ChatColor.WHITE + "Shields: ", ChatColor.GRAY + "Toggles if any kit could contain a shield" + (KitPvP.getSetting("enableshields") ? "On" : "Off"), 1));
        inv.setItem(15, Utils.createItem(Material.SPYGLASS, ChatColor.WHITE + "Not-In-PvP-Players: " + (Objects.equals(KitPvP.getMultipleChoiceSetting("neutralplayers"), "survival") ? "Survival" : (Objects.equals(KitPvP.getMultipleChoiceSetting("neutralplayers"), "disabledamageandfly") ? "Disable Damage & Enable Flying" : "Disable Damage")), ChatColor.GRAY + "Click to toggle between Survival/Disable Damage/Disable Damage & Enable Flying", 1));
        inv.setItem(16, Utils.createItem(Material.TNT, ChatColor.WHITE + "Creepers and TNT explodes: " + (KitPvP.getSetting("explodingentities") ? "On" : "Off"), ChatColor.GRAY + "Toggles if Creepers and TNT can destroy the map", 1));
        inv.setItem(22, Utils.createItem(Material.ARROW, ChatColor.WHITE + "Go Back", ChatColor.GRAY + "To Home", 1));
        inv.setItem(23, Utils.createItem(Material.BARREL, ChatColor.WHITE + "Restock All Kits", ChatColor.GRAY + "Restocks the current kit of all players", 1));
        player.openInventory(inv);
    }

    public static void openKitSelector(Player player, Player target) {
        Inventory kitInv = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE + "KitPVP - Select kit: " + target.getName());
        for (int i = 0; i < kitInv.getSize(); i++)
            kitInv.setItem(i, Utils.createItem(Material.CYAN_STAINED_GLASS_PANE, " ", null, 1));
        kitInv.setItem(10, Kit.STANDARD.getIcon());
        kitInv.setItem(11, Kit.PRO.getIcon());
        kitInv.setItem(12, Kit.ULTRA.getIcon());
        kitInv.setItem(13, Kit.EPIC.getIcon());
        kitInv.setItem(14, Kit.SNIPER.getIcon());
        kitInv.setItem(15, Kit.OP.getIcon());
        kitInv.setItem(16, Kit.ENDERMAN.getIcon());
        kitInv.setItem(22, Utils.createItem(Material.ARROW, ChatColor.RESET + "Go Back", ChatColor.GRAY + "To Home", 1));
        kitInv.setItem(26, Utils.createItem(Material.ARROW, ChatColor.RESET + "Next Page", ChatColor.GRAY + "(1/1)", 1));
        player.openInventory(kitInv);
        //Main.inInventory.add(player.getUniqueId());
    }

    public static void openKitSelector(Player player) {
        openKitSelector(player, player);
    }

    public static void openYesNoKitChangeGui(Player player, Kit newKit) {
        Inventory inv = Bukkit.createInventory(null, 27, "Change Kit to " + newKit.name());
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, Utils.createItem(Material.CYAN_STAINED_GLASS_PANE, " ", null, 1));
        inv.setItem(11, Utils.createItem(Material.BARRIER, ChatColor.RED + "Cancel", null, 1));
        inv.setItem(15, Utils.createItem(Material.GREEN_CONCRETE, ChatColor.GREEN + "Confirm",ChatColor.RED + "You WILL loose your current killstreak\n and get teleported back to spawn.\n" + ChatColor.GRAY +  ChatColor.GRAY + "New Kit: " + newKit.getColoredName() + ChatColor.GRAY, 1));
        player.openInventory(inv);
    }

    public static void openHotbarEditor(Player player, Kit kit) {
        Inventory inv = Bukkit.createInventory(null, 36,  ChatColor.LIGHT_PURPLE + "Hotbar Editor: " + kit.getColoredName());
        inv.setItem(0, Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, ChatColor.GREEN + "Inventory:", ChatColor.GRAY + "Here you put items that\n shouldn't be in you hotbar.", 1));
        for (int i = 9; i < 18; i++)
            inv.setItem(i, Utils.createItem(Material.CYAN_STAINED_GLASS_PANE, " ", null, 1));
        for (int i = 27; i < 36; i++)
            inv.setItem(i, Utils.createItem(Material.CYAN_STAINED_GLASS_PANE, " ", null, 1));
        inv.setItem(31, Utils.createItem(Material.LIME_STAINED_GLASS_PANE, ChatColor.RED + "Save Hotbar", null, 1));

        for (int i = 0; i < 9; i++) {
            ItemStack item = kit.getContents().getInventory()[i];
            if (item != null) item.setAmount(1);
            inv.setItem(i + 18, item);
        }

        //IDEA: wenn gespeichert wird, ursprüngliches item im slot anschauen und slot type merken und dann slottype zum slot setzen, wo item nun ist
        player.openInventory(inv);
    }

    public static void handleHotbarEditorClick(InventoryClickEvent event, int slot, ItemStack currentItem, Player player, String clearTitle) {
        if (event.getClickedInventory() == null || event.getClickedInventory() instanceof PlayerInventory) event.setCancelled(true);
        if (slot == 31 && event.getWhoClicked().getItemOnCursor().getType() == Material.AIR) {
            System.out.println("save");
            String kitName = clearTitle.replace("Hotbar Editor: ", "").toUpperCase();
            Kit kit = Kit.valueOf(kitName);
            HashMap<Material, HotbarItemOrder.Type> hotbarItems = new HashMap<>();
            System.out.println("Hotbar Default: "
                    + kit.getContents().getInventory()[0] + " " + kit.getContents().getHotbarItemOrder().slot1 + " "
                    + kit.getContents().getInventory()[1] + " " + kit.getContents().getHotbarItemOrder().slot2 + " "
                    + kit.getContents().getInventory()[2] + " " + kit.getContents().getHotbarItemOrder().slot3 + " "
                    + kit.getContents().getInventory()[3] + " " + kit.getContents().getHotbarItemOrder().slot4 + " "
                    + kit.getContents().getInventory()[4] + " " + kit.getContents().getHotbarItemOrder().slot5 + " "
                    + kit.getContents().getInventory()[5] + " " + kit.getContents().getHotbarItemOrder().slot6 + " "
                    + kit.getContents().getInventory()[6] + " " + kit.getContents().getHotbarItemOrder().slot7 + " "
                    + kit.getContents().getInventory()[7] + " " + kit.getContents().getHotbarItemOrder().slot8 + " "
                    + kit.getContents().getInventory()[8]);
            if (kit.getContents().getInventory()[0] != null) hotbarItems.put(kit.getContents().getInventory()[0].getType(), kit.getContents().getHotbarItemOrder().slot1);
            if (kit.getContents().getInventory()[1] != null) hotbarItems.put(kit.getContents().getInventory()[1].getType(), kit.getContents().getHotbarItemOrder().slot2);
            if (kit.getContents().getInventory()[2] != null) hotbarItems.put(kit.getContents().getInventory()[2].getType(), kit.getContents().getHotbarItemOrder().slot3);
            if (kit.getContents().getInventory()[3] != null) hotbarItems.put(kit.getContents().getInventory()[3].getType(), kit.getContents().getHotbarItemOrder().slot4);
            if (kit.getContents().getInventory()[4] != null) hotbarItems.put(kit.getContents().getInventory()[4].getType(), kit.getContents().getHotbarItemOrder().slot5);
            if (kit.getContents().getInventory()[5] != null) hotbarItems.put(kit.getContents().getInventory()[5].getType(), kit.getContents().getHotbarItemOrder().slot6);
            if (kit.getContents().getInventory()[6] != null) hotbarItems.put(kit.getContents().getInventory()[6].getType(), kit.getContents().getHotbarItemOrder().slot7);
            if (kit.getContents().getInventory()[7] != null) hotbarItems.put(kit.getContents().getInventory()[7].getType(), kit.getContents().getHotbarItemOrder().slot8);
            if (kit.getContents().getInventory()[8] != null) hotbarItems.put(kit.getContents().getInventory()[8].getType(), kit.getContents().getHotbarItemOrder().slot9);

            for (int i = 0; i < 9; i++) {
                ItemStack item = event.getClickedInventory().getItem(i + 18);
                if (item == null) continue;
                Material material = item.getType();
                if (kit.getContents().getInventory()[i] != null) item.setAmount(kit.getContents().getInventory()[i].getAmount());
                if (!hotbarItems.containsKey(material)) continue;
                kit.getContents().getInventory()[i] = item;
                kit.getContents().getHotbarItemOrder().setSlot(i, hotbarItems.get(material));
            }
            System.out.println("Hotbar New: "
                    + kit.getContents().getInventory()[0] + " " + kit.getContents().getHotbarItemOrder().slot1 + " "
                    + kit.getContents().getInventory()[1] + " " + kit.getContents().getHotbarItemOrder().slot2 + " "
                    + kit.getContents().getInventory()[2] + " " + kit.getContents().getHotbarItemOrder().slot3 + " "
                    + kit.getContents().getInventory()[3] + " " + kit.getContents().getHotbarItemOrder().slot4 + " "
                    + kit.getContents().getInventory()[4] + " " + kit.getContents().getHotbarItemOrder().slot5 + " "
                    + kit.getContents().getInventory()[5] + " " + kit.getContents().getHotbarItemOrder().slot6 + " "
                    + kit.getContents().getInventory()[6] + " " + kit.getContents().getHotbarItemOrder().slot7 + " "
                    + kit.getContents().getInventory()[7] + " " + kit.getContents().getHotbarItemOrder().slot8 + " "
                    + kit.getContents().getInventory()[8]);
            player.closeInventory();

        } else if ((slot >= 18 && slot <= 26) || (slot > 0 && slot < 9)) return;
        event.setCancelled(true);
    }

    public static void handleSettingsGuiClick(InventoryClickEvent event, int slot, ItemStack currentItem, Player player) {
        if (slot == 22) {
            openMainMenu(player);
            return;
        }
        switch (slot) {
            case 10:
                toggleNextSetting("useoldpvp", player);
                break;
            case 11:
                toggleNextMultipleChoiceSetting("defaultkillbonus", player);
                break;
            case 12:
                toggleNextMultipleChoiceSetting("restockonkill", player);
                break;
            case 13:
                toggleNextMultipleChoiceSetting("breakableblocks", player);
                break;
            case 14:
                toggleNextSetting("shields", player);
                break;
            case 15:
                toggleNextMultipleChoiceSetting("neutralplayers", player);
                break;
            case 16:
                toggleNextSetting("explodingentities", player);
                break;
            case 23:
                KitPvP.restockAllKits();
                player.closeInventory();
                break;
        }

    }

    public static void toggleNextSetting(String setting, Player executor) {
        if (!executor.isOp()) {
            executor.sendMessage(ChatColor.RED + "You are not allowed to change settings!");
            executor.closeInventory();
            return;
        }
        System.out.println("Toggling " + setting);
        KitPvP.setSetting(setting, !KitPvP.getSetting(setting));
        //Refresh
        openSettingsInterface(executor);
        KitPvP.refreshSettings();
    }

    public static void toggleNextMultipleChoiceSetting(String setting, Player executor) {

    }

    public static void handleMainMenuGuiClick(InventoryClickEvent event, int slot, ItemStack currentItem, Player player) {
        switch (slot) {
            case 10:
                openKitSelector(player);
                return;
            case 12:
                player.sendMessage(ChatColor.RED + "Sorry, but this feature is not yet implemented!");
                break;
            case 14:
                openSettingsInterface(player);
                return;
            case 16:
                if (currentItem.getType() == Material.LIME_CONCRETE) {
                    KitPvP.enableKitPvp(player);
                    player.sendMessage(ChatColor.AQUA + "[KitPvP] Du hast KitPvP eingeschaltet.");
                } else {
                    KitPvP.disableKitPvp(player);
                    player.sendMessage(ChatColor.AQUA + "[KitPvP] Du hast KitPvP ausgeschaltet.");
                }
                break;
            case 22:
                break;
            default:
                return;
        }
        player.closeInventory();
    }

    public static void handleSelectKitGuiClick(InventoryClickEvent event, int slot, ItemStack currentItem, Player player, String clearTitle) {
        String playerName = clearTitle.substring(21);
        Player target = Bukkit.getPlayer(playerName);
        boolean shouldBeClicked = !currentItem.isSimilar(Utils.createItem(Material.CYAN_STAINED_GLASS_PANE, " ", null, 1))
                && !Objects.equals(currentItem.getItemMeta().getDisplayName(), ChatColor.RESET + "Next Page")
                && !Objects.equals(currentItem.getItemMeta().getDisplayName(), ChatColor.RESET + "Go Back");
        if (shouldBeClicked && target == null) {
            player.sendMessage(ChatColor.RED + "Dieser Spieler ist nicht online.");
            player.closeInventory();
            return;
        }
        Kit selectedKit = Kit.STANDARD;
        if (Main.selectedKits.containsKey(target.getUniqueId())) Main.selectedKits.put(target.getUniqueId(), Kit.STANDARD);
        else selectedKit = Main.selectedKits.get(target.getUniqueId());
        assert selectedKit != null;
        switch (slot) {
            case 10:
                handleKitClickAction(target, Kit.STANDARD, player, event.isRightClick());
                break;
            case 11:
                handleKitClickAction(target, Kit.PRO, player, event.isRightClick());
                break;
            case 12:
                handleKitClickAction(target, Kit.ULTRA, player, event.isRightClick());
                break;
            case 13:
                handleKitClickAction(target, Kit.EPIC, player, event.isRightClick());
                break;
            case 14:
                handleKitClickAction(target, Kit.SNIPER, player, event.isRightClick());
                break;
            case 15:
                handleKitClickAction(target, Kit.OP, player, event.isRightClick());
                break;
            case 16:
                handleKitClickAction(target, Kit.ENDERMAN, player, event.isRightClick());
                break;
            case 22:
                openMainMenu(player);
                break;
        }
        /*if (shouldBeClicked) {
            Main.getPlugin().saveConfig();
            KitPvP.enableKitPvp(player);
            KitPvP.giveKit(target);
            player.closeInventory();
        }*/
    }

    public static void handleYesNoKitChangeGuiClick(InventoryClickEvent event, int slot, Player player, String clearTitle) {
        if (slot == 11)
            player.closeInventory();
        else if (slot == 15) {
            selectKit(player, Kit.valueOf(clearTitle.substring(14)), null);
            KitPvP.setKillStreak(player.getName(), 0);
            player.teleport(player.getWorld().getSpawnLocation());
        }
    }

    public static void handleKitClickAction(Player player, Kit kit, Player executor, boolean isRightClick) {
        if (isRightClick)
            openHotbarEditor(player, kit);
        else selectKit(player, kit, executor);
    }

    public static void selectKit(Player player, Kit kit, Player executor) {
        if (KitPvP.getKit(player) == kit && KitPvP.isInPVP(player)) {
            if (executor != null && executor != player) {
                executor.sendMessage(ChatColor.RED + "Dieser Spieler hat bereits dieses Kit ausgewählt.");
                executor.closeInventory();
            } else player.closeInventory();
            return;
        }
        if (executor == null || player != executor) {
            Main.selectedKits.put(player.getUniqueId(), kit);
            Main.getPlugin().saveConfig();
            KitPvP.enableKitPvp(player);
            KitPvP.giveKit(player);
            player.closeInventory();
        } else {
            openYesNoKitChangeGui(player, kit);
        }
    }
}
