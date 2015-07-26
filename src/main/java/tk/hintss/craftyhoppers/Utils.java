package tk.hintss.craftyhoppers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hintss on 7/22/2015.
 */
public class Utils {
    private static BlockFace[] SIGN_DIRECTIONS = new BlockFace[] {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP};
    private static BlockFace[] BLOCK_DIRECTIONS = new BlockFace[] {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};

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

    public static boolean hasAdjacent(Block b, Material mat) {
        if (b == null) {
            return false;
        }

        for (BlockFace bf : BLOCK_DIRECTIONS) {
            Block possible = b.getRelative(bf);
            if (possible.getType() == mat) {
                return true;
            }
        }

        return false;
    }

    public static boolean itemMatchesList(String s, ItemStack is) {
        String[] split = s.split(",");

        for (String item : split) {
            if (item.contains(":")) {
                String[] bits = item.split(":");

                item = bits[0];
                String damage = bits[1];

                short isDmg = is.getDurability();

                if (!isInt(damage)) {
                    continue;
                }
                
                if (Short.valueOf(damage) != isDmg) {
                    continue;
                }
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

    public static List<Recipe> getRecipes(String s) {
        LinkedList<Recipe> recipes = new LinkedList<>();

        String[] split = s.split(",");

        for (String item : split) {
            short dmg = -1;

            if (item.contains(":")) {
                String[] bits = item.split(":");

                item = bits[0];
                String damage = bits[1];

                if (!isInt(damage)) {
                    continue;
                }

                dmg = Short.valueOf(damage);
            }

            if (!isInt(item)) {
                continue;
            }

            int id = Integer.valueOf(item);

            ItemStack is = new ItemStack(id, 1, dmg);

            for (Recipe r : Bukkit.getRecipesFor(is)) {
                if (r instanceof ShapedRecipe || r instanceof ShapelessRecipe) {
                    recipes.add(r);
                }
            }
        }

        return recipes;
    }

    public List<ItemStack> getRecipeIngredients(Recipe r) {
        List<ItemStack> ingredients = new ArrayList<>();

        if (r instanceof ShapelessRecipe) {
            outer:
            for (ItemStack is : ((ShapelessRecipe) r).getIngredientList()) {
                for (ItemStack i : ingredients) {
                    if (i.getType() == is.getType() && i.getData() == is.getData()) {
                        i.setAmount(is.getAmount() + i.getAmount());
                        continue outer;
                    }
                }

                ingredients.add(is);
            }
        } else if (r instanceof ShapedRecipe) {

        }
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
