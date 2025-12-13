package com.github.minecraftschurlimods.bibliocraft.client.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class SpriteButton extends Button {
    public SpriteButton(int x, int y, int width, int height, OnPress onPress) {
        super(new Builder(Component.empty(), onPress).bounds(x, y, width, height));
    }

    protected abstract ResourceLocation getSprite();

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation sprite = getSprite();
        if (sprite != null) {
            guiGraphics.blit(sprite, getX(), getY(), 0, 0, getWidth(), getHeight());
        }
    }

    public static class RegularAndHighlightSprite extends SpriteButton {
        private final ResourceLocation sprite;
        private final ResourceLocation highlightedSprite;

        public RegularAndHighlightSprite(ResourceLocation sprite, ResourceLocation highlightedSprite, int x, int y, int width, int height, OnPress onPress) {
            super(x, y, width, height, onPress);
            this.sprite = sprite;
            this.highlightedSprite = highlightedSprite;
        }

        @Override
        protected ResourceLocation getSprite() {
            return isHoveredOrFocused() ? highlightedSprite : sprite;
        }
    }
}
