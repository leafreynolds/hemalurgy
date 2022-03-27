/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.hemalurgy.compat.patchouli;

import leaf.hemalurgy.utils.LogHelper;
import net.minecraftforge.fml.ModList;

public class PatchouliCompat
{
    private static boolean patchouliModDetected;

    public static boolean PatchouliIsPresent()
    {
        return patchouliModDetected;
    }

    public static void init()
    {
        patchouliModDetected = ModList.get().isLoaded("patchouli");
        LogHelper.info("Patchouli detected, hemalurgy can use it's guide item.");
    }

}
