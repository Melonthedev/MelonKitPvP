package wtf.melonthedev.melonkitpvp;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum Kit {
    STANDARD("Standard", "Kit contents:\n- Iron Sword\n- Full Leather\n- 64x Stone\n- 8x Golden Carrot", Material.LEATHER_CHESTPLATE, ChatColor.DARK_AQUA, new KitContents(new HotbarItemOrder(HotbarItemOrder.Type.SWORD, HotbarItemOrder.Type.BLOCKS, HotbarItemOrder.Type.FOOD))
            .addArmor(KitContents.KitArmor.LEATHER)
            .addSword(Material.IRON_SWORD)
            .addBlocks(Material.LIGHT_BLUE_CONCRETE, 64)
            .addGoldenCarrots(8)
            .addTools()
            .build()),
    PRO("Pro", "Kit contents:\n- Iron Sword\n- Full Iron\n- 64x Stone\n- 1x Water Bucket\n- Fishing Rod\n- 16x Golden Carrot", Material.IRON_SWORD, ChatColor.AQUA, new KitContents(new HotbarItemOrder(HotbarItemOrder.Type.SWORD, HotbarItemOrder.Type.BLOCKS, HotbarItemOrder.Type.FOOD).setSlot9(HotbarItemOrder.Type.BUCKET))
            .addArmor(KitContents.KitArmor.IRON)
            .addSword(Material.IRON_SWORD)
            .addBlocks(Material.CYAN_CONCRETE, 64)
            .addGoldenCarrots(16)
            .addWaterBucket()
            .addTools()
            .build()),
    ULTRA("Ultra", "Kit contents:\n- Iron Sword\n- Full Iron\n- 64x Stone\n- 1x Water Bucket\n- Fishing Rod\n- 16x Golden Carrot\n- Shield\n- 16x Cobweb", Material.COBWEB, ChatColor.LIGHT_PURPLE, new KitContents(new HotbarItemOrder(HotbarItemOrder.Type.SWORD, HotbarItemOrder.Type.ROD, HotbarItemOrder.Type.BLOCKS, HotbarItemOrder.Type.FOOD, HotbarItemOrder.Type.COBWEB).setSlot9(HotbarItemOrder.Type.BUCKET))
            .addArmor(KitContents.KitArmor.IRON)
            .addSword(Material.IRON_SWORD)
            .addBlocks(Material.PINK_CONCRETE, 64)
            .addCobwebs(16)
            .addGoldenCarrots(16)
            .addWaterBucket()
            .addRod()
            .addTools()
            .build()),
    EPIC("Epic", "Kit contents:\n- All Ultra-Items\n- Iron Sword + Sharpness I\n- 1x Bow\n- 16x Arrows", Utils.createItem(Material.IRON_SWORD, ChatColor.DARK_PURPLE + "Epic", null, 1, Enchantment.DAMAGE_ALL, 1), ChatColor.DARK_PURPLE, new KitContents(new HotbarItemOrder(HotbarItemOrder.Type.SWORD, HotbarItemOrder.Type.BLOCKS, HotbarItemOrder.Type.BOW, HotbarItemOrder.Type.ROD, HotbarItemOrder.Type.FOOD, HotbarItemOrder.Type.COBWEB, HotbarItemOrder.Type.NONE, HotbarItemOrder.Type.ARROWS, HotbarItemOrder.Type.BUCKET))
            .addArmor(KitContents.KitArmor.IRON)
            .addSword(Material.IRON_SWORD, new EnchantmentOffer(Enchantment.DAMAGE_ALL, 1, 0))
            .addBlocks(Material.PURPLE_CONCRETE, 64)
            .addCobwebs(16)
            .addGoldenCarrots(16)
            .addRod()
            .addBow()
            .addShield()
            .addArrows(16)
            .addWaterBucket()
            .addTools()
            .build()),
    SNIPER("Sniper", "Kit contents:\n- All Ultra-Items\n- 1x Bow + Infinity\n- Arrow\n- 1x Skeleton Spawn egg", Utils.createItem(Material.BOW, ChatColor.GREEN + "Sniper", null, 1, Enchantment.ARROW_INFINITE, 1), ChatColor.GREEN, new KitContents(new HotbarItemOrder(HotbarItemOrder.Type.SWORD, HotbarItemOrder.Type.BLOCKS, HotbarItemOrder.Type.BOW, HotbarItemOrder.Type.ROD, HotbarItemOrder.Type.FOOD, HotbarItemOrder.Type.OTHERS).setSlot9(HotbarItemOrder.Type.BUCKET))
            .addArmor(KitContents.KitArmor.IRON)
            .addSword(Material.IRON_SWORD)
            .addBlocks(Material.LIME_CONCRETE, 64)
            .addBow(new EnchantmentOffer(Enchantment.ARROW_INFINITE, 1, 0))
            .addGoldenCarrots(16)
            .addDefenderSpawnEgg(EntityType.SKELETON, Material.SKELETON_SPAWN_EGG, 5, 20)
            .addArrow()
            .addRod()
            .addShield()
            .addWaterBucket()
            .addTools()
            .build()),
    OP("Op", "Kit contents:\n- Diamond Sword\n- 32x Stone", Material.DIAMOND_SWORD, ChatColor.RED, new KitContents(new HotbarItemOrder(HotbarItemOrder.Type.SWORD, HotbarItemOrder.Type.BLOCKS, HotbarItemOrder.Type.COBWEB).setSlot9(HotbarItemOrder.Type.BUCKET))
            .addArmor(KitContents.KitArmor.LEATHER)
            .addSword(Material.DIAMOND_SWORD)
            .addBlocks(Material.RED_CONCRETE, 64)
            .addCobwebs(8)
            .addTools()
            .addWaterBucket()
            .build()),
    ENDERMAN("Enderman", "Kit contents:\n- All Pro-Items\n- 8x Ender Pearl", Material.ENDER_PEARL, ChatColor.DARK_BLUE, new KitContents(new HotbarItemOrder(HotbarItemOrder.Type.SWORD, HotbarItemOrder.Type.BLOCKS, HotbarItemOrder.Type.ROD, HotbarItemOrder.Type.OTHERS, HotbarItemOrder.Type.FOOD).setSlot9(HotbarItemOrder.Type.BUCKET))
            .addArmor(KitContents.KitArmor.IRON)
            .addSword(Material.IRON_SWORD)
            .addBlocks(Material.BLUE_CONCRETE, 64)
            .addGoldenCarrots(16)
            .addItem(4, Material.ENDER_PEARL, 4)
            .addWaterBucket()
            .addRod()
            .addTools()
            .build()),
    TANK("Tank", "Kit contents:\n- All Standard-Items\n- Full Diamond", Material.DIAMOND_CHESTPLATE, ChatColor.BLUE, new KitContents(new HotbarItemOrder(HotbarItemOrder.Type.SWORD, HotbarItemOrder.Type.BLOCKS, HotbarItemOrder.Type.FOOD))
            .addArmor(KitContents.KitArmor.DIAMOND)
            .addSword(Material.IRON_SWORD)
            .addBlocks(Material.BROWN_CONCRETE, 64)
            .addGoldenCarrots(8)
            .addTools()
            .build());

    ChatColor color;
    String name;
    String description;
    KitContents contents;
    ItemStack icon;
    //HotbarItemOrder hotbarItemOrder;

    Kit(String name, String description, Material icon, ChatColor color, KitContents contents) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.contents = contents;
        this.icon = Utils.createItem(icon, color + name, ChatColor.GRAY + description + ChatColor.GRAY + "\n\nRightclick to edit hotbar", 1);
    }

    Kit(String name, String description, ItemStack icon, ChatColor color, KitContents contents) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.contents = contents;
        if (!icon.hasItemMeta() || !icon.getItemMeta().hasLore() || icon.getItemMeta().getLore().isEmpty())
            this.icon = Utils.addLore(icon, ChatColor.GRAY + description + ChatColor.GRAY + "\n\nRightclick to edit hotbar");
        else this.icon = icon;
    }

    public void loadKitInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setContents(contents.getInventory());
        player.getInventory().setArmorContents(contents.getArmor());
        player.getInventory().setItemInOffHand(contents.getOffhand());
    }


    public ChatColor getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getColoredName() {
        return color + name;
    }

    public String getDescription() {
        return description;
    }

    public KitContents getContents() {
        return contents;
    }

    public ItemStack getIcon() {
        return icon;
    }
}
