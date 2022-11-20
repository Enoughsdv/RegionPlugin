package ml.enoughsdv.region.region;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.Getter;
import ml.enoughsdv.region.RegionPlugin;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RegionHandler {

    @Getter
    private final Map<String, Region> regionMap;
    private final RegionPlugin plugin;

    public RegionHandler(@NotNull RegionPlugin plugin) {
        this.plugin = plugin;
        this.regionMap = new HashMap<>();

        load();
    }

    private void load() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getMongoHandler().getRegions().find().forEach((Consumer<? super Document>) document
                    -> new Region(document.getString("_id")));
        });
    }

    @NotNull
    public Collection<Region> getRegions() {
        return regionMap.values();
    }

    @Nullable
    public Region getRegion(@NotNull String name) {
        return regionMap.get(name);
    }

}
