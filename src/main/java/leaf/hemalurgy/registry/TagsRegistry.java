/*
 * File created ~ 28 - 3 - 2022 ~Leaf
 */

package leaf.hemalurgy.registry;

import com.legobmw99.allomancy.api.enums.Metal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TagsRegistry
{

	public static void init()
	{
		TagsRegistry.Items.init();
	}

	public static class Items
	{
		private static void init()
		{
			//empty
		}

		public static final TagKey<Item> CURIO_ANY = makeItem(CuriosApi.MODID, "curio");
		public static final TagKey<Item> CURIO_BRACELET = makeItem(CuriosApi.MODID, "bracelet");


		public static final Map<Metal, TagKey<Item>> METAL_INGOT_TAGS =
				Arrays.stream(Metal.values())
						.collect(Collectors.toMap(
								Function.identity(),
								type -> forgeItemTag("ingots/" + type.name().toLowerCase())));


		public static TagKey<Item> makeItem(String domain, String path)
		{
			return ItemTags.create(new ResourceLocation(domain, path));
		}

		public static TagKey<Item> makeItem(ResourceLocation resourceLocation)
		{
			return ItemTags.create(resourceLocation);
		}

		private static TagKey<Item> forgeItemTag(String name)
		{
			final ResourceLocation forgeTagToMake = new ResourceLocation("forge", name);
			return makeItem(forgeTagToMake);
		}
	}
}
