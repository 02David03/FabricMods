package ninjaphenix.expandedstorage.client.screen.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import ninjaphenix.expandedstorage.common.Const;

public final class ScreenTypeSelectionScreenButton extends Button
{
    private final ResourceLocation TEXTURE;

    public ScreenTypeSelectionScreenButton(final int x, final int y, final OnTooltip tooltipSupplier)
    {
        super(x, y, 12, 12, new TranslatableComponent("screen.expandedstorage.change_screen_button"), button ->
        {
            ClientSidePacketRegistry.INSTANCE.sendToServer(Const.OPEN_SCREEN_SELECT, new FriendlyByteBuf(Unpooled.buffer()));
        }, tooltipSupplier);
        TEXTURE = Const.id("textures/gui/select_screen_button.png");
    }

    @Override
    @SuppressWarnings("deprecation")
    public void renderButton(final PoseStack matrices, final int mouseX, final int mouseY, final float delta)
    {
        Minecraft.getInstance().getTextureManager().bind(TEXTURE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        blit(matrices, x, y, 0, isHovered() ? height : 0, width, height, 16, 32);
        if (isHovered()) { renderToolTip(matrices, mouseX, mouseY); }
    }
}