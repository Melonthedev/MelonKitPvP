package wtf.melonthedev.melonkitpvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class KitPvpInterfaces {

    public static void openMainMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.BLUE + "KitPVP - Main Menu");
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

    public static void openKitSelector(Player player, Player target, int page) {
        Inventory kitInv = Bukkit.createInventory(null, 27, ChatColor.BLUE + "Select kit: " + target.getName());
        for (int i = 0; i < kitInv.getSize(); i++)
            kitInv.setItem(i, Utils.createItem(Material.CYAN_STAINED_GLASS_PANE, " ", null, 1));

        int kitSlot = 10;
        for (int i = 0; i <= 7; i++) {
            int kitIndex = i + ((page-1) * 7);
            ItemStack stack;
            if (kitIndex >= Kit.values().length) stack = Utils.createItem(Material.CYAN_STAINED_GLASS_PANE, " ", null, 1);
            else stack = Kit.values()[kitIndex].getIcon();
            kitInv.setItem(kitSlot, stack);
            kitSlot ++;
            if (kitSlot >= 17) break;
        }
        int maxPages = (int)Math.ceil((float) Kit.values().length / 7);
        kitInv.setItem(22, Utils.createItem(Material.ARROW, ChatColor.RESET + "Go Back", ChatColor.GRAY + "To Home", 1));
        if (page > 1) kitInv.setItem(9, Utils.createItem(Material.ARROW, ChatColor.RESET + "Previous Page", ChatColor.GRAY + "(" + page + "/" + maxPages + ")", 1));
        if (page != maxPages) kitInv.setItem(17, Utils.createItem(Material.ARROW, ChatColor.RESET + "Next Page", ChatColor.GRAY + "(" + page + "/" + maxPages + ")", 1));
        player.openInventory(kitInv);
    }
    public static void openKitSelector(Player player) {
        openKitSelector(player, player, 1);
    }
    public static void handleSelectKitGuiClick(InventoryClickEvent event, int slot, ItemStack currentItem, Player player, String clearTitle) {
        String playerName = clearTitle.substring(12);
        Player target = Bukkit.getPlayer(playerName);
        switch (slot) {
            case 22 -> openMainMenu(player);
            case 9 -> {
                if (!currentItem.hasItemMeta() || !currentItem.getItemMeta().hasLore()) return;
                int page = Integer.parseInt(ChatColor.stripColor(currentItem.getItemMeta().getLore().get(0)).substring(1).split("/")[0]);
                if (page <= 1) return;
                openKitSelector(player, target, page - 1);
            }
            case 17 -> {
                if (!currentItem.hasItemMeta() || !currentItem.getItemMeta().hasLore()) return;
                String lore = ChatColor.stripColor(currentItem.getItemMeta().getLore().get(0));
                int page1 = Integer.parseInt(lore.substring(1).split("/")[0]);
                int maxPage = Integer.parseInt(lore.substring(1, lore.length() - 1).split("/")[1]);
                if (page1 >= maxPage) return;
                openKitSelector(player, target, page1 + 1);
            }
        }
        if (slot >= 10 && slot <= 16) {
            event.setCancelled(true);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Dieser Spieler ist nicht online.");
                player.closeInventory();
                return;
            }
            if (!currentItem.getItemMeta().hasDisplayName()) return;
            Kit kit = Kit.valueOf(ChatColor.stripColor(currentItem.getItemMeta().getDisplayName()).toUpperCase());
            handleKitClickAction(target, kit, player, event.isRightClick());
        }
    }

    public static void openYesNoKitChangeGui(Player player, Kit newKit) {
        Inventory inv = Bukkit.createInventory(null, 27, "Change Kit to " + newKit.getColoredName());
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, Utils.createItem(Material.CYAN_STAINED_GLASS_PANE, " ", null, 1));
        inv.setItem(11, Utils.createItem(Material.BARRIER, ChatColor.RED + "Cancel", null, 1));
        inv.setItem(15, Utils.createItem(Material.GREEN_CONCRETE, ChatColor.GREEN + "Confirm",ChatColor.RED + "You WILL loose your current killstreak\n and get teleported back to spawn.\n" + ChatColor.GRAY +  ChatColor.GRAY + "New Kit: " + newKit.getColoredName() + ChatColor.GRAY, 1));
        inv.setItem(22, Utils.createItem(Material.ARROW, ChatColor.RESET + "Go Back", ChatColor.GRAY + "To Kit Selector", 1));
        player.openInventory(inv);
    }
    public static void handleYesNoKitChangeGuiClick(InventoryClickEvent event, int slot, Player player, String clearTitle) {
        switch (slot) {
            case 11 -> player.closeInventory();
            case 22 -> openKitSelector(player);
            case 15 -> {
                selectKit(player, Kit.valueOf(clearTitle.toUpperCase().substring(14)), null);
                KitPvP.setKillStreak(player.getName(), 0);
                player.teleport(player.getWorld().getSpawnLocation());
            }
        }
    }

    public static void openSettingsInterface(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27,  ChatColor.BLUE + "KitPvP Settings");
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, Utils.createItem(Material.CYAN_STAINED_GLASS_PANE, " ", null, 1));
        inv.setItem(10, Utils.createItem(Material.IRON_SWORD, ChatColor.WHITE + "PvP System: " + (KitPvP.getSetting("useoldpvp") ? "1.8" : "1.9+"), ChatColor.GRAY + "Click to toggle between 1.8/1.9+", 1));
        inv.setItem(11, Utils.createItem(Material.GOLDEN_APPLE, ChatColor.WHITE + "Default Kill Bonus: " + (Objects.equals(KitPvP.getMultipleChoiceSetting("defaultkillbonus"), "1") ? "Full HP" : (Objects.equals(KitPvP.getMultipleChoiceSetting("defaultkillbonus"), "2") ? "Golden Apple" : "Golden Apple Effects")), ChatColor.GRAY + "Click to toggle between Full HP/Golden Apple/Golden Apple Effects", 1));
        inv.setItem(12, Utils.createItem(Material.BARREL, ChatColor.WHITE + "Restock Kit On Kill: " + (Objects.equals(KitPvP.getMultipleChoiceSetting("restockonkill"), "1") ? "Only Food & Blocks" : (Objects.equals(KitPvP.getMultipleChoiceSetting("restockonkill"), "2") ? "Everything" : "Off")), ChatColor.GRAY + "Click to toggle between Everything/Only Food & Blocks/Off", 1));
        inv.setItem(13, Utils.createItem(Material.IRON_PICKAXE, ChatColor.WHITE + "Breakable Blocks: " + (Objects.equals(KitPvP.getMultipleChoiceSetting("breakableblocks"), "1") ? "Blocks contained in kits" : (Objects.equals(KitPvP.getMultipleChoiceSetting("breakableblocks"), "2") ? "All" : "None")), ChatColor.GRAY + "Click to toggle between All/Blocks contained in kits/None", 1));
        inv.setItem(14, Utils.createItem(Material.SHIELD, ChatColor.WHITE + "Shields: " + (KitPvP.getSetting("enableshields") ? "On" : "Off"), ChatColor.GRAY + "Toggles if any kit could contain a shield", 1));
        inv.setItem(15, Utils.createItem(Material.SPYGLASS, ChatColor.WHITE + "Not-In-PvP-Players: " + (Objects.equals(KitPvP.getMultipleChoiceSetting("neutralplayers"), "1") ? "Survival" : (Objects.equals(KitPvP.getMultipleChoiceSetting("neutralplayers"), "2") ? "Disable Damage" : "Disable Damage & Enable Flying")), ChatColor.GRAY + "Click to toggle between Survival/Disable Damage/Disable Damage & Enable Flying", 1));
        //inv.setItem(16, Utils.createItem(Material.TNT, ChatColor.WHITE + "Creepers and TNT explodes: " + (KitPvP.getSetting("explodingentities") ? "On" : "Off"), ChatColor.GRAY + "Toggles if Creepers and TNT can destroy the map", 1));
        inv.setItem(16, Utils.createItem(Material.FILLED_MAP, ChatColor.WHITE + "Map Reset: " + (Objects.equals(KitPvP.getMultipleChoiceSetting("mapreset"), "1") ? "Every 5 Minutes" : (Objects.equals(KitPvP.getMultipleChoiceSetting("mapreset"), "2") ? "Every 15 Minutes" : (Objects.equals(KitPvP.getMultipleChoiceSetting("mapreset"), "3") ? "Every 30 Minutes" : (Objects.equals(KitPvP.getMultipleChoiceSetting("mapreset"), "4") ? "Every Hour" : "Never")))), ChatColor.GRAY + "Click to toggle between Every 5/15/30 Minutes/Every Hour/Never\nYou can set the template map in the config file.", 1));
        inv.setItem(22, Utils.createItem(Material.ARROW, ChatColor.WHITE + "Go Back", ChatColor.GRAY + "To Home", 1));
        inv.setItem(23, Utils.createItem(Material.BARREL, ChatColor.WHITE + "Restock All Kits", ChatColor.GRAY + "Restocks the current kit of all players", 1));
        player.openInventory(inv);
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
                toggleNextMultipleChoiceSetting("defaultkillbonus", player, 3);
                break;
            case 12:
                toggleNextMultipleChoiceSetting("restockonkill", player, 3);
                break;
            case 13:
                toggleNextMultipleChoiceSetting("breakableblocks", player, 3);
                break;
            case 14:
                toggleNextSetting("enableshields", player);
                break;
            case 15:
                toggleNextMultipleChoiceSetting("neutralplayers", player, 3);
                break;
            case 16:
                toggleNextMultipleChoiceSetting("mapreset", player, 5);
                break;
            case 23:
                KitPvP.restockAllKits();
                player.closeInventory();
                player.sendMessage(ChatColor.GREEN + "Restocked all kits!");
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
    public static void toggleNextMultipleChoiceSetting(String setting, Player executor, int maxOptionValues) {
        if (!executor.isOp()) {
            executor.sendMessage(ChatColor.RED + "You are not allowed to change settings!");
            executor.closeInventory();
            return;
        }
        System.out.println("Toggling " + setting);
        int currentValue = 0;
        try {
            currentValue = Integer.parseInt(KitPvP.getMultipleChoiceSetting(setting));
        } catch (NumberFormatException ignored) {}

        if (currentValue >= maxOptionValues) currentValue = 1;
        else currentValue++;

        KitPvP.setMultipleChoiceSetting(setting, String.valueOf(currentValue));
        //Refresh
        KitPvP.refreshSettings();
        openSettingsInterface(executor);
    }

    public static void openHotbarEditor(Player player, Kit kit) {
        Inventory inv = Bukkit.createInventory(null, 36,  ChatColor.BLUE + "Hotbar Editor: " + kit.getColoredName());
        inv.setItem(0, Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, ChatColor.GREEN + "Inventory:", ChatColor.GRAY + "Here you put items that\n shouldn't be in you hotbar.", 1));
        for (int i = 9; i < 18; i++)
            inv.setItem(i, Utils.createItem(Material.CYAN_STAINED_GLASS_PANE, " ", null, 1));
        for (int i = 27; i < 36; i++)
            inv.setItem(i, Utils.createItem(Material.CYAN_STAINED_GLASS_PANE, " ", null, 1));
        //inv.setItem(30, Utils.createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "Reset Hotbar", null, 1));
        inv.setItem(31, Utils.createItem(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "Save Hotbar", null, 1));
        //inv.setItem(32, Utils.createItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.AQUA + "Save Hotbar & Equip Kit", null, 1));

        for (int i = 0; i < 9; i++) {
            if (kit.getContents().getInventory()[i] == null) {
                inv.setItem(i + 18, new ItemStack(Material.AIR));
                continue;
            }
            ItemStack item = kit.getContents().getInventory()[i].clone();
            item.setAmount(1);
            inv.setItem(i + 18, item);
        }

        //IDEA: wenn gespeichert wird, ursprüngliches item im slot anschauen und slot type merken und dann slottype zum slot setzen, wo item nun ist
        //TODO: Save per player
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
                item = item == null ? new ItemStack(Material.AIR) : item;
                Material material = item.getType();
                if (kit.getContents().getInventory()[i] != null) item.setAmount(kit.getContents().getInventory()[i].getAmount());
                if (!hotbarItems.containsKey(material)) {
                    kit.getContents().getInventory()[i] = null;
                    kit.getContents().getHotbarItemOrder().setSlot(i, HotbarItemOrder.Type.NONE);
                    continue;
                }
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

    public static void handleKitClickAction(Player player, Kit kit, Player executor, boolean isRightClick) {
        if (isRightClick) openHotbarEditor(player, kit);
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
        if (executor == null || player != executor || !KitPvP.isInPVP(player)) {
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
