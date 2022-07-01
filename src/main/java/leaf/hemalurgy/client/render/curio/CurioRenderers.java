package leaf.hemalurgy.client.render.curio;

import leaf.hemalurgy.client.render.curio.renderer.SpikeRenderer;
import leaf.hemalurgy.registry.ItemsRegistry;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.function.Supplier;

public class CurioRenderers
{
	public static void register()
	{
		final Supplier<ICurioRenderer> spikeRenderer = SpikeRenderer::new;
		for (RegistryObject<Item> itemRegistryObject : ItemsRegistry.METAL_SPIKE.values())
		{
			CuriosRendererRegistry.register(itemRegistryObject.get(), spikeRenderer);
		}
	}
}
