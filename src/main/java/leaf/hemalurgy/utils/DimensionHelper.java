/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.hemalurgy.utils;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class DimensionHelper
{

    public static boolean isDimensionOfType(Level world, ResourceKey<DimensionType> dimTypeKey)
    {
        // I think it has to be done this way since the soul dimension is not
        // shared between players like it is in random things SpectreKey dimension
        DimensionType type = world.registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).get(dimTypeKey);
        return type != null && type.equalTo(world.dimensionType());
    }

}
