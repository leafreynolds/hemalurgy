/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to SizableShrimp from the Forge Project discord!
 * Java isn't my first programming language, so I didn't know you could collect and set up items like this!
 * Makes setting up items for metals a breeze~
 */

package leaf.hemalurgy.registry;


import com.legobmw99.allomancy.api.enums.Metal;
import leaf.hemalurgy.Hemalurgy;
import leaf.hemalurgy.items.GuideItem;
import leaf.hemalurgy.items.HemalurgicSpikeItem;
import leaf.hemalurgy.utils.MetalHelper;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class ItemsRegistry
{
    static { TagsRegistry.init(); }

    public static final DeferredRegister<net.minecraft.world.item.Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Hemalurgy.MODID);

    //other items
    public static final RegistryObject<net.minecraft.world.item.Item> GUIDE = ITEMS.register("guide", () -> createItem(new GuideItem()));


    public static final Map<Metal, RegistryObject<Item>> METAL_SPIKE =
            Arrays.stream(Metal.values())
                    .filter(MetalHelper::isMetalSpikeAvailable)
                    .collect(Collectors.toMap(
                            Function.identity(),
                            type -> ITEMS.register(
                                    type.name().toLowerCase() + "_spike",
                                    () -> createItem(new HemalurgicSpikeItem(type))
                            )));



    private static <T extends net.minecraft.world.item.Item> T createItem(T item)
    {
        return item;
    }

}
