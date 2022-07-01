/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.hemalurgy.datagen.language;

import com.legobmw99.allomancy.api.enums.Metal;
import leaf.hemalurgy.Hemalurgy;
import leaf.hemalurgy.constants.Constants;
import leaf.hemalurgy.items.HemalurgyItemGroups;
import leaf.hemalurgy.utils.LogHelper;
import leaf.hemalurgy.utils.MetalHelper;
import leaf.hemalurgy.utils.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.data.DataGenerator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class EngLangGen extends LanguageProvider
{
    private final DataGenerator generator;

    public EngLangGen(DataGenerator gen)
    {
        super(gen, Hemalurgy.MODID, "en_us");
        this.generator = gen;
    }

    @Override
    protected void addTranslations()
    {

        //Items and Blocks
        for (Item item : ForgeRegistries.ITEMS.getValues())
        {
            if (item.getRegistryName().getNamespace().contentEquals(Hemalurgy.MODID))
            {
                final String path = item.getRegistryName().getPath();
                String localisedString = StringHelper.fixCapitalisation(path);
                final String tooltipStringKey = String.format(Constants.StringKeys.HEMALURGY_ITEM_TOOLTIP, path);

                String tooltipString = "";


                //string overrides
                switch (localisedString)
                {
                    case "Guide":
                        localisedString = "Leather Journal";
                        tooltipString = "If patchouli is installed, this is your guide to the mod";
                        break;
                }

                add(tooltipStringKey, tooltipString);
                add(item.getDescriptionId(), localisedString);


            }
        }

        //Entities
        for (EntityType<?> type : ForgeRegistries.ENTITIES)
        {
            final String value = StringHelper.fixCapitalisation(type.getRegistryName().getPath());
            if (type.getRegistryName().getNamespace().equals(Hemalurgy.MODID))
            {
                add(type.getDescriptionId(), value);
            }
        }

        //ItemGroups/Tabs
        add("itemGroup." + HemalurgyItemGroups.ITEMS.getRecipeFolderName(), "Hemalurgy Items");

        //Damage Sources

        //Containers

        //effects

        //Sound Schemes

        //Configs

        //Commands
        add(Constants.StringKeys.SET_EYE_HEIGHT_SUCCESS, "Set eye height to %s");


        //Tooltips
        add(Constants.StringKeys.SHIFT_ITEM_TOOLTIP, "\u00A77Hold \u00A78[\u00A7eShift\u00A78]");
        add(Constants.StringKeys.SHIFT_CONTROL_ITEM_TOOLTIP, "\u00A77Hold \u00A78[\u00A7eShift\u00A78] \u00A77and \u00A78[\u00A7eControl\u00A78]");
        add(Constants.StringKeys.CONTROL_ITEM_TOOLTIP, "\u00A77Hold \u00A78[\u00A7eControl\u00A78]");

        add(Constants.StringKeys.PATCHOULI_NOT_INSTALLED, "Patchouli is not installed");
        add(Constants.StringKeys.CONTAINED_POWERS_FOUND, "Hemalurgic Charge:");

        add("tooltip.cosmere.attribute.iron","+%s Attack Damage");
        add("tooltip.cosmere.attribute.tin","+%s%% Better Night Vision");
        add("tooltip.cosmere.attribute.copper","+%s%% XP");
        add("tooltip.cosmere.attribute.zinc","+%s%% Emotional Fortitude");
        add("tooltip.cosmere.attribute.duralumin","");//todo hemalurgic connection/identity
        add("tooltip.cosmere.attribute.chromium","+%s%% Luck");
        add("tooltip.cosmere.attribute.nicrosil","");//todo hemalurgic investiture

        for(Metal spikeMetal : Metal.values())
        {
            if (MetalHelper.isMetalSpikeAvailable(spikeMetal))
            {
                for(Metal stealType : Metal.values())
                {
                    final String s = MetalHelper.getPowerName(spikeMetal, stealType);
                    try
                    {
                        add("tooltip.hemalurgy." + s, StringHelper.fixCapitalisation(s));
                    }
                    catch (Exception e)
                    {

                    }
                }
            }
        }

        //Guide book
        add("hemalurgy.landing", "They say the soul is infinite. They didn't say how empty it was to mutilate it. That's where this guide comes in.");

        add("category.basics", "Basics");

        add("entry.welcome", "Welcome");
        add("entry.guide", "Guide");


        //KeyBindings
        add(Constants.StringKeys.KEYS_CATEGORY, "Hemalurgy");


        //Advancements

        add("advancements.hemalurgy.main.title", "Hemalurgy");
        add("advancements.hemalurgy.main.description", "Welcome to Hemalurgy. The way to mutilate your inner soul.");


        add("advancements.hemalurgy.obtained_guide.title", "Well Read");
        add("advancements.hemalurgy.obtained_guide.description", "");

        add("advancements.hemalurgy.blank.title", "blank");
        add("advancements.hemalurgy.blank.description", "blank");

        //misc

        add(Hemalurgy.HEMALURGY_LOC.toString(), "Hemalurgy");
        add("biome.hemalurgy.hemalurgy", "Hemalurgy");

    }

}
