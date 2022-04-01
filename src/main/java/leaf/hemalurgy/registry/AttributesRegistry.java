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

public class AttributesRegistry
{
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Hemalurgy.MODID);

	public static final RegistryObject<Attribute> TIN_SENSES_ATTRIBUTE = ATTRIBUTES.register(
			Metal.TIN.getName(),
			() -> (new RangedAttribute(
					Hemalurgy.MODID + "." + Metal.TIN.getName(),
					0,
					0,
					1)).setSyncable(true)
	);
}
