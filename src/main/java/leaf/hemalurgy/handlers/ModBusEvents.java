/*
 * File created ~ 5 - 5 - 2021 ~ Leaf
 */

package leaf.hemalurgy.handlers;

import com.legobmw99.allomancy.api.enums.Metal;
import leaf.hemalurgy.Hemalurgy;
import leaf.hemalurgy.registry.AttributesRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Hemalurgy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEvents
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
    {
        for (Metal metalType : Metal.values())
        {
            //check for others
            final RegistryObject<Attribute> metalRelatedAttribute = AttributesRegistry.HEMALURGY_ATTRIBUTES.get(metalType.getName());
            if (metalRelatedAttribute != null && metalRelatedAttribute.isPresent())
            {
                //player only, because it doesn't make sense for mobs to have them.
                //eg xp gain rate,
                event.add(EntityType.PLAYER, metalRelatedAttribute.get());
            }

        }
    }
}
