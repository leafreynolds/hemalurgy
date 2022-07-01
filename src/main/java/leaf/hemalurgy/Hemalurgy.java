/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.hemalurgy;

import leaf.hemalurgy.api.ISpiritweb;
import leaf.hemalurgy.compat.curios.CuriosCompat;
import leaf.hemalurgy.compat.patchouli.PatchouliCompat;
import leaf.hemalurgy.handlers.ColorHandler;
import leaf.hemalurgy.network.Network;
import leaf.hemalurgy.registry.AttributesRegistry;
import leaf.hemalurgy.registry.ItemsRegistry;
import leaf.hemalurgy.registry.LootFunctionRegistry;
import leaf.hemalurgy.utils.LogHelper;
import leaf.hemalurgy.utils.ResourceLocationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Hemalurgy.MODID)
public class Hemalurgy
{
    public static final String MODID = "hemalurgy";
    public static final ResourceLocation HEMALURGY_LOC = ResourceLocationHelper.prefix(Hemalurgy.MODID);

    public Hemalurgy()
    {
        LogHelper.info("Registering Hemalurgy related mcgubbins!");
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::commonSetup);
        modBus.addListener(this::loadComplete);
        modBus.addListener(this::onAddCaps);


        MinecraftForge.EVENT_BUS.register(this);

        //Register our deferred registries
        ItemsRegistry.ITEMS.register(modBus);
        AttributesRegistry.ATTRIBUTES.register(modBus);

        Network.init();

        // init cross mod compatibility stuff, if relevant
        CuriosCompat.init();
        PatchouliCompat.init();
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            //FeatureRegistry.registerConfiguredFeatures();
            //EntityRegistry.PrepareEntityAttributes();
            LootFunctionRegistry.Register();

        });

        //Entity Caps


        LogHelper.info("Common setup complete!");
    }

    private void onAddCaps(RegisterCapabilitiesEvent capabilitiesEvent)
    {
        capabilitiesEvent.register(ISpiritweb.class);
    }

    private void loadComplete(FMLLoadCompleteEvent event)
    {
        event.enqueueWork(() ->
        {
            ColorHandler.init();
        });
    }

}
