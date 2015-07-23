package tk.hintss.craftyhoppers.listeners;

import org.bukkit.block.Hopper;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import tk.hintss.craftyhoppers.Utils;

/**
 * Created by hintss on 7/22/2015.
 */
public class HopperListener implements Listener {
    @EventHandler
    public void onItemPickup(InventoryPickupItemEvent event) {
        if (!(event.getInventory().getHolder() instanceof Hopper)) {
            return;
        }

        boolean shouldCancel = false;

        shouldCancel = !hopperShouldIngest(((Hopper) event.getInventory().getHolder()), event.getItem().getItemStack());

        if (shouldCancel) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemMove(InventoryMoveItemEvent event) {
        if (!(event.getDestination().getHolder() instanceof Hopper)) {
            return;
        }

        boolean shouldCancel = false;

        shouldCancel = !hopperShouldIngest(((Hopper) event.getDestination().getHolder()), event.getItem());

        if (shouldCancel) {
            event.setCancelled(true);
        }
    }

    private boolean hopperShouldIngest(Hopper h, ItemStack is) {
        boolean negativeEvidence = false;

        for (Sign sign : Utils.getAttachedSigns(h.getBlock())) {
            // item sorting
            if (sign.getLine(0).equals("[sort]")) {
                for (int i = 1; i < 4; i++) {
                    if (Utils.itemMatchesList(sign.getLine(i), is)) return true;
                }

                negativeEvidence = true;
            }
        }

        return !negativeEvidence;
    }
}
