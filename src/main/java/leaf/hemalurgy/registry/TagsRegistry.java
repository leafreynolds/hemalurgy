/*
 * File created ~ 28 - 3 - 2022 ~Leaf
 */

package leaf.hemalurgy.registry;

import com.legobmw99.allomancy.Allomancy;
import com.legobmw99.allomancy.api.enums.Metal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TagsRegistry
{

    public static void init()
    {
        TagsRegistry.Items.init();
    }

    private static <T> Tag.Named<T> getOrRegister(List<? extends Tag.Named<T>> list,
                                                       Function<ResourceLocation, Tag.Named<T>> register,
                                                       ResourceLocation loc)
    {
        for (Tag.Named<T> existing : list)
        {
            if (existing.getName().equals(loc))
            {
                return existing;
            }
        }

        return register.apply(loc);
    }


    public static class Items
    {
        private static void init()
        {
            //empty
        }

        public static final Tag.Named<Item> CURIO_ANY = makeItem("curios", "curio");
        public static final Tag.Named<Item> CURIO_BRACELET = makeItem("curios", "bracelet");

        public static final Map<Metal, Tag.Named<Item>> METAL_INGOT_TAGS =
                Arrays.stream(Metal.values())
                        .collect(Collectors.toMap(
                                Function.identity(),
                                type -> forgeItemTag("ingots/" + type.name().toLowerCase())));

        public static Tag.Named<Item> makeItem(String domain, String path)
        {
            return ItemTags.bind(new ResourceLocation(domain, path).toString());
        }

        public static Tag.Named<Item> makeItem(ResourceLocation resourceLocation)
        {
            return ItemTags.bind(resourceLocation.toString());
        }

        private static Tag.Named<Item> forgeItemTag(String name)
        {
            final TagCollection<Item> allTags = ItemTags.getAllTags();

            final ResourceLocation forgeTagToMake = new ResourceLocation("forge", name);
            if (!allTags.hasTag(forgeTagToMake))
            {
                return makeItem(forgeTagToMake);
            }

            return (Tag.Named<Item>) allTags.getTag(forgeTagToMake);
        }

    }
}
