/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.hemalurgy.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.legobmw99.allomancy.api.enums.Metal;
import leaf.hemalurgy.Hemalurgy;
import leaf.hemalurgy.compat.curios.CuriosCompat;
import leaf.hemalurgy.compat.curios.HemalurgyCurios;
import leaf.hemalurgy.constants.Constants;
import leaf.hemalurgy.properties.PropTypes;
import leaf.hemalurgy.utils.LogHelper;
import leaf.hemalurgy.utils.MetalHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = Hemalurgy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HemalurgicSpikeItem extends BaseItem implements IHasMetalType, IHemalurgicInfo, ICurioItem
{
    private final Metal metalType;
    private final float attackDamage;
    /**
     * Modifiers applied when the item is in the mainhand of a user. copied from sword item
     */
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;


    public static final DamageSource SPIKED = (new DamageSource("spiked")).bypassArmor().bypassMagic();

    //todo move
    private static final ResourceLocation SPIKE_TEXTURE = new ResourceLocation("hemalurgy", "textures/block/metal_block.png");
    private Object model;

    public HemalurgicSpikeItem(Metal metalType)
    {
        super(PropTypes.Items.ONE.get().rarity(Rarity.COMMON).tab(HemalurgyItemGroups.ITEMS));
        this.metalType = metalType;

        //todo decide on damage
        this.attackDamage = 2f + 1f;//tier.getAttackDamage();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double) this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double) -2.4f, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }


    @Override
    public Metal getMetalType()
    {
        return metalType;
    }

    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> stacks)
    {
        if (allowdedIn(tab))
        {
            ItemStack stack = new ItemStack(this);
            stacks.add(stack);

            //if (getMetalType().hasFeruchemicalEffect())
            {

                //what powers can this metal type contain

                if (this.getMetalType() == Metal.IRON)
                {
                    ItemStack filledIronSpike = new ItemStack(this);
                    //steals physical strength
                    //don't steal modified values, only base value
                    //todo decide how much strength is reasonable to steal and how much goes to waste
                    //currently will try 70%
                    double strengthToAdd = 15 * 0.7D;// Iron golems have the most base attack damage of normal mods (giants have 50??). Ravagers have

                    Invest(filledIronSpike, MetalHelper.getPowerName(this.getMetalType()), strengthToAdd, UUID.randomUUID());

                    stacks.add(filledIronSpike);
                }
                else if (this.getMetalType() == Metal.TIN)
                {
                    ItemStack filledIronSpike = new ItemStack(this);
                    Invest(filledIronSpike, MetalHelper.getPowerName(this.getMetalType()), 0.33f, UUID.randomUUID());
                    stacks.add(filledIronSpike);
                }

                Collection<Metal> hemalurgyStealWhitelist = MetalHelper.getHemalurgyStealWhitelist(getMetalType());
                if (hemalurgyStealWhitelist != null)
                {
                    for (Metal stealType : hemalurgyStealWhitelist)
                    {
                        try
                        {
                            ItemStack spike = new ItemStack(this);
                            Invest(spike, MetalHelper.getPowerName(this.getMetalType(), stealType), 10, UUID.randomUUID());
                            stacks.add(spike);
                        }
                        catch (Exception e)
                        {
                            LogHelper.info(String.format("remove %s from whitelist for %s spikes", stealType.toString(), getMetalType()));
                        }
                    }
                }
            }

        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level worldIn, java.util.List<Component> tooltip, TooltipFlag flagIn)
    {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        // no extra info if there isn't any
        if (getHemalurgicIdentity(stack) == null)
        {
            return;
        }


        //stolen identities listed?

        //extra investiture powers added
        addInvestitureInformation(stack, this, tooltip);
    }

    public void killedEntity(ItemStack stack, LivingEntity entityKilled)
    {
        //https://wob.coppermind.net/events/332/#e9569

        // do nothing if an identity exists and doesn't match
        if (!matchHemalurgicIdentity(stack, entityKilled.getUUID()))
        {
            return;
        }

        // ensure we set the stolen identity
        stealFromSpiritweb(stack, getMetalType(), entityKilled);
    }

    @Override
    public boolean isFoil(@Nonnull ItemStack stack)
    {
        return super.isFoil(stack) || hemalurgicIdentityExists(stack);
    }

    /**
     * Gets a map of item attribute modifiers, used by damage when used as melee weapon.
     */
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot)
    {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.attributeModifiers
                                                       : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack)
    {
        Multimap<Attribute, AttributeModifier> attributeModifiers = LinkedHashMultimap.create();

        //add hemalurgic attributes, if any.
        return ((IHemalurgicInfo) (stack.getItem())).getHemalurgicAttributes(attributeModifiers, stack, metalType);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack)
    {
        //do not allow players to wear two spikes of the same metal empowered by the same killed entity UUID
        if (slotContext.entity() instanceof Player)
        {
            Player player = (Player)slotContext.entity();
            final UUID stackWeWantToEquipUUID = getHemalurgicIdentity(stack);

            if (stackWeWantToEquipUUID != null)
            {
                Predicate<ItemStack> spikePredicate = stackToFind ->
                {
                    final boolean isSpike = stackToFind.getItem() instanceof HemalurgicSpikeItem;
                    final HemalurgicSpikeItem hemalurgicSpikeItem = (HemalurgicSpikeItem) stackToFind.getItem();
                    final UUID foundSpikeUUID = getHemalurgicIdentity(stackToFind);
                    return isSpike
                            && hemalurgicSpikeItem.getMetalType() == metalType
                            && foundSpikeUUID != null
                            && foundSpikeUUID.compareTo(stackWeWantToEquipUUID) == 0;
                };
                final Optional<ImmutableTriple<String, Integer, ItemStack>> curioSpike = CuriosApi.getCuriosHelper().findEquippedCurio(spikePredicate, player);
                return curioSpike.isEmpty();
            }
        }
        return true;
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity)
    {
        //can't equip in armor slots
        return false;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack)
    {
        //has to be a conscious decision to stab yourself
        return true;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack)
    {
        //todo better logic.
        boolean isEquipping = prevStack == null || stack.getItem() != prevStack.getItem();

        if (isEquipping)
        {
            //then do hemalurgy spike logic
            //hurt the user
            //spiritweb attributes are handled in metalmind
            slotContext.getWearer().hurt(SPIKED, 4);

            for (Metal stealType : Metal.values())
            {
                if (hasHemalurgicPower(stack, this.getMetalType(), stealType))
                {

                    switch (this.getMetalType())
                    {
                        //steals allomantic abilities
                        case STEEL:
                        case BRONZE:
                        case CADMIUM:
                        case ELECTRUM:
                            slotContext.entity().getCapability(com.legobmw99.allomancy.modules.powers.data.AllomancerCapability.PLAYER_CAP).ifPresent((iAllomancerData) ->
                            {
                                iAllomancerData.addPower(stealType);
                                com.legobmw99.allomancy.network.Network.sync((Player) slotContext.entity());
                            });
                            break;
                        //steals feruchemical abilities
                        case PEWTER:
                        case BRASS:
                        case BENDALLOY:
                        case GOLD:
                            slotContext.entity().getCapability(com.example.feruchemy.caps.FeruchemyCapability.FERUCHEMY_CAP).ifPresent((iFeruchemistData) ->
                            {
                                iFeruchemistData.addPower(stealType);
                                com.example.feruchemy.network.NetworkUtil.sync((Player) slotContext.entity());
                            });
                            break;
                    }
                }
            }
        }

    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack)
    {
        slotContext.getWearer().hurt(SPIKED, 4);

        for (Metal stealType : Metal.values())
        {
            if (hasHemalurgicPower(stack, this.getMetalType(), stealType))
            {
                switch (this.getMetalType())
                {
                    //steals allomantic abilities
                    case STEEL:
                    case BRONZE:
                    case CADMIUM:
                    case ELECTRUM:
                        slotContext.entity().getCapability(com.legobmw99.allomancy.modules.powers.data.AllomancerCapability.PLAYER_CAP).ifPresent((iAllomancerData) ->
                        {
                            iAllomancerData.revokePower(stealType);
                            com.legobmw99.allomancy.network.Network.sync((Player) slotContext.entity());
                        });
                        break;
                    //steals feruchemical abilities
                    case PEWTER:
                    case BRASS:
                    case BENDALLOY:
                    case GOLD:
                        slotContext.entity().getCapability(com.example.feruchemy.caps.FeruchemyCapability.FERUCHEMY_CAP).ifPresent((iFeruchemistData) ->
                        {
                            iFeruchemistData.revokePower(stealType);
                            com.example.feruchemy.network.NetworkUtil.sync((Player) slotContext.entity());
                        });
                        break;
                }
            }
        }
    }
}
