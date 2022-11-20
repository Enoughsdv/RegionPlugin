package ml.enoughsdv.region.region;

import com.mongodb.client.model.ReplaceOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import ml.enoughsdv.region.RegionPlugin;
import ml.enoughsdv.region.database.MongoHandler;
import ml.enoughsdv.region.utils.Cuboid;
import ml.enoughsdv.region.utils.LocationUtil;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

@Data
public class Region {

    private final String name;
    private String displayName;
    private Location positionOne, positionTwo;
    private List<UUID> players;

    private MongoHandler mongoHandler = RegionPlugin.getInstance().getMongoHandler();

    public Region(@NotNull String name) {
        this.name = name;
        this.players = new ArrayList<>();

        load();
        RegionPlugin.getInstance().getRegionHandler().getRegionMap().put(name, Region.this);
    }

    private void load() {
        Bukkit.getScheduler().runTaskAsynchronously(RegionPlugin.getInstance(), () -> {
            Document document = mongoHandler.getRegions().find(new Document("_id", name)).first();

            if (document == null) {
                return;
            }

            this.displayName = document.getString("displayName");
            this.positionOne = LocationUtil.parseToLocation(document.getString("position1"));
            this.positionTwo = LocationUtil.parseToLocation(document.getString("position2"));

            this.players = document.getList("players", UUID.class);
        });
    }

    public void save(@NotNull boolean async) {
        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(RegionPlugin.getInstance(), () -> save(false));
            return;
        }

        Document document = mongoHandler.getRegions().find(new Document("_id", name)).first();

        if (document == null) {
            mongoHandler.getRegions().insertOne(toBson());
            return;
        }

        mongoHandler.getRegions().replaceOne(document, toBson(), new ReplaceOptions().upsert(true));
    }

    public void delete(@NotNull boolean async) {

        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(RegionPlugin.getInstance(), () -> delete(false));
            return;
        }

        players.clear();
        mongoHandler.getRegions().deleteOne(new Document("_id", name));
        RegionPlugin.getInstance().getRegionHandler().getRegionMap().remove(name);
    }

    @NotNull
    public Cuboid getCuboid() {
        return new Cuboid(positionOne, positionTwo);
    }

    @NotNull
    public Document toBson() {
        return new Document("_id", name)
                .append("displayName", displayName)
                .append("position1", LocationUtil.parseToString(positionOne))
                .append("position2", LocationUtil.parseToString(positionTwo))
                .append("players", players);
    }

}
