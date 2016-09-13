package pw.hintss.craftyhoppers.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pw.hintss.craftyhoppers.CraftyHoppers;
import pw.hintss.craftyhoppers.Utils;

/**
 * Used to implement human interaction with open chests (could be any inv, really)
 */
public class ItemMoveListener implements Listener {
    CraftyHoppers plugin;

    public ItemMoveListener(CraftyHoppers plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        if (event.getInventory() instanceof BlockState) {
            Block b = ((BlockState) event.getView().getTopInventory()).getBlock();

            for (Sign sign : Utils.getAttachedSigns(b)) {
                if (sign.getLine(0).equalsIgnoreCase("[open]")) {
                    Inventory i = ((InventoryHolder) b.getState()).getInventory();

                    for (ItemStack is : i.getContents()) {
                        b.getWorld().dropItem(b.getLocation(), is);
                    }

                    i.clear();

                    return;
                }
            }
        }
    }
}
