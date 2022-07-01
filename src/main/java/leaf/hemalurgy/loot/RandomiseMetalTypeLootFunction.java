/*
 * File created ~ 26 - 3 - 2022 ~ Leaf
 */

package leaf.hemalurgy.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.legobmw99.allomancy.api.enums.Metal;
import leaf.hemalurgy.items.HemalurgicSpikeItem;
import leaf.hemalurgy.capability.entity.IHasMetalType;
import leaf.hemalurgy.registry.ItemsRegistry;
import leaf.hemalurgy.registry.LootFunctionRegistry;
import leaf.hemalurgy.utils.MetalHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RandomiseMetalTypeLootFunction extends LootItemConditionalFunction
{

	protected RandomiseMetalTypeLootFunction(LootItemCondition[] conditionsIn)
	{
		super(conditionsIn);
	}


	@Override
	public LootItemFunctionType getType()
	{
		return LootFunctionRegistry.RANDOMISE_METALTYPE;
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext lootContext)
	{
		final Item item = stack.getItem();
		if (!(item instanceof IHasMetalType))
		{
			return stack;
		}

		if (item instanceof HemalurgicSpikeItem)
		{
			List<Metal> metalTypes =
					Arrays.stream(Metal.values())
							.filter(MetalHelper::isMetalSpikeAvailable)
							.collect(Collectors.toList());
			Collections.shuffle(metalTypes);
			Optional<Metal> metalType = metalTypes.stream().findFirst();

			if (metalType.isPresent())
			{
				stack = new ItemStack(ItemsRegistry.METAL_SPIKE.get(metalType.get()).get());
			}
		}

		return stack;
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<RandomiseMetalTypeLootFunction>
	{
		@Override
		public RandomiseMetalTypeLootFunction deserialize(JsonObject jsonObject, JsonDeserializationContext deserializationContext, LootItemCondition[] lootConditions)
		{
			return new RandomiseMetalTypeLootFunction(lootConditions);
		}
	}
}