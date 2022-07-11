package net.dec4234.files;

import net.dec4234.src.KitPvPCoreMain;
import org.bukkit.configuration.file.FileConfiguration;

public class YmlConfig {

	private static FileConfiguration fc;

	public YmlConfig() {
		fc = KitPvPCoreMain.getInstance().getConfig();
		fc.options().copyDefaults(true);
		save();
	}

	public static void set(String path, String value) {
		fc.set(path, value);
		save();
	}

	public static void save() {
		KitPvPCoreMain.getInstance().saveConfig();
	}

	public static FileConfiguration getFC() {
		return fc;
	}
}
