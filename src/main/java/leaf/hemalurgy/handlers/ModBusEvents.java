/*
 * File created ~ 5 - 5 - 2021 ~ Leaf
 */

package leaf.hemalurgy.handlers;

import leaf.hemalurgy.Hemalurgy;
import leaf.hemalurgy.registry.AttributesRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Hemalurgy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEvents
{
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
    {
        event.add(EntityType.PLAYER, AttributesRegistry.TIN_SENSES_ATTRIBUTE.get());
    }
}
