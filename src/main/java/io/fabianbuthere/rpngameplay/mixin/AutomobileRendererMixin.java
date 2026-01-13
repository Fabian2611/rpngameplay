package io.fabianbuthere.rpngameplay.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.fabianbuthere.rpngameplay.RpnMod;
import io.github.foundationgames.automobility.entity.AutomobileEntity;
import io.github.foundationgames.automobility.entity.render.AutomobileEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AutomobileEntityRenderer.class)
@OnlyIn(Dist.CLIENT)
public class AutomobileRendererMixin extends EntityRenderer<AutomobileEntity> {
    protected AutomobileRendererMixin(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(AutomobileEntity pEntity) {
        return null;
    }

    @Inject(
            method = "render(Lio/github/foundationgames/automobility/entity/AutomobileEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("TAIL"),
            remap = false
    )
    public void rpn$render(AutomobileEntity entity, float yaw, float tickDelta, PoseStack pose, MultiBufferSource buffers, int light, CallbackInfo ci) {
        pose.pushPose();

        double rads = Math.toRadians(yaw);
        double offset = 1;
        double dx = Math.sin(rads) * offset;
        double dz = -Math.cos(rads) * offset;
        pose.translate(dx, 0.2, dz);

        if (this.shouldShowName(entity) && entityRenderDispatcher.distanceToSqr(entity) < 64.0) {
            super.renderNameTag(entity, entity.getDisplayName(), pose, buffers, light);
        }

        pose.popPose();
    }
}
