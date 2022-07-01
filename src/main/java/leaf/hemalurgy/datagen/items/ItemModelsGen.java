/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.hemalurgy.datagen.items;

import leaf.hemalurgy.Hemalurgy;
import leaf.hemalurgy.items.HemalurgicSpikeItem;
import leaf.hemalurgy.registry.ItemsRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ItemModelsGen extends ItemModelProvider
{

    public ItemModelsGen(DataGenerator generator, ExistingFileHelper existingFileHelper)
    {
        super(generator, Hemalurgy.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        for (RegistryObject<Item> itemRegistryObject : ItemsRegistry.ITEMS.getEntries())
        {
            String path = getPath(itemRegistryObject);
            Item item = itemRegistryObject.get();

            //blocks have their own model rules
            if (item instanceof BlockItem)
            {
                continue;
            }
            else if (item instanceof HemalurgicSpikeItem)
            {
                this.getBuilder(path)
                        .parent(new ModelFile.UncheckedModelFile("hemalurgy:item/spike"))
                        .texture("layer0", modLoc("item/" + "metal_spike"));
                continue;
            }

            //else normal item texture rules apply
            simpleItem(path, path);
        }

    }

    public String getPath(Supplier<? extends Item> itemSupplier)
    {
        ResourceLocation location = itemSupplier.get().getRegistryName();
        return location.getPath();
    }

    public ItemModelBuilder simpleItem(String path, String texturePath)
    {
        return this.getBuilder(path)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc("item/" + texturePath));
    }
}