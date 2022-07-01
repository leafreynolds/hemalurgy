/*
 * File created ~ 26 - 3 - 2022 ~ Leaf
 */

package leaf.hemalurgy.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.legobmw99.allomancy.api.enums.Metal;
import leaf.hemalurgy.items.HemalurgicSpikeItem;
import leaf.hemalurgy.registry.LootFunctionRegistry;
import leaf.hemalurgy.utils.MetalHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class InvestSpikeLootFunction extends LootItemConditionalFunction
{

	protected InvestSpikeLootFunction(LootItemCondition[] conditionsIn)
	{
		super(conditionsIn);
	}


	@Override
	public LootItemFunctionType getType()
	{
		return LootFunctionRegistry.INVEST_SPIKE;
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext lootContext)
	{
		if (!(stack.getItem() instanceof HemalurgicSpikeItem item))
		{
			return stack;
		}

		final Metal spikeMetalType = item.getMetalType();

		if (!MetalHelper.isMetalSpikeAvailable(spikeMetalType))
		{
			return stack;
		}

		Collection<Metal> hemalurgyStealWhitelist = MetalHelper.getHemalurgyStealWhitelist(spikeMetalType);

		Optional<Metal> stealType =
				hemalurgyStealWhitelist == null
				? Optional.empty()
				: hemalurgyStealWhitelist
						.stream()
						.filter(MetalHelper::isMetalSpikeAvailable)
						.skip(lootContext.getRandom().nextInt(hemalurgyStealWhitelist.size()))
						.findFirst();


		final float strengthLevel = Mth.clamp(5 + lootContext.getLuck(), 1, 10);

		switch (spikeMetalType)
		{
			case IRON:
				// add strength
				item.Invest(stack, MetalHelper.getPowerName(spikeMetalType), strengthLevel, UUID.randomUUID());

				break;
			case TIN:
			case COPPER:
			case CHROMIUM:
			{
				item.Invest(stack, MetalHelper.getPowerName(spikeMetalType), strengthLevel / 10, UUID.randomUUID());
			}
			break;
			//steals allomantic abilities
			case STEEL:
			case BRONZE:
			case CADMIUM:
			case ELECTRUM:
			//steals feruchemical abilities
			case PEWTER:
			case BRASS:
			case BENDALLOY:
			case GOLD:
			{
				if (!stealType.isPresent())
				{
					return stack;
				}

				item.Invest(stack, MetalHelper.getPowerName(spikeMetalType, stealType.get()), 5, UUID.randomUUID());
			}
			break;
		}

		return stack;
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<InvestSpikeLootFunction>
	{
		@Override
		public InvestSpikeLootFunction deserialize(JsonObject jsonObject, JsonDeserializationContext deserializationContext, LootItemCondition[] lootConditions)
		{
			return new InvestSpikeLootFunction(lootConditions);
		}
	}
}