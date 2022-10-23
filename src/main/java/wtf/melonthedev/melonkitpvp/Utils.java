package wtf.melonthedev.melonkitpvp;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Utils {

    public static ItemStack createItem(Material type, String displayName, String lore, int amount) {
        ItemStack item = new ItemStack(type, amount);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
        addLore(item, lore);
        return item;
    }

    public static ItemStack addLore(ItemStack item, String lore) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        if (lore != null) {
            String[] loreEntrys = lore.split("\n");
            String color = ChatColor.getLastColors(loreEntrys[0]);
            for (int i = 0; i < loreEntrys.length; i++) {
                loreEntrys[i] = color + loreEntrys[i];
            }
            List<String> itemLore = Arrays.asList(loreEntrys);
            meta.setLore(itemLore);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material type, String displayName, String lore, int amount, Enchantment enchantment, int level) {
        ItemStack item = createItem(type, displayName, lore, amount);
        item.addUnsafeEnchantment(enchantment, level);
        return item;
    }

    public static ItemStack createSkull(String owner, String displayName, String lore, int amount) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, amount);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(displayName);
        if (lore != null)
            meta.setLore(Collections.singletonList(lore));
        meta.setOwner(owner);
        item.setItemMeta(meta);
        return item;
    }
}
