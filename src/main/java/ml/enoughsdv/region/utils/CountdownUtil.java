package ml.enoughsdv.region.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import ml.enoughsdv.region.RegionPlugin;
import ml.enoughsdv.region.menu.SmartInventory;
import ml.enoughsdv.region.menus.RegionMenu;
import ml.enoughsdv.region.region.Region;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class CountdownUtil {

    private final Map<UUID, Region> countdownMap = new HashMap<>();
    private final FileConfiguration config = RegionPlugin.getInstance().getConfig();

    public void start(@NotNull Player player,
            @NotNull SmartInventory inventory,
            @NotNull Region region) {

        int seconds = config.getInt("messages.region.rename.seconds");
        config.getStringList("messages.region.rename.message")
                .stream().map(MessageUtil::translate).forEach(list
                -> player.sendMessage(list.replace("%seconds%", seconds + "")));
        Bukkit.getScheduler().runTaskLaterAsynchronously(RegionPlugin.getInstance(), () -> {
            if (!hasMapped(player)) {
                return;
            }

            end(player, region);
        }, 20L * seconds);

        player.closeInventory();
        countdownMap.put(player.getUniqueId(), region);
    }

    public void end(@NotNull Player player, @NotNull Region region) {
        Bukkit.getScheduler().runTask(RegionPlugin.getInstance(), () -> {
            countdownMap.remove(player.getUniqueId());
            new RegionMenu(region).getInventory().open(player);
        });
    }

    @NotNull
    public boolean hasMapped(@NotNull Player player) {
        return countdownMap.containsKey(player.getUniqueId());
    }

    @NotNull
    public Map<UUID, Region> getCountdownMap() {
        return countdownMap;
    }

}
