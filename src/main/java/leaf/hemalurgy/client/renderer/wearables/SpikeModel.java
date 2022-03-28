/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to TheIllusiveC4 and their mod Curios for their example of rendering an extra model on the player.
 * https://github.com/TheIllusiveC4/Curios
 *
 * Eventually this script will change to take in where exactly the user is spiked.
 * https://coppermind.net/w/images/Hemalurgy_table.jpg
 *
 */

package leaf.hemalurgy.client.renderer.wearables;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nonnull;

public class SpikeModel extends HumanoidModel<LivingEntity>
{

    public SpikeModel(ModelPart part)
    {
        super(part);
    }


    public static LayerDefinition createLayer() {
        CubeDeformation cube = new CubeDeformation(0.4F);
        MeshDefinition mesh = HumanoidModel.createMesh(cube, 0.0F);
        PartDefinition part = mesh.getRoot();
        part.addOrReplaceChild("right_arm",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 9.0F, -2.0F, 2, 1, 4, cube),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -5.0F, 2.0F, 0.0F));
        part.addOrReplaceChild("left_arm",
                CubeListBuilder.create().mirror().texOffs(0, 0).addBox(1.0F, 9.0F, -2.0F, 2, 1, 4, cube),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 5.0F, 2.0F, 0.0F));
        return LayerDefinition.create(mesh, 16, 16);
    }

    @Override
    @Nonnull
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    @Nonnull
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList
                .of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg, this.hat);
    }
}
