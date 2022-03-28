/*
 * File created ~ 28 - 3 - 2022 ~Leaf
 */

package leaf.hemalurgy.utils;

import com.legobmw99.allomancy.api.enums.Metal;

public class MetalHelper
{

    public static boolean isMetalSpikeAvailable(Metal metal)
    {
        switch (metal)
        {
            case IRON:
            case STEEL:
            case BRONZE:
            case CADMIUM:
            case ELECTRUM:
            case PEWTER:
            case BRASS:
            case BENDALLOY:
            case GOLD:
                return true;
        }
        return false;
    }

    public static String GetPowerName(Metal spikeMetal)
    {
        return GetPowerName(spikeMetal,spikeMetal);
    }

    public static String GetPowerName(Metal spikeMetal, Metal powerMetal)
    {
        switch (spikeMetal)
        {
            //steals allomantic abilities
            case STEEL:
            case BRONZE:
            case CADMIUM:
            case ELECTRUM:
                return "allomantic_" + powerMetal.getName();
            //steals feruchemical abilities
            case PEWTER:
            case BRASS:
            case BENDALLOY:
            case GOLD:
                return "feruchemical_" + powerMetal.getName();
            default:
                return spikeMetal.getName();
        }
    }
}
