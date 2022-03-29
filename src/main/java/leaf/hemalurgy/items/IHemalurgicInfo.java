/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.hemalurgy.items;

import com.google.common.collect.Multimap;
import com.legobmw99.allomancy.api.enums.Metal;
import leaf.hemalurgy.constants.Constants;
import leaf.hemalurgy.utils.CompoundNBTHelper;
import leaf.hemalurgy.utils.MetalHelper;
import leaf.hemalurgy.utils.StackNBTHelper;
import leaf.hemalurgy.utils.TextHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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

    default void stealFromSpiritweb(ItemStack stack, Metal spikeMetalType, LivingEntity entityKilled)
    {
        CompoundTag hemalurgyInfo = getHemalurgicInfo(stack);

        //Steals non-manifestation based abilities. traits inherent to an entity?
        switch (spikeMetalType)
        {
            case IRON:
                //steals physical strength
                double strengthCurrent = CompoundNBTHelper.getDouble(hemalurgyInfo, spikeMetalType.name(), 0);
                //don't steal modified values, only base value
                //todo decide how much strength is reasonable to steal and how much goes to waste
                //currently will try 70%
                double strengthToAdd = entityKilled.getAttributes().getBaseValue(Attributes.ATTACK_DAMAGE) * 0.7D;

                Invest(stack, MetalHelper.GetPowerName(spikeMetalType), strengthCurrent + strengthToAdd, entityKilled.getUUID());
                return;
            case TIN:
                //Steals senses
                //todo figure out what that means in minecraft
                break;
            case COPPER:
                //Steals mental fortitude, memory, and intelligence
                //todo increase base xp gain?
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
                        Invest(stack, MetalHelper.GetPowerName(spikeMetalType, metal.get()), 10, entityKilled.getUUID());
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
                        Invest(stack, MetalHelper.GetPowerName(spikeMetalType, metal.get()), 10, entityKilled.getUUID());
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
                MetalHelper.GetPowerName(metalType),
                0);

        switch (metalType)
        {
            case IRON:
                attributeModifiers.put(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                hemalurgicIdentity,
                                "Hemalurgic " + metalType.name(),
                                strength,
                                AttributeModifier.Operation.ADDITION));

                break;
            case TIN:
                //Steals senses
                //todo figure out what that means in minecraft
                break;
            case ZINC:
                //Steals emotional fortitude
                //todo figure out what that means
                break;
            case COPPER:
                //Steals mental fortitude, memory, and intelligence
                //todo increase base xp gain?
                break;
            case CHROMIUM:
                //Might steal destiny
                //so we could add some permanent luck?
                break;
            case NICROSIL:
                //Steals Investiture
                //todo figure out what that means
                break;
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

        if (hemalurgicSpikeItem.getMetalType() == Metal.IRON)
        {
            double attackDamage = CompoundNBTHelper.getDouble(hemalurgicSpikeItem.getHemalurgicInfo(stack), MetalHelper.GetPowerName(Metal.IRON), 0);

            //todo, make this translated text
            if (attackDamage > 0)
            {
                tooltip.add(TextHelper.createText("+" + attackDamage + " Attack Damage"));
            }
        }


        Collection<Metal> hemalurgyStealWhitelist = MetalHelper.getHemalurgyStealWhitelist(hemalurgicSpikeItem.getMetalType());

        if (hemalurgyStealWhitelist != null)
        {
            for (Metal stealType : hemalurgyStealWhitelist)
            {
                // if this spike has that power
                if (hasHemalurgicPower(stack, hemalurgicSpikeItem.getMetalType(), stealType))
                {
                    //then grant it
                    tooltip.add(TextHelper.createTranslatedText("tooltip.hemalurgy." + MetalHelper.GetPowerName(hemalurgicSpikeItem.getMetalType(), stealType)));
                }
            }
        }
        else
        {
            tooltip.add(TextHelper.createTranslatedText("tooltip.hemalurgy." + MetalHelper.GetPowerName(hemalurgicSpikeItem.getMetalType())));
        }

    }

    default boolean hasHemalurgicPower(ItemStack stack, Metal spikeMetal, Metal stealType)
    {
        return CompoundNBTHelper.getDouble(getHemalurgicInfo(stack), MetalHelper.GetPowerName(spikeMetal, stealType), 0) > 0;
    }

    default void Invest(ItemStack stack, String power, double level, UUID identity)
    {
        CompoundTag spikeInfo = getHemalurgicInfo(stack);
        CompoundNBTHelper.setDouble(spikeInfo, power, (double) Math.round(level * 100) / 100);//round off, so we don't get things like 1.1233333337
        StackNBTHelper.setUuid(stack, stolen_identity_tag, identity);
    }
}

