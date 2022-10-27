package ml.enoughsdv.region.listeners;

import ml.enoughsdv.region.utils.CountdownUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDisconnectListener implements Listener {
    
    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        CountdownUtil.getCountdownMap().remove(event.getPlayer().getUniqueId());
    }

}
