/*
 * File created ~ 28 - 3 - 2022 ~Leaf
 */

package leaf.hemalurgy.utils;

import com.legobmw99.allomancy.api.enums.Metal;

import java.util.Arrays;
import java.util.Collection;

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


    public static Collection<Metal> getHemalurgyStealWhitelist(Metal spikeMetal)
    {
        return switch (spikeMetal)
                {
                    //Steals a physical allomantic power
                    //Steals a physical feruchemical power
                    //Iron//Steel//Tin//Pewter
                    case STEEL, PEWTER -> Arrays.asList(
                            Metal.IRON,
                            Metal.STEEL,
                            Metal.TIN,
                            Metal.PEWTER);
                    //Steals a Mental Allomantic power
                    //Steals a cognitive feruchemical power
                    //Zinc//Brass//Copper//Bronze
                    case BRASS, BRONZE -> Arrays.asList(
                            Metal.ZINC,
                            Metal.BRASS,
                            Metal.COPPER,
                            Metal.BRONZE);
                    //Steals a Temporal Allomantic power
                    //Steals a Hybrid Feruchemical power
                    //Cadmium//Bendalloy//Gold//Electrum
                    case CADMIUM, GOLD -> Arrays.asList(
                            Metal.CADMIUM,
                            Metal.BENDALLOY,
                            Metal.GOLD,
                            Metal.ELECTRUM);
                    //Steals an Enhancement Allomantic power
                    //Steals a Spiritual Feruchemical power
                    //Chromium//Nicrosil//Aluminum//Duralumin
                    case BENDALLOY, ELECTRUM -> Arrays.asList(
                            Metal.CHROMIUM,
                            Metal.NICROSIL,
                            Metal.ALUMINUM,
                            Metal.DURALUMIN);
                    default -> null;
                };
    }
}
