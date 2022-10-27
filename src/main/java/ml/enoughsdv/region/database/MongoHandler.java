package ml.enoughsdv.region.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Getter;
import ml.enoughsdv.region.RegionPlugin;

import org.bson.Document;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class MongoHandler {

    private final FileConfiguration config = RegionPlugin.getInstance().getConfig();

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private MongoCollection<Document> regions;

    public MongoHandler() {
        init();
    }

    private void init() {
        disableLogging();

        if (config.getBoolean("storage.mongodb.uri.enabled")) {
            this.mongoClient = MongoClients.create(config.getString("storage.mongodb.uri.connection"));
            this.mongoDatabase = mongoClient.getDatabase(config.getString("storage.mongodb.uri.database"));
            this.regions = mongoDatabase.getCollection("regions");
            return;
        }

        String host = config.getString("storage.mongodb.normal.host");
        int port = config.getInt("storage.mongodb.normal.port");

        String connection = "mongodb://" + host + ":" + port;

        if (config.getBoolean("storage.mongodb.normal.auth.enabled")) {
            String username = config.getString("storage.mongodb.normal.auth.username");
            String password = config.getString("storage.mongodb.normal.auth.password");
            connection = "mongodb://" + username + ":" + password + "@" + host + ":" + port;
        }

        this.mongoClient = MongoClients.create(connection);

        this.regions = mongoDatabase.getCollection("regions");

    }

    public void shutdown() {
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (this.mongoClient != null) {
            this.mongoClient.close();
        }
    }

    public void disableLogging() {
        Logger mongoLogger = Logger.getLogger("com.mongodb");
        mongoLogger.setLevel(Level.SEVERE);

        Logger legacyLogger = Logger.getLogger("org.mongodb");
        legacyLogger.setLevel(Level.SEVERE);
    }

}
