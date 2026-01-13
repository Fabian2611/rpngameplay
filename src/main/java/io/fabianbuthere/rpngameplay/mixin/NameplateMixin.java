package io.fabianbuthere.rpngameplay.mixin;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
@OnlyIn(Dist.CLIENT)
public abstract class NameplateMixin<T extends Entity> {
    @Final
    @Shadow
    protected EntityRenderDispatcher entityRenderDispatcher;

    @Inject(
            method = "shouldShowName(Lnet/minecraft/world/entity/Entity;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void forceNameplate(T entity, CallbackInfoReturnable<Boolean> cir) {
        String entityId = entity.getType().getDescriptionId();

        if (entityId.contains("automobility")) {
            cir.setReturnValue(true);
        }
    }
}
