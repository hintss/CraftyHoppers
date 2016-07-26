package pw.hintss.craftyhoppers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.*;

/**
 * Created by hintss on 7/22/2015.
 */
public class Utils {
    private static BlockFace[] SIGN_DIRECTIONS = new BlockFace[] {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP};
    private static BlockFace[] BLOCK_DIRECTIONS = new BlockFace[] {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};

    public static Set<Material> SMELTABLE = new HashSet<>();

    /**
     * Gets all the signs attached to a block. Will check to make sure the sign is actually attached and not simply adjacent.
     * @param b the block to look for attached signs on
     * @return a List of the BlockStates of the signs
     */
    public static List<Sign> getAttachedSigns(Block b) {
        LinkedList<Sign> signs = new LinkedList<>();

        if (b == null) {
            return signs;
        }

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

    /**
     * Checks if the given block has a block of material mat touching it.
     * @param b the block we're looking for blocks next to
     * @param mat the type of block we're looking for
     * @return true if an attached block has the right type
     */
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
            if ((item.equalsIgnoreCase("smelt") || item.equalsIgnoreCase("smeltable")) && SMELTABLE.contains(is.getType())) {
                return true;
            }

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

    /**
     * Gets a list of ingredients needed to make a given recipe. Only implemented for crafting recpies.
     * @param r The recipe.
     * @return A list of ItemStacks. ingredients needing multiple will be condensed into one itemstack with >1 quantity.
     */

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
            HashMap<Character, Integer> ingredientCount = new HashMap<>();

            for (String s : ((ShapedRecipe) r).getShape()) {
                for (char c : s.toCharArray()) {
                    if (ingredientCount.containsKey(c)) {
                        ingredientCount.put(c, ingredientCount.get(c) + 1);
                    } else {
                        ingredientCount.put(c, 1);
                    }
                }
            }

            for (Character c : ((ShapedRecipe) r).getIngredientMap().keySet()) {
                ItemStack is = ((ShapedRecipe) r).getIngredientMap().get(c);
                is.setAmount(ingredientCount.get(c));

                ingredients.add(is);
            }
        }

        return ingredients;
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
