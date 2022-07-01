/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.hemalurgy.handlers;

import com.legobmw99.allomancy.api.enums.Metal;
import leaf.hemalurgy.Hemalurgy;
import leaf.hemalurgy.api.ISpiritweb;
import leaf.hemalurgy.capability.entity.SpiritwebCapability;
import leaf.hemalurgy.items.HemalurgicSpikeItem;
import leaf.hemalurgy.registry.AttributesRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
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
            ItemStack itemstack = playerEntity.getMainHandItem();
            if (itemstack.getItem() instanceof HemalurgicSpikeItem)
            {
                //entity was killed by a spike
                HemalurgicSpikeItem spikeItem = (HemalurgicSpikeItem) itemstack.getItem();
                //pass in killed entity for the item to figure out what to do
                spikeItem.killedEntity(itemstack, playerEntity, event.getEntityLiving());
            }
        }
    }

    @SubscribeEvent
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        SpiritwebCapability.attachEntityCapabilities(event);
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event)
    {
        event.getOriginal().revive();

        SpiritwebCapability.get(event.getOriginal()).ifPresent((oldSpiritWeb) ->
                SpiritwebCapability.get(event.getPlayer()).ifPresent((newSpiritWeb) ->
                {
                    //copy across anything from the old player
                    //things like eye height and attributes
                    newSpiritWeb.transferFrom(oldSpiritWeb);
                }));

    }

    @SubscribeEvent
    public static void onTrackPlayer(PlayerEvent.StartTracking startTracking)
    {
        SpiritwebCapability.get(startTracking.getPlayer()).ifPresent(cap ->
        {
            cap.syncToClients(null);
        });
    }


    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingUpdateEvent event)
    {
        SpiritwebCapability.get(event.getEntityLiving()).ifPresent(ISpiritweb::tick);
    }

    @SubscribeEvent
    public void onXPChange(PlayerXpEvent.XpChange event)
    {
        if (event.getEntityLiving().level.isClientSide)
        {
            return;
        }

        RegistryObject<Attribute> xpGainRateAttribute = AttributesRegistry.HEMALURGY_ATTRIBUTES.get(Metal.COPPER.getName());
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
