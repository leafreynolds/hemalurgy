/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.hemalurgy.items;

import com.legobmw99.allomancy.api.enums.Metal;
import leaf.hemalurgy.Hemalurgy;
import leaf.hemalurgy.registry.ItemsRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class HemalurgyItemGroups
{
    public static CreativeModeTab ITEMS = new CreativeModeTab(Hemalurgy.MODID + ".items")
    {
        @Override
        public ItemStack makeIcon()
        {
            return new ItemStack(ItemsRegistry.METAL_SPIKE.get(Metal.CADMIUM).get());
        }
    };
}
