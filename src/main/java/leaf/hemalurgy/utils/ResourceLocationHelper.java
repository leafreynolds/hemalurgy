/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.hemalurgy.utils;

import leaf.hemalurgy.Hemalurgy;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public class ResourceLocationHelper
{
    public static ResourceLocation prefix(String path)
    {
        return new ResourceLocation(Hemalurgy.MODID, path.toLowerCase(Locale.ROOT));
    }
}
