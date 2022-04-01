package leaf.hemalurgy.mixin;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
/*    @Inject(at = @At("RETURN"), method = "getNightVisionScale", cancellable = true)
    private static void getNightVisionScale(LivingEntity livingEntity, float f, CallbackInfoReturnable<Float> info)
    {

        //int i = livingEntity.getEffect(MobEffects.NIGHT_VISION).getDuration();
        //info.setReturnValue(i > 200 ? 1.0F : 0.7F + Mth.sin(((float) i - f) * (float) Math.PI * 0.2F) * 0.3F);

        MobEffectInstance effect = livingEntity.getEffect(MobEffects.NIGHT_VISION);
        float i = effect != null ? effect.getDuration() : f;
        info.setReturnValue(i > 0 ? 1 : i);
    }*/


}
