/*
 * File created ~ 1 - 7 - 2022 ~Leaf
 */

package leaf.hemalurgy.registry;

import leaf.hemalurgy.Hemalurgy;
import leaf.hemalurgy.loot.InvestSpikeLootFunction;
import leaf.hemalurgy.loot.RandomiseMetalTypeLootFunction;
import leaf.hemalurgy.utils.LogHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class LootFunctionRegistry
{
	public static final LootItemFunctionType INVEST_SPIKE = register("invest_spike", new InvestSpikeLootFunction.Serializer());
	public static final LootItemFunctionType RANDOMISE_METALTYPE = register("randomise_metaltype", new RandomiseMetalTypeLootFunction.Serializer());

	private static LootItemFunctionType register(String name, Serializer<? extends LootItemFunction> serializer)
	{
		return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(Hemalurgy.MODID, name), new LootItemFunctionType(serializer));
	}

	public static void Register()
	{
		//static finals get initialized on class being referenced
		LogHelper.info("Registering Loot Functions");
	}
}
