package wtf.melonthedev.melonkitpvp;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Objects;

public class KitPvpListeners implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player deadPlayer = event.getEntity();
        Player killer = deadPlayer.getKiller();
        if (!KitPvP.isInPVP(deadPlayer)) return;
        event.setDeathMessage((killer == null ? ChatColor.RED + deadPlayer.getName() + ChatColor.GRAY + " died" : ChatColor.RED + deadPlayer.getName() + ChatColor.GRAY + " was killed by " + ChatColor.GREEN + killer.getName()));
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
            deadPlayer.getNearbyEntities(50, 10, 50).forEach(entity -> {
                if (entity.getType() == EntityType.DROPPED_ITEM) entity.remove();
            });
            if (Main.playerDefenders.containsKey(deadPlayer.getUniqueId())) {
                for (LivingEntity entity : Main.playerDefenders.get(deadPlayer.getUniqueId()))
                    entity.remove();
                Main.playerDefenders.remove(deadPlayer.getUniqueId());
            }
            KitPvP.setKillStreak(deadPlayer.getName(), 0);
            if (killer == null || !KitPvP.isInPVP(killer)) return;
            if (deadPlayer == killer || killer.isDead()) return;
            //Todo: add option to disable refresh
            killer.setFoodLevel(20);
            KitPvP.giveKit(killer); //Todo: add option to disable refresh
            killer.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "+1 Kill");
            KitPvP.addKills(killer.getName(), 1);
            KitPvP.addCoins(killer, 5); //TODO: Add Assist coins
            KitPvP.setKillStreak(killer.getName(), KitPvP.getKillStreak(killer.getName()) + 1);
            KitPvP.updateScoreboard();
            switch (KitPvP.getMultipleChoiceSetting("defaultkillbonus")) {
                case "1" -> killer.setHealth(20);
                case "2" -> killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
                case "3" -> {
                    killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
                    killer.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 1));
                }
            }
        }, 10);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (!KitPvP.isInPVP(event.getPlayer())) return;
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
            KitPvP.giveKit(event.getPlayer());
            event.getPlayer().setGameMode(GameMode.SURVIVAL);
        }, 10);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!KitPvP.isInPVP(player)) return;
        if (!(event.getDamager() instanceof Player)) return;
        Player damager = (Player) event.getDamager();
        if (!Main.hits.containsKey(player.getName())) Main.hits.put(player.getName(), 0);
        Main.hits.put(damager.getName(), Main.hits.get(damager.getName()) + 1);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!KitPvP.isInPVP(event.getPlayer())) {
            event.getPlayer().setAllowFlight(KitPvP.getMultipleChoiceSetting("neutralplayers").equals("3"));
            return;
        }
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
            KitPvP.enableKitPvp(event.getPlayer());
            KitPvP.updateScoreboard();
        }, 10);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!KitPvP.isInPVP(event.getPlayer())) return;
        Material brokenMaterial = event.getBlock().getType();
        if (brokenMaterial == Material.TALL_GRASS
                || brokenMaterial == Material.TORCH
                || brokenMaterial == Material.OBSIDIAN
                || brokenMaterial == Material.COBBLESTONE
                || brokenMaterial == Material.FIRE
                || brokenMaterial == Material.SOUL_FIRE
        ) return;
        switch (KitPvP.getMultipleChoiceSetting("breakableblocks")) {
            case "1" -> {
                for (Kit kit : Kit.values()) {
                    for (ItemStack stack : kit.getContents().getAll()) {
                        if (stack == null) continue;
                        if (stack.getType() == brokenMaterial)
                            return;
                    }
                }
            }
            case "2" -> {return;}
            case "3" -> {}
        }
        event.setCancelled(true);
        event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "You can't break this block in KitPvP!"));
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (!KitPvP.isInPVP(event.getPlayer())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!KitPvP.isInPVP(player)) return;
        for (Kit kit : Kit.values()) {
            for (ItemStack stack : kit.getContents().getAll()) {
                if (stack == null) continue;
                if (stack.getType() == event.getItem().getItemStack().getType())
                    return;
            }
        }
        player.getWorld().spawnParticle(Particle.ASH, event.getItem().getLocation(), 100, 0.1, 0.1, 0.1, 0.1);
        event.getItem().remove();
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        /*for (Entity nearbyEntity : event.getEntity().getNearbyEntities(7, 7, 7)) {
            if (!(nearbyEntity instanceof Player)) continue;
            Player player = (Player) nearbyEntity;
            if (KitPvP.isInPVP(player) && !KitPvP.getSetting("explodingentities")) event.blockList().clear();
            return;
        }
         */
        if (!KitPvP.getSetting("explodingentities")) event.blockList().clear();
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        if (!KitPvP.getSetting("explodingentities")) event.blockList().clear();
    }

    @EventHandler
    public void onFishing(PlayerFishEvent event) {
        if (!KitPvP.isInPVP(event.getPlayer())) return;
        if (event.getHook().getState() == FishHook.HookState.HOOKED_ENTITY
                && event.getHook().getHookedEntity() != null
                && (event.getHook().getHookedEntity() instanceof Player || event.getHook().getHookedEntity() instanceof ArmorStand)) {
            event.getHook().remove();
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getHitEntity() == null) return;
        if (event.getEntity().getType() != EntityType.FISHING_HOOK) return;
        if (!(event.getHitEntity() instanceof Player)) return;
        Player hooked = (Player) event.getHitEntity();
        if (!KitPvP.isInPVP(hooked)) return;
        hooked.damage(0.5);
        hooked.getLocation().add(0, 0.4, 0);
        hooked.setVelocity(hooked.getLocation().getDirection().multiply(-1));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!KitPvP.isInPVP(event.getPlayer())) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!event.hasItem()) return;
        if (event.getItem() == null) return;
        if (!event.getItem().hasItemMeta()) return;
        if (!event.getItem().getItemMeta().hasDisplayName()) return;
        if (!event.getItem().getItemMeta().hasLore()) return;
        if (!event.getItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Defender Spawn Egg")) return;
        Player player = event.getPlayer();
        String lore = ChatColor.stripColor(event.getItem().getItemMeta().getLore().get(0));
        if (!lore.startsWith("Spawns a ")) return;
        String mobName = lore.substring(9).split(" ")[0];
        EntityType type = EntityType.valueOf(mobName.toUpperCase());
        int health = Integer.parseInt(lore.substring(9 + mobName.length()).split(" ")[2]);
        event.setCancelled(true);
        LivingEntity defender = (LivingEntity) player.getWorld().spawnEntity(Objects.requireNonNull(event.getClickedBlock()).getLocation().add(0, 1, 0), type);
        Objects.requireNonNull(defender.getEquipment()).setHelmet(new ItemStack(Material.IRON_HELMET), true);
        defender.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        defender.setHealth(health);
        defender.setCustomNameVisible(true);
        defender.setCustomName(ChatColor.RED + player.getName() + "'s " + mobName.replaceAll("_", " "));
        player.getInventory().getItem(Objects.requireNonNull(event.getHand())).setAmount(player.getInventory().getItem(event.getHand()).getAmount() - 1);
        if (!Main.playerDefenders.containsKey(player.getUniqueId())) Main.playerDefenders.put(player.getUniqueId(), new ArrayList<>());
        Main.playerDefenders.get(player.getUniqueId()).add(defender);
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player)) return;
        Player target = (Player) event.getTarget();
        if (!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity defender = (LivingEntity) event.getEntity();
        if (!Main.playerDefenders.containsKey(target.getUniqueId())) return;
        if (Main.playerDefenders.get(target.getUniqueId()).contains(defender))
            event.setCancelled(true);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) return;
        if (event.getCurrentItem() == null) return;
        String title = event.getView().getTitle();
        String clearTitle = ChatColor.stripColor(title);
        int slot = event.getSlot();
        ItemStack currentItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (clearTitle.startsWith("KitPVP - Main Menu")) KitPvpInterfaces.handleMainMenuGuiClick(event, slot, currentItem, player);
        else if (clearTitle.startsWith("Select kit: ")) KitPvpInterfaces.handleSelectKitGuiClick(event, slot, currentItem, player, clearTitle);
        else if (clearTitle.startsWith("Change Kit to ")) KitPvpInterfaces.handleYesNoKitChangeGuiClick(event, slot, player, clearTitle);
        else if (clearTitle.startsWith("KitPvP Settings")) KitPvpInterfaces.handleSettingsGuiClick(event, slot, currentItem, player);
        else return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInvClickEditor(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        String clearTitle = ChatColor.stripColor(title);
        int slot = event.getSlot();
        ItemStack currentItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (clearTitle.startsWith("Hotbar Editor: "))
            KitPvpInterfaces.handleHotbarEditorClick(event, slot, currentItem, player, clearTitle);
    }

    @EventHandler
    public void onInvClickEditor(InventoryDragEvent event) {
        if (ChatColor.stripColor(event.getView().getTitle()).startsWith("Hotbar Editor: ")) event.setCancelled(true);

    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        //Main.inInventory.remove(event.getPlayer().getUniqueId());
        if (ChatColor.stripColor(event.getView().getTitle()).startsWith("Hotbar Editor: ")) {
            event.getPlayer().setItemOnCursor(null);
        }
    }


}
