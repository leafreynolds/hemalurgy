/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.hemalurgy.datagen.tags;

import leaf.hemalurgy.Hemalurgy;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagsGen extends BlockTagsProvider
{
    public BlockTagsGen(DataGenerator generatorIn, ExistingFileHelper existingFileHelper)
    {
        super(generatorIn, Hemalurgy.MODID, existingFileHelper);
    }

    @Override
    protected void addTags()
    {
    }

}
