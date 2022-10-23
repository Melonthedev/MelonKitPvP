package wtf.melonthedev.melonkitpvp;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitContents {

    private ItemStack[] armor = new ItemStack[4];
    private ItemStack[] inventory = new ItemStack[36];
    private ItemStack offhand;
    private HotbarItemOrder hotbarItemOrder;

    public KitContents(HotbarItemOrder hotbarItemOrder) {
        this.hotbarItemOrder = hotbarItemOrder;
    }

    public KitContents addItem(int slot, Material material, EnchantmentOffer... enchantments) {
        return addItem(slot, material, null, enchantments);
    }
    public KitContents addItem(int slot, Material material, String name, EnchantmentOffer... enchantments) {
        return addItem(slot, material, name, 1, enchantments);
    }
    public KitContents addItem(int slot, Material material, int amount, EnchantmentOffer... enchantments) {
        return addItem(slot, material, null, amount, enchantments);
    }
    public KitContents addItem(int slot, Material material, String name, int amount, EnchantmentOffer... enchantments) {
        ItemStack item = new ItemStack(material, amount);
        for (EnchantmentOffer enchantment : enchantments)
            item.addUnsafeEnchantment(enchantment.getEnchantment(), enchantment.getEnchantmentLevel());
        if (name != null && item.getItemMeta() != null)
            item.getItemMeta().setDisplayName(name);
        inventory[slot] = item;
        return this;
    }

    public KitContents addArmor(KitContents.KitArmor armor, EnchantmentOffer... enchantments) {
        switch (armor) {
            case LEATHER:
                this.armor[0] = addSupportedEnchantments(new ItemStack(Material.LEATHER_BOOTS), enchantments);
                this.armor[1] = addSupportedEnchantments(new ItemStack(Material.LEATHER_LEGGINGS), enchantments);
                this.armor[2] = addSupportedEnchantments(new ItemStack(Material.LEATHER_CHESTPLATE), enchantments);
                this.armor[3] = addSupportedEnchantments(new ItemStack(Material.LEATHER_HELMET), enchantments);
                break;
            case CHAINMAIL:
                this.armor[0] = addSupportedEnchantments(new ItemStack(Material.CHAINMAIL_BOOTS), enchantments);
                this.armor[1] = addSupportedEnchantments(new ItemStack(Material.CHAINMAIL_LEGGINGS), enchantments);
                this.armor[2] = addSupportedEnchantments(new ItemStack(Material.CHAINMAIL_CHESTPLATE), enchantments);
                this.armor[3] = addSupportedEnchantments(new ItemStack(Material.CHAINMAIL_HELMET), enchantments);
                break;
            case IRON:
                this.armor[0] = addSupportedEnchantments(new ItemStack(Material.IRON_BOOTS), enchantments);
                this.armor[1] = addSupportedEnchantments(new ItemStack(Material.IRON_LEGGINGS), enchantments);
                this.armor[2] = addSupportedEnchantments(new ItemStack(Material.IRON_CHESTPLATE), enchantments);
                this.armor[3] = addSupportedEnchantments(new ItemStack(Material.IRON_HELMET), enchantments);
                break;
            case GOLD:
                this.armor[0] = addSupportedEnchantments(new ItemStack(Material.GOLDEN_BOOTS), enchantments);
                this.armor[1] = addSupportedEnchantments(new ItemStack(Material.GOLDEN_LEGGINGS), enchantments);
                this.armor[2] = addSupportedEnchantments(new ItemStack(Material.GOLDEN_CHESTPLATE), enchantments);
                this.armor[3] = addSupportedEnchantments(new ItemStack(Material.GOLDEN_HELMET), enchantments);
                break;
            case DIAMOND:
                this.armor[0] = addSupportedEnchantments(new ItemStack(Material.DIAMOND_BOOTS), enchantments);
                this.armor[1] = addSupportedEnchantments(new ItemStack(Material.DIAMOND_LEGGINGS), enchantments);
                this.armor[2] = addSupportedEnchantments(new ItemStack(Material.DIAMOND_CHESTPLATE), enchantments);
                this.armor[3] = addSupportedEnchantments(new ItemStack(Material.DIAMOND_HELMET), enchantments);
                break;
            case NETHERITE:
                this.armor[0] = addSupportedEnchantments(new ItemStack(Material.NETHERITE_BOOTS), enchantments);
                this.armor[1] = addSupportedEnchantments(new ItemStack(Material.NETHERITE_LEGGINGS), enchantments);
                this.armor[2] = addSupportedEnchantments(new ItemStack(Material.NETHERITE_CHESTPLATE), enchantments);
                this.armor[3] = addSupportedEnchantments(new ItemStack(Material.NETHERITE_HELMET), enchantments);
                break;
        }
        return this;
    }

    public KitContents addCustomArmorPice(int slot, ItemStack stack) {
        armor[slot] = stack;
        return this;
    }

    public KitContents addShield(EnchantmentOffer... enchantments) {
        offhand = addSupportedEnchantments(new ItemStack(Material.SHIELD), enchantments);
        return this;
    }

    public KitContents addOffHandItem(ItemStack stack, EnchantmentOffer... enchantments) {
        offhand = addSupportedEnchantments(stack, enchantments);
        return this;
    }

    public KitContents addRod(EnchantmentOffer... enchantments) {
        inventory[hotbarItemOrder.getSlot(HotbarItemOrder.Type.ROD)] = addSupportedEnchantments(new ItemStack(Material.FISHING_ROD), enchantments);
        return this;
    }

    public KitContents addBow(EnchantmentOffer... enchantments) {
        inventory[hotbarItemOrder.getSlot(HotbarItemOrder.Type.BOW)] = addSupportedEnchantments(new ItemStack(Material.BOW), enchantments);//TODO
        return this;
    }

    public KitContents addArrow() {
        inventory[17] = new ItemStack(Material.ARROW);
        return this;
    }

    public KitContents addArrows(int amount) {
        inventory[hotbarItemOrder.getSlot(HotbarItemOrder.Type.ARROWS)] = new ItemStack(Material.ARROW, amount);
        return this;
    }

    public KitContents addBlocks(int amount) {
        inventory[hotbarItemOrder.getSlot(HotbarItemOrder.Type.BLOCKS)] = new ItemStack(Material.WHITE_CONCRETE, amount);
        return this;
    }

    public KitContents addBlocks(Material material, int amount) {
        inventory[hotbarItemOrder.getSlot(HotbarItemOrder.Type.BLOCKS)] = new ItemStack(material, amount);
        return this;
    }

    public KitContents addWaterBucket() {
        inventory[hotbarItemOrder.getSlot(HotbarItemOrder.Type.BUCKET)] = new ItemStack(Material.WATER_BUCKET);
        return this;
    }

    public KitContents addLavaBucket() {
        if (inventory[hotbarItemOrder.getSlot(HotbarItemOrder.Type.BUCKET)] == null)
            inventory[hotbarItemOrder.getSlot(HotbarItemOrder.Type.BUCKET)] = new ItemStack(Material.LAVA_BUCKET);
        else inventory[hotbarItemOrder.getSlot(HotbarItemOrder.Type.OTHERS)] = new ItemStack(Material.LAVA_BUCKET);
        return this;
    }

    public KitContents addFood(int amount) {
        inventory[hotbarItemOrder.getSlot(HotbarItemOrder.Type.FOOD)] = new ItemStack(Material.COOKED_BEEF, amount);
        return this;
    }

    public KitContents addGoldenCarrots(int amount) {
        inventory[hotbarItemOrder.getSlot(HotbarItemOrder.Type.FOOD)] = new ItemStack(Material.GOLDEN_CARROT, amount);
        return this;
    }

    public KitContents addGoldenApples(int amount) {
        inventory[hotbarItemOrder.getSlot(HotbarItemOrder.Type.GAPPELS)] = new ItemStack(Material.GOLDEN_APPLE, amount);
        return this;
    }

    public KitContents addFood(Material material, int amount) {
        inventory[hotbarItemOrder.getSlot(HotbarItemOrder.Type.FOOD)] = new ItemStack(material, amount);
        return this;
    }

    public KitContents addSword(Material material, EnchantmentOffer... enchantments) {
        inventory[hotbarItemOrder.getSlot(HotbarItemOrder.Type.SWORD)] = addSupportedEnchantments(new ItemStack(material), enchantments);
        return this;
    }

    public KitContents addTools(EnchantmentOffer... enchantments) {
        inventory[9] = addSupportedEnchantments(new ItemStack(Material.STONE_PICKAXE), enchantments);
        inventory[10] = addSupportedEnchantments(new ItemStack(Material.STONE_SHOVEL), enchantments);
        inventory[11] = addSupportedEnchantments(new ItemStack(Material.STONE_AXE), enchantments);
        return this;
    }

    public KitContents addDefenderSpawnEgg(EntityType type, Material item, int amount, int health) {
        ItemStack stack = new ItemStack(item, amount);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Defender Spawn Egg");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Spawns a " + type.name() + " with " + health + " hp."));
        stack.setItemMeta(meta);
        inventory[hotbarItemOrder.getSlot(HotbarItemOrder.Type.OTHERS)] = stack;
        return this;
    }

    public KitContents addDefenderSpawnEgg(EntityType type, Material item, int amount) {
        return addDefenderSpawnEgg(type, item,  amount, 20);
    }

    public KitContents addCobwebs(int amount) {
        inventory[hotbarItemOrder.getSlot(HotbarItemOrder.Type.COBWEB)] = new ItemStack(Material.COBWEB, amount);
        return this;
    }

    public KitContents build() {
        return this;
    }


    public List<ItemStack> getAll() {
        List<ItemStack> items = new ArrayList<>();
        items.addAll(Arrays.asList(armor));
        items.addAll(Arrays.asList(inventory));
        items.add(offhand);
        return items;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public ItemStack getOffhand() {
        return offhand;
    }

    private ItemStack addSupportedEnchantments(ItemStack item, EnchantmentOffer... enchantments) {
        for (EnchantmentOffer enchantment : enchantments)
            item.addEnchantment(enchantment.getEnchantment(), enchantment.getEnchantmentLevel());
        return item;
    }

    public HotbarItemOrder getHotbarItemOrder() {
        return hotbarItemOrder;
    }

    public enum KitArmor {
        LEATHER, CHAINMAIL, IRON, GOLD, DIAMOND, NETHERITE
    }

}
