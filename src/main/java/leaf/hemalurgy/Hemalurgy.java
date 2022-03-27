/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.hemalurgy;

import leaf.hemalurgy.compat.patchouli.PatchouliCompat;
import leaf.hemalurgy.registry.ItemsRegistry;
import leaf.hemalurgy.utils.LogHelper;
import leaf.hemalurgy.utils.ResourceLocationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
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

        //DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init));
        //DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::registerIconTextures));
        //DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::retrieveRegisteredIconSprites));


        MinecraftForge.EVENT_BUS.register(this);

        //Register our deferred registries
        ItemsRegistry.ITEMS.register(modBus);
        //EffectsRegistry.EFFECTS.register(modBus);
        //LootModifierRegistry.LOOT_MODIFIERS.register(modBus);
        //AttributesRegistry.ATTRIBUTES.register(modBus);
        //EntityRegistry.ENTITIES.register(modBus);

        //FeatureRegistry.FEATURES.register(modBus);
        //RecipeRegistry.SPECIAL_RECIPES.register(modBus);

        //AdvancementTriggerRegistry.init();


        // init cross mod compatibility stuff, if relevant
        PatchouliCompat.init();
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            //FeatureRegistry.registerConfiguredFeatures();
            //EntityRegistry.PrepareEntityAttributes();

        });

        //Entity Caps


        LogHelper.info("Common setup complete!");
    }

    private void loadComplete(FMLLoadCompleteEvent event)
    {
        event.enqueueWork(() ->
        {
            //ColorHandler.init();
        });
    }

}
