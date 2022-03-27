/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to SizableShrimp from the Forge Project discord!
 * Java isn't my first programming language, so I didn't know you could collect and set up items like this!
 * Makes setting up items for metals a breeze~
 */

package leaf.hemalurgy.registry;


import leaf.hemalurgy.Hemalurgy;
import leaf.hemalurgy.items.GuideItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ItemsRegistry
{
    public static final DeferredRegister<net.minecraft.world.item.Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Hemalurgy.MODID);

    //other items
    public static final RegistryObject<net.minecraft.world.item.Item> GUIDE = ITEMS.register("guide", () -> createItem(new GuideItem()));


    private static <T extends net.minecraft.world.item.Item> T createItem(T item)
    {
        return item;
    }

}
