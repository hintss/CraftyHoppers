package pw.hintss.craftyhoppers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pw.hintss.craftyhoppers.listeners.HopperListener;

/**
 * Created by hintss on 7/22/2015.
 */
public class CraftyHoppers extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new HopperListener(), this);
    }
}
