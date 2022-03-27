/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.hemalurgy.handlers;

import leaf.hemalurgy.Hemalurgy;
import leaf.hemalurgy.commands.HemalurgyCommandRegister;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Hemalurgy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents
{
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event)
    {
        HemalurgyCommandRegister.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event)
    {

    }
}