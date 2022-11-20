package ml.enoughsdv.region;

import java.util.Locale;
import lombok.Getter;
import ml.enoughsdv.region.claim.ClaimHandler;
import ml.enoughsdv.region.commands.RegionsCommands;
import ml.enoughsdv.region.commands.arguments.RegionArgument;
import ml.enoughsdv.region.database.MongoHandler;
import ml.enoughsdv.region.listeners.ClaimListener;
import ml.enoughsdv.region.listeners.CountdownListener;
import ml.enoughsdv.region.listeners.PlayerInteractListener;
import ml.enoughsdv.region.menu.InventoryManager;
import ml.enoughsdv.region.region.Region;
import ml.enoughsdv.region.region.RegionHandler;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

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
        final BukkitCommandHandler commandHandler = BukkitCommandHandler.create(this);

        commandHandler.getTranslator().addResourceBundle("region");
        commandHandler.getTranslator().setLocale(new Locale("en"));

        commandHandler.setHelpWriter((command, actor) -> {
            if (!command.getPermission().canExecute(actor)) {
                return null;
            } else {
                return String.format("&7• &e/%s %s &7- %s",
                        command.getPath().toRealString(), command.getUsage(),
                        command.getDescription());
            }
        });

        commandHandler.registerDependency(FileConfiguration.class, this.getConfig());

        commandHandler.registerValueResolver(Region.class, new RegionArgument(this));
        commandHandler.getAutoCompleter()
                .registerParameterSuggestions(Region.class, new RegionArgument(this));

        commandHandler.register(new RegionsCommands(this));
    }

    private void loadListeners() {
        registerListeners(new CountdownListener(),
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
