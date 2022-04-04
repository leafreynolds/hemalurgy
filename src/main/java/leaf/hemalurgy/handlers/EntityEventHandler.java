/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.hemalurgy.handlers;

import com.legobmw99.allomancy.api.enums.Metal;
import com.legobmw99.allomancy.modules.powers.data.AllomancerCapability;
import leaf.hemalurgy.Hemalurgy;
import leaf.hemalurgy.items.HemalurgicSpikeItem;
import leaf.hemalurgy.registry.AttributesRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Hemalurgy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEventHandler
{
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event)
    {
        if (event.getSource().getEntity() instanceof Player)
        {
            Player playerEntity = (Player) event.getSource().getEntity();
            playerEntity.getCapability(AllomancerCapability.PLAYER_CAP).ifPresent((iAllomancerData) ->
            {
                ItemStack itemstack = playerEntity.getMainHandItem();
                if (itemstack.getItem() instanceof HemalurgicSpikeItem)
                {
                    //entity was killed by a spike
                    HemalurgicSpikeItem spikeItem = (HemalurgicSpikeItem) itemstack.getItem();
                    //pass in killed entity for the item to figure out what to do
                    spikeItem.killedEntity(itemstack, playerEntity,event.getEntityLiving());
                }

            });
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event)
    {
        event.getOriginal().revive();

        for (Metal metalType : Metal.values())
        {
            //check for others
            final RegistryObject<Attribute> metalRelatedAttribute = AttributesRegistry.COSMERE_ATTRIBUTES.get(metalType.getName());
            if (metalRelatedAttribute != null && metalRelatedAttribute.isPresent())
            {
                AttributeInstance oldPlayerAttribute = event.getOriginal().getAttribute(metalRelatedAttribute.get());
                AttributeInstance newPlayerAttribute = event.getPlayer().getAttribute(metalRelatedAttribute.get());

                if (newPlayerAttribute != null && oldPlayerAttribute != null)
                {
                    newPlayerAttribute.setBaseValue(oldPlayerAttribute.getBaseValue());
                }

            }
        }

    }


    @SubscribeEvent
    public void onXPChange(PlayerXpEvent.XpChange event)
    {
        if (event.getEntityLiving().level.isClientSide)
        {
            return;
        }

        RegistryObject<Attribute> xpGainRateAttribute = AttributesRegistry.COSMERE_ATTRIBUTES.get(Metal.COPPER.getName());
        if (xpGainRateAttribute != null && xpGainRateAttribute.isPresent())
        {
            AttributeInstance attribute = event.getPlayer().getAttribute(xpGainRateAttribute.get());
            if (attribute != null)
            {
                event.setAmount((int) (event.getAmount() * attribute.getValue()));
            }
        }
    }
}
