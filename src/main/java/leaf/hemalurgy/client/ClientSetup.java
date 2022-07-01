/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.hemalurgy.client;

import leaf.hemalurgy.Hemalurgy;
import leaf.hemalurgy.client.render.curio.CurioRenderers;
import leaf.hemalurgy.client.render.curio.CuriosLayerDefinitions;
import leaf.hemalurgy.client.render.curio.model.SpikeModel;
import leaf.hemalurgy.utils.LogHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Hemalurgy.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup
{
	@SubscribeEvent
	public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions evt)
	{
		evt.registerLayerDefinition(CuriosLayerDefinitions.SPIKE, SpikeModel::createLayer);
	}

	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		CurioRenderers.register();
		LogHelper.info("Client setup complete!");
	}

}
