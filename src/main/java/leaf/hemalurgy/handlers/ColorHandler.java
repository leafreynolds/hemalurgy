/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to Vazkii and Botania!
 * for the easier to understand example of hooking into color tints
 */

package leaf.hemalurgy.handlers;

import com.legobmw99.allomancy.api.enums.Metal;
import leaf.hemalurgy.capability.entity.IHasColour;
import leaf.hemalurgy.registry.ItemsRegistry;
import leaf.hemalurgy.utils.MetalHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;

public final class ColorHandler
{

	public static void init()
	{
		ItemColors itemColors = Minecraft.getInstance().getItemColors();

		ItemColor cosmereColourHandler =
				(itemStack, tintIndex) -> tintIndex == 0
				                          ? ((IHasColour) itemStack.getItem()).getColourValue()
				                          : -1;

		for (Metal metalType : Metal.values())
		{
			if (MetalHelper.isMetalSpikeAvailable(metalType))
			{
				itemColors.register(cosmereColourHandler, ItemsRegistry.METAL_SPIKE.get(metalType).get());
			}
		}
	}
}
