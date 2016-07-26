package pw.hintss.craftyhoppers;

import org.bukkit.Bukkit;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import pw.hintss.craftyhoppers.listeners.HopperListener;

import java.util.Iterator;

/**
 * Created by hintss on 7/22/2015.
 */
public class CraftyHoppers extends JavaPlugin {
    @Override
    public void onEnable() {
        // generate the list of smeltable items
        for (Iterator<Recipe> iter = Bukkit.recipeIterator(); iter.hasNext(); ) {
            Recipe r = iter.next();

            if (r instanceof FurnaceRecipe) {
                FurnaceRecipe smelt = (FurnaceRecipe) r;
                Utils.SMELTABLE.add(smelt.getInput().getType());
                getLogger().info("added smelting recipe for " + smelt.getInput().getType().name() + " -> " + smelt.getResult().getType().name());
            }
        }

        Bukkit.getPluginManager().registerEvents(new HopperListener(), this);
    }
}
