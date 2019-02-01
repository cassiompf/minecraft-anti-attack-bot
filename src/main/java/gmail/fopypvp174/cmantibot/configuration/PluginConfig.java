package gmail.fopypvp174.cmantibot.configuration;

import org.bukkit.plugin.java.JavaPlugin;

public final class PluginConfig extends Config {
    public PluginConfig(JavaPlugin plugin, String fileName) {
        super(plugin, fileName);
    }

    public PluginConfig(JavaPlugin plugin, String fileName, String defaultsName) {
        super(plugin, fileName, defaultsName);
    }
}
