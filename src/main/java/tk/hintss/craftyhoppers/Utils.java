package tk.hintss.craftyhoppers;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.inventory.InventoryHolder;

import java.util.LinkedList;

/**
 * Created by hintss on 7/22/2015.
 */
public class Utils {
    private static BlockFace[] SIGN_DIRECTIONS = new BlockFace[] {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP};

    public static Sign getAttachedSign(Block b) {
        if (b == null) {
            return null;
        }

        LinkedList<Sign> signs = new LinkedList<>();

        if (b.getState() instanceof InventoryHolder) {
            for (BlockFace bf : SIGN_DIRECTIONS) {
                Block possibleSign = b.getRelative(bf);
                if (possibleSign.getState() instanceof Sign && ((Sign) possibleSign.getState()).getLine(0).equalsIgnoreCase("[infinitechest]")) {
                    if (((org.bukkit.material.Sign) possibleSign.getState().getData()).getAttachedFace() == bf.getOppositeFace()) {
                        return (Sign) possibleSign.getState();
                    }
                }
            }
        }

        return null;
    }
}
