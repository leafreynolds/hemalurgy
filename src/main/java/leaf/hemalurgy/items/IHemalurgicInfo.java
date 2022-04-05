/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.hemalurgy.items;

import com.google.common.collect.Multimap;
import com.legobmw99.allomancy.api.enums.Metal;
import leaf.hemalurgy.constants.Constants;
import leaf.hemalurgy.registry.AttributesRegistry;
import leaf.hemalurgy.utils.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;


public interface IHemalurgicInfo
{
    String stolen_identity_tag = "stolen_identity_tag";
    List<Metal> whiteList = new ArrayList<Metal>(4);

    default boolean matchHemalurgicIdentity(ItemStack stack, UUID uniqueID)
    {
        if (!hemalurgicIdentityExists(stack))
        {
            return true;
        }

        return StackNBTHelper.getUuid(stack, stolen_identity_tag).compareTo(uniqueID) == 0;
    }

    default boolean hemalurgicIdentityExists(ItemStack stack)
    {
        return StackNBTHelper.verifyExistance(stack, stolen_identity_tag);
    }

    default UUID getHemalurgicIdentity(ItemStack stack)
    {
        return StackNBTHelper.getUuid(stack, stolen_identity_tag);
    }

    default CompoundTag getHemalurgicInfo(ItemStack stack)
    {
        return stack.getOrCreateTagElement("hemalurgy");
    }

    default void stealFromSpiritweb(ItemStack stack, Metal spikeMetalType, Player playerEntity, LivingEntity entityKilled)
    {
        CompoundTag hemalurgyInfo = getHemalurgicInfo(stack);
        //Steals non-manifestation based abilities. traits inherent to an entity?
        switch (spikeMetalType)
        {
            case IRON:
            case TIN:
            case COPPER:
            case ZINC:
            case ALUMINUM:
            case DURALUMIN:
            case CHROMIUM:
            case NICROSIL:

            //Non-Manifestation based hemalurgy all comes here
                //How much is already stored? (like koloss spikes could keep storing strength on the same spike)
                final double strengthCurrent = CompoundNBTHelper.getDouble(hemalurgyInfo, spikeMetalType.name(), 0);
                //how much should we add.
                final double entityAbilityStrength = MetalHelper.getEntityAbilityStrength(entityKilled, playerEntity, spikeMetalType);
                final double strengthToAdd = strengthCurrent + entityAbilityStrength;
                if (strengthToAdd > 0.01 || strengthToAdd < -0.01)
                {
                    Invest(stack, MetalHelper.getPowerName(spikeMetalType), strengthToAdd, entityKilled.getUUID());
                }
                return;
        }

        List<Metal> allomancerPowersFound = new ArrayList<>();
        entityKilled.getCapability(com.legobmw99.allomancy.modules.powers.data.AllomancerCapability.PLAYER_CAP).ifPresent((iAllomancerData) ->
        {
            for (Metal metalType : Metal.values())
            {
                if (iAllomancerData.hasPower(metalType))
                {
                    allomancerPowersFound.add(metalType);
                }
            }
        });
        List<Metal> feruchemistPowersFound = new ArrayList<>();
        entityKilled.getCapability(com.example.feruchemy.caps.FeruchemyCapability.FERUCHEMY_CAP).ifPresent((iFeruchemistData) ->
        {
            for (Metal metalType : Metal.values())
            {
                if (iFeruchemistData.hasPower(metalType))
                {
                    feruchemistPowersFound.add(metalType);
                }
            }
        });
        whiteList.clear();

        //The type of thing you can steal is dependant on the type of metal.
        Collection<Metal> hemalurgyStealWhitelist = MetalHelper.getHemalurgyStealWhitelist(spikeMetalType);
        if (hemalurgyStealWhitelist != null)
        {
            whiteList.addAll(hemalurgyStealWhitelist);
        }

        switch (spikeMetalType)
        {
            //steals allomantic abilities
            case STEEL, BRONZE, CADMIUM, ELECTRUM -> {

                if (allomancerPowersFound.size() > 0)
                {
                    Optional<Metal> metal = getRandomMetalPowerFromList(allomancerPowersFound, whiteList);
                    if (metal.isPresent())
                    {
                        Invest(stack, MetalHelper.getPowerName(spikeMetalType, metal.get()), 10, entityKilled.getUUID());
                        entityKilled.getCapability(com.legobmw99.allomancy.modules.powers.data.AllomancerCapability.PLAYER_CAP).ifPresent((iAllomancerData) ->
                        {
                            iAllomancerData.revokePower(metal.get());
                            com.legobmw99.allomancy.network.Network.sync((Player) entityKilled);
                        });
                    }
                }
            }
            //steals feruchemical abilities
            case PEWTER, BRASS, BENDALLOY, GOLD -> {

                if (feruchemistPowersFound.size() > 0)
                {
                    Optional<Metal> metal = getRandomMetalPowerFromList(feruchemistPowersFound, whiteList);
                    if (metal.isPresent())
                    {
                        Invest(stack, MetalHelper.getPowerName(spikeMetalType, metal.get()), 10, entityKilled.getUUID());
                        entityKilled.getCapability(com.example.feruchemy.caps.FeruchemyCapability.FERUCHEMY_CAP).ifPresent((iFeruchemistData) ->
                        {
                            iFeruchemistData.revokePower(metal.get());
                            com.example.feruchemy.network.NetworkUtil.sync((Player) entityKilled);
                        });
                    }
                }
            }
        }
    }


    default Optional<Metal> getRandomMetalPowerFromList(
            List<Metal> manifestationsFound,
            List<Metal> whiteList)
    {
        Collections.shuffle(whiteList);

        //then check the entity has those types to steal
        for (Metal typeToTrySteal : whiteList)
        {
            int i = manifestationsFound.indexOf(typeToTrySteal);
            //if it exists in the list
            if (i >= 0)
            {
                //then we've found something to steal!
                return Optional.ofNullable(manifestationsFound.get(i));
            }
        }

        return Optional.empty();
    }

    default Multimap<Attribute, AttributeModifier> getHemalurgicAttributes(Multimap<Attribute, AttributeModifier> attributeModifiers, ItemStack stack, Metal metalType)
    {
        CompoundTag hemalurgyInfo = getHemalurgicInfo(stack);
        UUID hemalurgicIdentity = getHemalurgicIdentity(stack);

        if (hemalurgicIdentity == null)
        {
            return attributeModifiers;
        }

        final double strength = CompoundNBTHelper.getDouble(
                hemalurgyInfo,
                MetalHelper.getPowerName(metalType),
                0);

        Attribute attribute = null;
        AttributeModifier.Operation attributeModifier = AttributeModifier.Operation.ADDITION;

        switch (metalType)
        {
            case IRON:
                attribute = Attributes.ATTACK_DAMAGE;
                break;
            case CHROMIUM:
                //Might steal destiny
                //so we could add some permanent luck?
                attribute = Attributes.LUCK;
                break;
            default:
                //TIN:
                //Steals senses
                //a type of night vision

                //Copper:
                //Steals mental fortitude, memory, and intelligence


                final RegistryObject<Attribute> attributeRegistryObject = AttributesRegistry.COSMERE_ATTRIBUTES.get(metalType.getName());
                if (attributeRegistryObject != null && attributeRegistryObject.isPresent())
                {
                    attribute = attributeRegistryObject.get();
                }
                break;
            /*
            case ZINC:
                //Steals emotional fortitude
                //todo figure out what that means
                break;
            case NICROSIL:
                //Steals Investiture
                //todo figure out what that means
                break;*/
        }

        if (attribute != null)
        {
            attributeModifiers.put(
                    attribute,
                    new AttributeModifier(
                            hemalurgicIdentity,
                            "Hemalurgic " + metalType.getName(),
                            strength,
                            attributeModifier));
        }

        return attributeModifiers;
    }


    default void addInvestitureInformation(ItemStack stack, HemalurgicSpikeItem hemalurgicSpikeItem, List<Component> tooltip)
    {
        if (!hemalurgicIdentityExists(stack))
        {
            return;
        }

        tooltip.add(TextHelper.createTranslatedText(Constants.StringKeys.CONTAINED_POWERS_FOUND));

        Collection<Metal> hemalurgyStealWhitelist = MetalHelper.getHemalurgyStealWhitelist(hemalurgicSpikeItem.getMetalType());

        if (hemalurgyStealWhitelist != null)
        {
            for (Metal stealType : hemalurgyStealWhitelist)
            {
                // if this spike has that power
                if (hasHemalurgicPower(stack, hemalurgicSpikeItem.getMetalType(), stealType))
                {
                    //then grant it
                    tooltip.add(TextHelper.createTranslatedText("tooltip.hemalurgy." + MetalHelper.getPowerName(hemalurgicSpikeItem.getMetalType(), stealType)));
                }
            }
        }
        else
        {
            tooltip.add(TextHelper.createTranslatedText("tooltip.hemalurgy." + MetalHelper.getPowerName(hemalurgicSpikeItem.getMetalType())));
        }

    }

    default boolean hasHemalurgicPower(ItemStack stack, Metal spikeMetal, Metal stealType)
    {
        final double aDouble = CompoundNBTHelper.getDouble(getHemalurgicInfo(stack), MetalHelper.getPowerName(spikeMetal, stealType), 0);
        final double marginOfError = 0.01;
        return aDouble > marginOfError || aDouble < -marginOfError;
    }

    default void Invest(ItemStack stack, String power, double level, UUID identity)
    {
        CompoundTag spikeInfo = getHemalurgicInfo(stack);
        CompoundNBTHelper.setDouble(spikeInfo, power, (double) Math.round(level * 100) / 100);//round off, so we don't get things like 1.1233333337
        StackNBTHelper.setUuid(stack, stolen_identity_tag, identity);
    }
}

