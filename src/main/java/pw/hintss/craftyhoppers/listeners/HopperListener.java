package pw.hintss.craftyhoppers.listeners;

import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import pw.hintss.craftyhoppers.Utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hintss on 7/22/2015.
 */
public class HopperListener implements Listener {
    @EventHandler
    public void onItemPickup(InventoryPickupItemEvent event) {
        if (!(event.getInventory().getHolder() instanceof Hopper)) {
            return;
        }

        Hopper h = ((Hopper) event.getInventory().getHolder());
        List<Sign> signs = Utils.getAttachedSigns(h.getBlock());

        if (!hopperShouldIngest(h, signs, event.getItem().getItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemMove(InventoryMoveItemEvent event) {
        if (!(event.getDestination().getHolder() instanceof Hopper)) {
            return;
        }

        Hopper h = ((Hopper) event.getDestination().getHolder());
        List<Sign> signs = Utils.getAttachedSigns(h.getBlock());

        if (!hopperShouldIngest(h, signs, event.getItem())) {
            event.setCancelled(true);
        }
    }

    private boolean hopperShouldIngest(Hopper h, List<Sign> signs, ItemStack is) {
        // whether this itemstack has encountered a sign that didn't like it yet
        boolean negate = false;

        for (Sign sign : signs) {
            // item sorting
            if (sign.getLine(0).equalsIgnoreCase("[sort]")) {
                for (int i = 1; i < 4; i++) {
                    if (Utils.itemMatchesList(sign.getLine(i), is)) {
                        return true;
                    }
                }

                negate = true;
            } else if (sign.getLine(0).equalsIgnoreCase("[craft]")) {
                // item crafting
                // require an adjacent workbench
                if (!Utils.hasAdjacent(h.getBlock(), Material.WORKBENCH)) {
                    continue;
                }

                List<Recipe> recipes = new LinkedList<>();

                for (int i = 1; i < 4; i++) {
                    recipes.addAll(Utils.getRecipes(sign.getLine(i)));
                }

                Inventory i = h.getInventory();

                // TODO - stuff
            }
        }

        return !negate;
    }
}
