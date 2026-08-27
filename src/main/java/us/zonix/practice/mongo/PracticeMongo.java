package us.zonix.practice.mongo;

import org.bukkit.configuration.file.FileConfiguration;
import java.util.Collections;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import com.mongodb.MongoClientURI;
import us.zonix.practice.Practice;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;

public class PracticeMongo
{
    private static PracticeMongo instance;
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> players;

    public PracticeMongo() {
        if (PracticeMongo.instance != null) {
            throw new RuntimeException("The mongo database has already been instantiated.");
        }
        PracticeMongo.instance = this;
        final FileConfiguration config = (FileConfiguration)Practice.getInstance().getMainConfig().getConfiguration();
        final String connectionString = config.contains("mongo.connection-string") ? trimToNull(config.getString("mongo.connection-string")) : null;
        String databaseName = config.getString("mongo.database", "practice");
        if (connectionString != null) {
            final MongoClientURI uri = new MongoClientURI(connectionString);
            this.client = new MongoClient(uri);
            if (uri.getDatabase() != null) {
                databaseName = uri.getDatabase();
            }
        }
        else {
            if (!config.contains("mongo.host") || !config.contains("mongo.port") || !config.contains("mongo.database")) {
                throw new RuntimeException("Missing configuration option: set either mongo.connection-string or mongo.host/mongo.port");
            }
            if (config.getBoolean("mongo.authentication.enabled")) {
                if (!config.contains("mongo.authentication.username") || !config.contains("mongo.authentication.password") || !config.contains("mongo.authentication.database")) {
                    throw new RuntimeException("Missing configuration option: mongo.authentication.*");
                }
                final MongoCredential credential = MongoCredential.createCredential(config.getString("mongo.authentication.username"), config.getString("mongo.authentication.database"), config.getString("mongo.authentication.password").toCharArray());
                this.client = new MongoClient(new ServerAddress(config.getString("mongo.host"), config.getInt("mongo.port")), Collections.singletonList(credential));
            }
            else {
                this.client = new MongoClient(new ServerAddress(config.getString("mongo.host"), config.getInt("mongo.port")));
            }
        }
        this.database = this.client.getDatabase(databaseName);
        this.players = this.database.getCollection("players");
    }

    private static String trimToNull(final String s) {
        if (s == null) {
            return null;
        }
        final String t = s.trim();
        return (t.isEmpty() || t.equalsIgnoreCase("none")) ? null : t;
    }

    public MongoClient getClient() {
        return this.client;
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

    public MongoCollection<Document> getPlayers() {
        return this.players;
    }

    public static PracticeMongo getInstance() {
        return PracticeMongo.instance;
    }
}
