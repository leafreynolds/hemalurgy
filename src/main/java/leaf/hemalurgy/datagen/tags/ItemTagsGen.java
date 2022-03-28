/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.hemalurgy.datagen.tags;

import com.legobmw99.allomancy.api.enums.Metal;
import leaf.hemalurgy.Hemalurgy;
import leaf.hemalurgy.items.HemalurgicSpikeItem;
import leaf.hemalurgy.registry.ItemsRegistry;
import leaf.hemalurgy.registry.TagsRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ItemTagsGen extends ItemTagsProvider
{
    public ItemTagsGen(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(dataGenerator, blockTagsProvider, Hemalurgy.MODID, existingFileHelper);
    }

    @Override
    protected void addTags()
    {
        for (Metal metalType : Metal.values())
        {
            if (ItemsRegistry.METAL_SPIKE.containsKey(metalType))
            {
                final Item item = ItemsRegistry.METAL_SPIKE.get(metalType).get();
                final Tag.Named<Item> curioAny = TagsRegistry.Items.CURIO_ANY;
                add(curioAny, item);
            }
        }
    }

    public void add(Tag.Named<Item> branch, Item item)
    {
        this.tag(branch).add(item);
    }

    public void add(Tag.Named<Item> branch, Item... item)
    {
        this.tag(branch).add(item);
    }


}