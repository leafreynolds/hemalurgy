/*
 * File created ~ 1 - 7 - 2022 ~Leaf
 */

package leaf.hemalurgy.capability.entity;

import com.legobmw99.allomancy.api.enums.Metal;
import leaf.hemalurgy.api.ISpiritweb;
import leaf.hemalurgy.network.Network;
import leaf.hemalurgy.network.packets.SyncPlayerSpiritwebMessage;
import leaf.hemalurgy.registry.AttributesRegistry;
import leaf.hemalurgy.utils.CompoundNBTHelper;
import leaf.hemalurgy.utils.ResourceLocationHelper;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/*
    "The actual outlet of the power is not chosen by the practitioner, but instead is hardwritten into their Spiritweb"
    -Khriss
    https://coppermind.net/wiki/Ars_Arcanum#The_Alloy_of_Law
 */

public class SpiritwebCapability implements ISpiritweb
{

	public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
		Entity eventEntity = event.getObject();

		if (!(eventEntity instanceof Player))
		{
			return;
		}


		LivingEntity livingEntity = (LivingEntity) eventEntity;
		event.addCapability(ResourceLocationHelper.prefix("spiritweb"), new ICapabilitySerializable<CompoundTag>()
		{
			final SpiritwebCapability spiritweb = new SpiritwebCapability(livingEntity);
			final LazyOptional<ISpiritweb> spiritwebInstance = LazyOptional.of(() -> spiritweb);

			@Nonnull
			@Override
			public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side)
			{
				return cap == SpiritwebCapability.CAPABILITY ? (LazyOptional<T>) spiritwebInstance
				                                             : LazyOptional.empty();
			}

			@Override
			public CompoundTag serializeNBT()
			{
				return spiritweb.serializeNBT();
			}

			@Override
			public void deserializeNBT(CompoundTag nbt)
			{
				spiritweb.deserializeNBT(nbt);
			}
		});
	}


	//Injection
	public static final Capability<ISpiritweb> CAPABILITY = CapabilityManager.get(new CapabilityToken<ISpiritweb>()
	{
	});

	//detect if capability has been set up yet
	private boolean didSetup = false;

	private final LivingEntity livingEntity;
	private CompoundTag nbt;
	public SpiritwebCapability(LivingEntity ent)
	{
		this.livingEntity = ent;
		nbt = new CompoundTag();
	}


	@Nonnull
	public static LazyOptional<ISpiritweb> get(LivingEntity entity)
	{
		return entity != null ? entity.getCapability(SpiritwebCapability.CAPABILITY, null)
		                      : LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT()
	{
		return this.nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		this.nbt = nbt;
	}

	@Override
	public void tick()
	{
		//if server
		if (!livingEntity.level.isClientSide)
		{
			//Login sync
			if (!didSetup)
			{
				syncToClients(null);
				didSetup = true;
			}
		}
	}


	@Override
	public LivingEntity getLiving()
	{
		return livingEntity;
	}


	//Copy things from an old spiritweb into the new one.
	//Eg a player has died and we need to make sure they get their stormlight and breaths back.
	@Override
	public void transferFrom(ISpiritweb oldSpiritWeb)
	{
		var oldWeb = (SpiritwebCapability) oldSpiritWeb;
		this.nbt = oldWeb.nbt;

		for (Metal metalType : Metal.values())
		{
			//check for others
			final RegistryObject<Attribute> metalRelatedAttribute = AttributesRegistry.COSMERE_ATTRIBUTES.get(metalType.getName());
			if (metalRelatedAttribute != null && metalRelatedAttribute.isPresent())
			{
				AttributeInstance oldPlayerAttribute = oldSpiritWeb.getLiving().getAttribute(metalRelatedAttribute.get());
				AttributeInstance newPlayerAttribute = getLiving().getAttribute(metalRelatedAttribute.get());

				if (newPlayerAttribute != null && oldPlayerAttribute != null)
				{
					newPlayerAttribute.setBaseValue(oldPlayerAttribute.getBaseValue());
				}

			}
		}
	}

	@Override
	public int getEyeHeight()
	{
		return CompoundNBTHelper.getInt(this.nbt,"eye_height", 0);
	}

	@Override
	public void setEyeHeight(int eyeHeight)
	{
		this.nbt.putInt("eye_height", eyeHeight);
	}

	@Override
	public void syncToClients(@Nullable ServerPlayer serverPlayerEntity)
	{
		if (livingEntity != null && livingEntity.level.isClientSide)
		{
			throw new IllegalStateException("Don't sync client -> server");
		}

		CompoundTag nbt = serializeNBT();

		if (serverPlayerEntity == null)
		{
			Network.sendToAllInWorld(new SyncPlayerSpiritwebMessage(this.livingEntity.getId(), nbt), (ServerLevel) livingEntity.level);
		}
		else
		{
			Network.sendTo(new SyncPlayerSpiritwebMessage(this.livingEntity.getId(), nbt), serverPlayerEntity);
		}
	}
}
