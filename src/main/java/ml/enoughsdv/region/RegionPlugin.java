package ml.enoughsdv.region;

import lombok.Getter;
import me.vaperion.blade.Blade;
import me.vaperion.blade.bukkit.BladeBukkitPlatform;
import ml.enoughsdv.region.claim.ClaimHandler;
import ml.enoughsdv.region.commands.RegionCommands;
import ml.enoughsdv.region.commands.arguments.RegionArgument;
import ml.enoughsdv.region.database.MongoHandler;
import ml.enoughsdv.region.listeners.ClaimListener;
import ml.enoughsdv.region.listeners.PlayerChatListener;
import ml.enoughsdv.region.listeners.PlayerDisconnectListener;
import ml.enoughsdv.region.listeners.PlayerInteractListener;
import ml.enoughsdv.region.menu.InventoryManager;
import ml.enoughsdv.region.region.Region;
import ml.enoughsdv.region.region.RegionHandler;
import ml.enoughsdv.region.utils.MessageUtil;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class RegionPlugin extends JavaPlugin {

    private InventoryManager inventoryManager;

    private MongoHandler mongoHandler;
    private RegionHandler regionHandler;
    private ClaimHandler claimHandler;

    @Override
    public void onLoad() {
        this.saveDefaultConfig();
        loadDependencies();
    }

    @Override
    public void onEnable() {
        loadManagers();
        loadCommands();
        loadListeners();
    }

    @Override
    public void onDisable() {
        this.regionHandler.getRegions().forEach(region -> region.save(false));
        this.mongoHandler.shutdown();
    }

    private void loadCommands() {
        Blade.forPlatform(new BladeBukkitPlatform(this))
                .config(config -> {
                    config.setFallbackPrefix("region");
                    config.setOverrideCommands(true);
                    config.setDefaultPermissionMessage(MessageUtil.translate(this.getConfig()
                            .getString("messages.general.no_permissions")));
                    config.setExecutionTimeWarningThreshold(10L);
                })
                .bind(binder -> {
                    binder.bind(Region.class, new RegionArgument(this));
                })
                .build()
                .register(new RegionCommands(this, getConfig()));
    }

    private void loadListeners() {
        registerListeners(new PlayerChatListener(), new PlayerDisconnectListener(),
                new ClaimListener(this, claimHandler), new PlayerInteractListener(this));
    }

    private void loadManagers() {
        this.mongoHandler = new MongoHandler();
        this.regionHandler = new RegionHandler(this);
        this.claimHandler = new ClaimHandler(this);

        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.init();
    }

    private void loadDependencies() {
        try {
            BukkitLibraryManager loader = new BukkitLibraryManager(this);
            loader.addMavenCentral();
            Library library = Library.builder()
                    .groupId("org.mongodb")
                    .artifactId("mongo-java-driver")
                    .version("3.12.10")
                    .build();
            loader.loadLibrary(library);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static RegionPlugin getInstance() {
        return getPlugin(RegionPlugin.class);
    }

    private void registerListeners(Listener... listener) {
        for (Listener listeners : listener) {
            this.getServer().getPluginManager().registerEvents(listeners, this);
        }
    }

}
