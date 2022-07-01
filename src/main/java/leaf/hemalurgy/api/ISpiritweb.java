/*
 * File created ~ 1 - 7 - 2022 ~Leaf
 */

package leaf.hemalurgy.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public interface ISpiritweb extends INBTSerializable<CompoundTag>
{
	void tick();

	LivingEntity getLiving();

	void syncToClients(@Nullable ServerPlayer serverPlayerEntity);

	void transferFrom(ISpiritweb oldSpiritWeb);

	int getEyeHeight();
	void setEyeHeight(int eyeHeight);
}
