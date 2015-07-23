package tk.hintss.craftyhoppers;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;

/**
 * Created by hintss on 7/22/2015.
 */
public class Utils {
    private static BlockFace[] SIGN_DIRECTIONS = new BlockFace[] {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP};

    public static LinkedList<Sign> getAttachedSigns(Block b) {
        if (b == null) {
            return null;
        }

        LinkedList<Sign> signs = new LinkedList<>();

        for (BlockFace bf : SIGN_DIRECTIONS) {
            Block possibleSign = b.getRelative(bf);
            if (possibleSign.getState() instanceof Sign) {
                if (((org.bukkit.material.Sign) possibleSign.getState().getData()).getAttachedFace() == bf.getOppositeFace()) {
                    signs.add(((Sign) possibleSign.getState()));
                }
            }
        }

        return signs;
    }

    public static boolean itemMatchesList(String s, ItemStack is) {
        String[] split = s.split(",");

        for (String item : split) {
            if (item.contains(":")) {
                String[] bits = item.split(":");

                item = bits[0];
                String damage = bits[1];

                short isDmg = is.getDurability();

                if (!isInt(damage)) continue;
                if (Short.valueOf(damage) != isDmg) continue;
            }

            if (!isInt(item)) {
                continue;
            }

            if (Integer.valueOf(item) == is.getType().getId()) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInt(String s) {
        if (s.length() == 0) {
            return false;
        }

        for (char c : s.toCharArray()) {
            if (c < '0') {
                return false;
            }

            if (c > '9') {
                return false;
            }
        }

        return true;
    }
}
