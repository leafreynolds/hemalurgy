/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.hemalurgy.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.storage.LevelData;

public class TeleportHelper
{
    public static void teleportEntity(Entity entity, ServerLevel destinationDimension, double x, double y, double z, float yRot, float xRot)
    {
        if (entity == null || entity.level.isClientSide || !entity.canChangeDimensions())
        {
            return;
        }

        ServerLevel currentDimension = entity.getServer().getLevel(entity.getCommandSenderWorld().dimension());


        boolean isChangingDimension = !currentDimension.dimension().location().equals(destinationDimension.dimension().location());
        final boolean entityIsPlayer = entity instanceof ServerPlayer;
        final ServerPlayer serverPlayerEntity = entityIsPlayer ? (ServerPlayer) entity : null;

        if (isChangingDimension && !entity.canChangeDimensions())
        {
            //early exit
            return;
        }

        //no passengers allowed.
        if (entity.isPassenger())
        {
            entity.getRootVehicle().unRide();
        }


        //This section is mostly copied and then modified from TeleportCommand.performTeleport()
        if (entityIsPlayer)
        {
            ChunkPos chunkPos = new ChunkPos(new BlockPos(x, y, z));
            destinationDimension.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkPos, 1, entity.getId());
            entity.stopRiding();
            if (serverPlayerEntity.isSleeping())
            {
                serverPlayerEntity.stopSleepInBed(true, true);
            }

            if (isChangingDimension)
            {
                serverPlayerEntity.teleportTo(destinationDimension, x, y, z, yRot, xRot);
            }
            else
            {
                serverPlayerEntity.connection.teleport(x, y, z, yRot, xRot);
            }

            serverPlayerEntity.setYHeadRot(yRot);

            //restore stuff. Annoyingly it doesn't happen automatically
            for (MobEffectInstance effectinstance : serverPlayerEntity.getActiveEffects())
            {
                serverPlayerEntity.connection.send(new ClientboundUpdateMobEffectPacket(serverPlayerEntity.getId(), effectinstance));
            }

            LevelData worldInfo = serverPlayerEntity.level.getLevelData();
            //I'd always wondered what the deal was with xp not showing properly.
            serverPlayerEntity.connection.send(new ClientboundPlayerAbilitiesPacket(serverPlayerEntity.getAbilities()));
            serverPlayerEntity.connection.send(new ClientboundChangeDifficultyPacket(worldInfo.getDifficulty(), worldInfo.isDifficultyLocked()));
            serverPlayerEntity.connection.send(new ClientboundSetExperiencePacket(serverPlayerEntity.experienceProgress, serverPlayerEntity.totalExperience, serverPlayerEntity.experienceLevel));
        }
        else
        {
            float entityYRot = Mth.wrapDegrees(yRot);
            float entityXRot = Mth.wrapDegrees(xRot);
            entityXRot = Mth.clamp(entityXRot, -90.0F, 90.0F);
            if (isChangingDimension)
            {
                Entity originalEntity = entity;
                entity = originalEntity.getType().create(destinationDimension);
                if (entity == null)
                {
                    //error
                    LogHelper.error("Was unable to create an entity when trying to teleport it.");
                    return;
                }
                entity.restoreFrom(originalEntity);
                entity.moveTo(x, y, z, entityYRot, entityXRot);
                entity.setYHeadRot(entityYRot);

                currentDimension.removeEntity(originalEntity);

                destinationDimension.addFreshEntity(entity);

                currentDimension.resetEmptyTime();
                destinationDimension.resetEmptyTime();
            }
            else
            {
                entity.moveTo(x, y, z, entityYRot, entityXRot);
                entity.setYHeadRot(entityYRot);
            }
        }

        //not sure if I care about elytra, SO todo decide later. might be fun to let them keep flying?
        if (!(entity instanceof LivingEntity) || !((LivingEntity) entity).isFallFlying())
        {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
            entity.setOnGround(true);
        }

        if (entity instanceof PathfinderMob)
        {
            ((PathfinderMob) entity).getNavigation().stop();
        }
    }
}
