/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.hemalurgy.registry;

import com.legobmw99.allomancy.api.enums.Metal;
import leaf.hemalurgy.Hemalurgy;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class AttributesRegistry
{
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Hemalurgy.MODID);
	public static final Map<String, RegistryObject<Attribute>> HEMALURGY_ATTRIBUTES = makeAttributeMap();

	public static Map<String, RegistryObject<Attribute>> makeAttributeMap()
	{
		Map<String, RegistryObject<Attribute>> attributeModifiers = new HashMap<>();

		for (Metal metalType : Metal.values())
		{
			if (metalType != Metal.COPPER && metalType != Metal.TIN)
		{
			continue;
		}
			final String metalName = metalType.getName();

			int defaultVal = 0;
			int min = 0;
			int max = 0;


			switch (metalType)
			{
				case TIN:
				{
					defaultVal = 0;
					min = 0;
					max = 1;
				}
				break;
				case COPPER:
				{
					defaultVal = 1;
					min = 0;
					max = 20;
				}
				break;
			}

			//requires effectively final values
			int finalDefaultVal = defaultVal;
			int finalMin = min;
			int finalMax = max;
			attributeModifiers.put(metalName, ATTRIBUTES.register(
					metalName,
					() -> (new RangedAttribute(
							Hemalurgy.MODID + "." + metalName,
							finalDefaultVal,
							finalMin,
							finalMax))
							.setSyncable(true)
			));
		}


		return attributeModifiers;
	}

}
