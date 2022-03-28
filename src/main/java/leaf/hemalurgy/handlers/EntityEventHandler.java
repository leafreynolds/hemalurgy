/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.hemalurgy.handlers;

import com.legobmw99.allomancy.modules.powers.data.AllomancerCapability;
import leaf.hemalurgy.Hemalurgy;
import leaf.hemalurgy.items.HemalurgicSpikeItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
                    spikeItem.killedEntity(itemstack, event.getEntityLiving());
                }

            });
        }
    }


}
