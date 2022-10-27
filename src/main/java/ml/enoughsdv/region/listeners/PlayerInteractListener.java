package ml.enoughsdv.region.listeners;

import ml.enoughsdv.region.RegionPlugin;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private final RegionPlugin plugin;

    public PlayerInteractListener(RegionPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        denyInteract(event.getPlayer(), event, event.getClickedBlock());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        denyInteract(event.getPlayer(), event, event.getBlock());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockPlaceEvent event) {
        denyInteract(event.getPlayer(), event, event.getBlock());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onHangingPlace(HangingPlaceEvent event) {
        denyInteract(event.getPlayer(), event, event.getBlock().getRelative(event.getBlockFace()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            denyInteract((Player) entity, event, event.getBlock());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        denyInteract(event.getPlayer(), event, event.getBlockClicked().getRelative(event.getBlockFace()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBucketFill(PlayerBucketFillEvent event) {
        denyInteract(event.getPlayer(), event, event.getBlockClicked().getRelative(event.getBlockFace()));
    }

    private void denyInteract(Player player, Cancellable cancellable, Block block) {
        //if (player.hasPermission("region.bypass")) return;

        plugin.getRegionHandler().getRegions().stream()
                .filter(region -> !region.getPlayers().contains(player.getUniqueId()))
                .filter(region -> region.getCuboid() != null && block != null)
                .filter(region -> region.getCuboid().isIn(block.getLocation()))
                .forEach(region -> cancellable.setCancelled(true));
    }

}
