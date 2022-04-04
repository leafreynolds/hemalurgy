/*
 * File created ~ 31 - 3 - 2022 ~ Leaf
 */

package leaf.hemalurgy.mixin;

import com.legobmw99.allomancy.api.enums.Metal;
import leaf.hemalurgy.registry.AttributesRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LightTexture.class)
public class LightTextureMixin
{
	@ModifyConstant(method = "updateLightTexture", constant = @Constant(floatValue = 0f, ordinal = 1))
	private float updateLightTextureConstant(float prev)
	{
		//do stuff
		Player clientPlayer = (Player) Minecraft.getInstance().getCameraEntity();
		if (clientPlayer == null)
		{
			return prev;
		}

		AttributeInstance attribute = clientPlayer.getAttribute(AttributesRegistry.COSMERE_ATTRIBUTES.get(Metal.TIN.getName()).get());
		//return modded val
		return attribute != null ? (float) attribute.getValue() : prev;
	}
}
