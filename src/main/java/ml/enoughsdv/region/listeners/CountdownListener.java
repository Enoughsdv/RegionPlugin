package ml.enoughsdv.region.listeners;

import ml.enoughsdv.region.RegionPlugin;
import ml.enoughsdv.region.utils.CountdownUtil;
import ml.enoughsdv.region.utils.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CountdownListener implements Listener {

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        CountdownUtil.getCountdownMap().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!CountdownUtil.hasMapped(player)) {
            return;
        }

        CountdownUtil.getCountdownMap().values().forEach(region -> {
            region.setDisplayName(event.getMessage());
            region.save(false);
            CountdownUtil.end(player, region);
        });

        player.sendMessage(MessageUtil.translate(RegionPlugin.getInstance().getConfig()
                .getString("messages.region.rename.changed").replace("%message%", event.getMessage())));
        event.setCancelled(true);

    }

}
