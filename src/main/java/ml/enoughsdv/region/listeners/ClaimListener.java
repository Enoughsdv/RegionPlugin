package ml.enoughsdv.region.listeners;

import ml.enoughsdv.region.RegionPlugin;
import ml.enoughsdv.region.claim.ClaimHandler;
import ml.enoughsdv.region.utils.LocationUtil;
import ml.enoughsdv.region.utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.event.block.Action.LEFT_CLICK_BLOCK;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

public class ClaimListener implements Listener {

    private final RegionPlugin plugin;
    private final ClaimHandler claimHandler;

    public ClaimListener(@NotNull RegionPlugin plugin, @NotNull ClaimHandler claimHandler) {
        this.plugin = plugin;
        this.claimHandler = claimHandler;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        claimHandler.getClaimHandlerMap().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (isClaimingWand(event.getPlayer().getItemInHand())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (!player.hasPermission("region.create")) {
            return;
        }

        if (action == Action.PHYSICAL || !event.hasItem() || !isClaimingWand(event.getItem())) {
            return;
        }

        if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {

            Block block = event.getClickedBlock();
            Location blockLocation = block.getLocation();

            if (action == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
            }

            switch (action) {
                case LEFT_CLICK_BLOCK:
                    claimHandler.getClaimHandlerMap().get(player.getUniqueId()).setPosition1(blockLocation);
                    player.sendMessage(MessageUtil.translate(plugin
                            .getConfig().getString("messages.region.claim.position.one").
                            replace("%location%", LocationUtil.parseToString(blockLocation))));
                    break;
                case RIGHT_CLICK_BLOCK:
                    claimHandler.getClaimHandlerMap().get(player.getUniqueId()).setPosition2(blockLocation);
                    player.sendMessage(MessageUtil.translate(plugin
                            .getConfig().getString("messages.region.claim.position.two")
                            .replace("%location%", LocationUtil.parseToString(blockLocation))));
                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        if (isClaimingWand(item.getItemStack())) {
            item.remove();
            claimHandler.getClaimHandlerMap().remove(event.getPlayer().getUniqueId());

            event.getPlayer().sendMessage(MessageUtil.translate(plugin
                    .getConfig().getString("messages.region.claim.cancelled")));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickup(PlayerPickupItemEvent event) {
        Item item = event.getItem();
        if (isClaimingWand(item.getItemStack())) {
            item.remove();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getDrops().remove(plugin.getClaimHandler().getClaimWand());
        if (!claimHandler.getClaimHandlerMap().containsKey(event.getEntity().getUniqueId())) {
            return;
        }

        event.getEntity().sendMessage(MessageUtil.translate(plugin
                .getConfig().getString("messages.region.claim.cancelled")));

        claimHandler.getClaimHandlerMap().remove(event.getEntity().getUniqueId());
    }

    @NotNull
    private boolean isClaimingWand(@NotNull ItemStack stack) {
        return stack.isSimilar(plugin.getClaimHandler().getClaimWand());
    }

}
