package pw.hintss.craftyhoppers.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
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
    public void onItemMove(InventoryClickEvent event) {
        if (event.getView().getTopInventory() instanceof BlockState) {
            final Block b = ((BlockState) event.getView().getTopInventory()).getBlock();

            for (Sign sign : Utils.getAttachedSigns(b)) {
                if (sign.getLine(0).equalsIgnoreCase("[open]")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Inventory i = ((InventoryHolder) b.getState()).getInventory();

                            for (ItemStack is : i.getContents()) {
                                b.getWorld().dropItem(b.getLocation(), is);
                            }

                            i.clear();
                        }
                    }.runTaskLater(plugin, 1);

                    return;
                }
            }
        }
    }
}
