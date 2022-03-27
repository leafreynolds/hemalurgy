/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.hemalurgy.properties;

import leaf.hemalurgy.items.HemalurgyItemGroups;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class PropTypes
{
    public static class Blocks
    {
        public static final Supplier<Block.Properties> EXAMPLE = () -> Block.Properties.of(Material.GLASS).strength(2.0F, 6.0F);
    }

    public static class Items
    {
        public static final Supplier<Item.Properties> ONE = () -> new Item.Properties().tab(HemalurgyItemGroups.ITEMS).stacksTo(1);

        public static final Supplier<Item.Properties> SIXTEEN = () -> new Item.Properties().tab(HemalurgyItemGroups.ITEMS).stacksTo(16);

        public static final Supplier<Item.Properties> SIXTY_FOUR = () -> new Item.Properties().tab(HemalurgyItemGroups.ITEMS).stacksTo(64);
    }
}
