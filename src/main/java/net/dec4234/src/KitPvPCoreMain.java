package net.dec4234.src;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import net.dec4234.commands.*;
import net.dec4234.files.YmlConfig;
import net.dec4234.guis.content.boosters.BoosterHandling;
import net.dec4234.guis.framework.GUIListener;
import net.dec4234.listeners.*;
import net.dec4234.listeners.placeholders.PlaceholderManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jefferies.core.sCore;
import org.jefferies.core.utils.config.Configuration;
import org.jefferies.core.utils.mongo.MongoConnection;

public class KitPvPCoreMain extends JavaPlugin {

	private static KitPvPCoreMain instance;
	private static MongoClient mc = null;
	private static MongoConnection mongoConnection;
	private static Configuration data;

	@Override
	public void onEnable() {
		instance = this;
		register();
	}

	@Override
	public void onDisable() {
		BoosterHandling.deactivateAll();
		CustomItemsListener.despawnAll();

		Bukkit.getScheduler().cancelTasks(this);
	}

	/**
	 * Used to register commands, events, runnables, etc.
	 */
	private void register() {
		new YmlConfig();
		data = sCore.getInstance().getData();

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerJoinListener(), this);
		pm.registerEvents(new PlayerDeathListener(), this);
		pm.registerEvents(new PlayerRespawnListener(), this);
		pm.registerEvents(new PlayerLevelupListener(), this);
		pm.registerEvents(new EnchantmentDragAndDropListener(), this);
		pm.registerEvents(new GUIListener(), this);
		pm.registerEvents(new ArrowHitListener(), this);
		pm.registerEvents(new CustomItemsListener(), this);

		getCommand("adminbooster").setExecutor(new AdminBoostersCommand());
		getCommand("adminitems").setExecutor(new AdminItemsCommand());

		new BountyCommand();
		new UpgradesCommand();
		new BoostersCommand();

		// If PlaceholderAPI is installed
		if(Bukkit.getPluginManager().getPlugin("PlaceHolderAPI") != null) {
			new PlaceholderManager();
		}
	}

	public static KitPvPCoreMain getInstance() {
		return instance;
	}

	public static MongoClient getMongoClient() {
		if (mc == null) {
			if (data.getConfig().getBoolean("MONGO.AUTHENTICATION.ENABLED")) {
				mongoConnection = new MongoConnection(
						data.getConfig().getString("MONGO.ADDRESS"),
						data.getConfig().getInt("MONGO.PORT"),
						"playerKitPvPData",
						data.getConfig().getString("MONGO.AUTHENTICATION.USERNAME"),
						data.getConfig().getString("MONGO.AUTHENTICATION.PASSWORD"),
						data.getConfig().getString("MONGO.AUTHENTICATION.AUTH-SOURCE")
				);
			} else {
				mongoConnection = new MongoConnection(
						data.getConfig().getString("MONGO.ADDRESS"),
						data.getConfig().getInt("MONGO.PORT"),
						"playerKitPvPData"
				);
			}
			ConnectionString connectionString = new ConnectionString(mongoConnection.getConnectionString()); // Fetches mongo address from sCore
			MongoClientSettings mcs = MongoClientSettings.builder()
														 .applyConnectionString(connectionString)
														 .retryWrites(true)
														 .build();
			mc = MongoClients.create(mcs);
		}

		return mc;
	}

	public static MongoDatabase getMongoDatabase(String name) {
		return getMongoClient().getDatabase(name);
	}
}
