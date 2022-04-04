/*
 * File created ~ 28 - 3 - 2022 ~Leaf
 */

package leaf.hemalurgy.utils;

import com.legobmw99.allomancy.api.enums.Metal;
import leaf.hemalurgy.registry.AttributesRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;

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
            case TIN:
            case PEWTER:
            case COPPER:
            case BRONZE:
            case CADMIUM:
            case ELECTRUM:
            case BRASS:
            case BENDALLOY:
            case GOLD:
                return true;
            case ZINC:
            case ALUMINUM:
            case DURALUMIN:
            case CHROMIUM:
            case NICROSIL:
            default:
                return false;
        }
    }

    public static double getEntityAbilityStrength(LivingEntity killedEntity, Player playerEntity, Metal metalSpikeType)
    {
        //Steals non-manifestation based abilities. traits inherent to an entity?
        double strengthToAdd = 0;
        switch (metalSpikeType)
        {
            case IRON:
                //steals physical strength
                //don't steal modified values, only base value
                //todo decide how much strength is reasonable to steal and how much goes to waste
                //currently will try 70%
                strengthToAdd = killedEntity.getAttributes().getBaseValue(Attributes.ATTACK_DAMAGE) * 0.7D;

                break;
            case TIN:
                //Steals senses

                //can track you on a huge island, even underground.
                if (killedEntity instanceof EnderDragon)
                {
                    strengthToAdd = 0.77;
                }
                //Literally exist to hunt players at night
                else if (killedEntity instanceof Phantom)
                {
                    strengthToAdd = 0.66;
                }
                else if (killedEntity instanceof Player)
                {
                    strengthToAdd = 0.25;
                }
                else
                {
                    final AttributeInstance attribute = killedEntity.getAttribute(Attributes.FOLLOW_RANGE);

                    if (attribute != null)
                    {
                        //ghasts have 100 base follow range,
                        //most others have 16-40
                        final int hemalurgicDecay = 150;
                        strengthToAdd = attribute.getBaseValue() / hemalurgicDecay;
                    }
                    else
                    {
                        //at this point, who cares.
                        strengthToAdd =  0.10;
                    }
                }
                break;
            case COPPER:
                //Steals mental fortitude, memory, and intelligence
                //increase base xp gain rate
                final float potentialRewardRate = killedEntity.getExperienceReward(playerEntity) / 150f;

                if (killedEntity instanceof Player)
                {
                    final AttributeInstance attribute = killedEntity.getAttribute(AttributesRegistry.COSMERE_ATTRIBUTES.get(Metal.COPPER.getName()).get());
                    if (attribute != null)
                    {
                        //70% strength to spike
                        strengthToAdd = attribute.getValue() * 0.70;
                        //25% remaining on player
                        final double newBaseValue = attribute.getValue() * 0.25;
                        attribute.setBaseValue(((int)(newBaseValue * 100)) / 100f);
                    }
                    else
                    {
                        strengthToAdd = potentialRewardRate;
                    }
                }
                else if (killedEntity instanceof EnderDragon)
                {
                    EnderDragon dragonEntity = (EnderDragon) killedEntity;
                    //dragon doesn't reward xp in a typical way
                    strengthToAdd =
                            dragonEntity.getDragonFight() != null && !dragonEntity.getDragonFight().hasPreviouslyKilledDragon()
                            ? 1 //give first person to kill dragon a full rate increase spike
                            : 0.33;//else similar to wither rate.
                }
                else
                {
                    strengthToAdd = potentialRewardRate;
                }
                break;
            case ZINC:
                //Steals emotional fortitude
                //todo figure out what that means
                break;
            case ALUMINUM:
                //Removes all powers
                //... ooops?
                //maybe its an item you can equip on others that they then have to remove?
                break;
            case DURALUMIN:
                //Steals Connection/Identity
                break;
            case CHROMIUM:
                //Might steal destiny
                //so we could add some permanent luck?
                break;
            case NICROSIL:
                //Steals Investiture
                break;
        }
        return strengthToAdd;
    }

    public static String getPowerName(Metal spikeMetal)
    {
        return getPowerName(spikeMetal,spikeMetal);
    }

    public static String getPowerName(Metal spikeMetal, Metal powerMetal)
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
