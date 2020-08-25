package ninjaphenix.expandedstorage.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import ninjaphenix.expandedstorage.common.Const;
import ninjaphenix.expandedstorage.client.screen.widget.ScreenTypeSelectionScreenButton;
import ninjaphenix.expandedstorage.common.inventory.PagedScreenHandler;
import ninjaphenix.expandedstorage.common.inventory.screen.PagedScreenMeta;

public final class PagedScreen extends AbstractScreen<PagedScreenHandler, PagedScreenMeta>
{
    private Rectangle blankArea = null;
    private PageButtonWidget leftPageButton;
    private PageButtonWidget rightPageButton;
    private int page;
    private TranslatableText currentPageText;
    private float pageTextX;

    public PagedScreen(final PagedScreenHandler screenHandler, final PlayerInventory playerInventory, final Text title)
    {
        super(screenHandler, playerInventory, title, (screenMeta) -> (screenMeta.WIDTH * 18 + 14) / 2 - 80);
        backgroundWidth = 14 + 18 * SCREEN_META.WIDTH;
        backgroundHeight = 17 + 97 + 18 * SCREEN_META.HEIGHT;
    }

    private void setPage(final int oldPage, final int newPage)
    {
        page = newPage;
        if (newPage > oldPage)
        {
            if (page == SCREEN_META.PAGES)
            {
                rightPageButton.setActive(false);
                final int blanked = SCREEN_META.BLANK_SLOTS;
                if (blanked > 0)
                {
                    final int xOffset = 7 + (SCREEN_META.WIDTH - blanked) * 18;
                    blankArea = new Rectangle(x + xOffset, y + backgroundHeight - 115, blanked * 18, 18, xOffset, backgroundHeight,
                                              SCREEN_META.TEXTURE_WIDTH, SCREEN_META.TEXTURE_HEIGHT);
                }
            }
            if (!leftPageButton.active) { leftPageButton.setActive(true); }
        }
        else if (newPage < oldPage)
        {
            if (page == 1) { leftPageButton.setActive(false); }
            if (blankArea != null) {blankArea = null; }
            if (!rightPageButton.active) { rightPageButton.setActive(true); }
        }
        final int slotsPerPage = SCREEN_META.WIDTH * SCREEN_META.HEIGHT;
        final int oldMin = slotsPerPage * (oldPage - 1);
        final int oldMax = Math.min(oldMin + slotsPerPage, SCREEN_META.TOTAL_SLOTS);
        handler.moveSlotRange(oldMin, oldMax, -2000);
        final int newMin = slotsPerPage * (newPage - 1);
        final int newMax = Math.min(newMin + slotsPerPage, SCREEN_META.TOTAL_SLOTS);
        handler.moveSlotRange(newMin, newMax, 2000);
        setPageText();
    }

    private void setPageText() { currentPageText = new TranslatableText("screen.expandedstorage.page_x_y", page, SCREEN_META.PAGES); }

    @Override
    public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta)
    {
        super.render(matrices, mouseX, mouseY, delta);
        if (SCREEN_META.PAGES != 1)
        {
            leftPageButton.renderTooltip(matrices, mouseX, mouseY);
            rightPageButton.renderTooltip(matrices, mouseX, mouseY);
        }
    }

    @Override
    protected void init()
    {
        final FabricLoader instance = FabricLoader.getInstance();
        final boolean inventoryProfilesLoaded = instance.isModLoaded("inventoryprofiles");
        final boolean inventorySorterLoaded = instance.isModLoaded("inventorysorter");
        super.init();
        final int settingsXOffset;
        if (inventoryProfilesLoaded) { settingsXOffset = -67; }
        else if (inventorySorterLoaded) { settingsXOffset = -37; }
        else { settingsXOffset = -19; }
        addButton(new ScreenTypeSelectionScreenButton(x + backgroundWidth + settingsXOffset, y + 4,
                                                      (button, matrices, mouseX, mouseY) -> renderTooltip(matrices, button.getMessage(), mouseX, mouseY)));
        if (SCREEN_META.PAGES != 1)
        {
            final int pageButtonsXOffset;
            if (inventoryProfilesLoaded) { pageButtonsXOffset = -12; }
            else if (inventorySorterLoaded) { pageButtonsXOffset = -18; }
            else { pageButtonsXOffset = 0; }
            page = 1;
            setPageText();
            leftPageButton = new PageButtonWidget(x + backgroundWidth - 61 + pageButtonsXOffset, y + backgroundHeight - 96, 0,
                                                  new TranslatableText("screen.expandedstorage.prev_page"), button -> setPage(page, page - 1),
                                                  (button, matrices, bX, bY) -> renderTooltip(matrices, button.getMessage(), bX, bY));
            leftPageButton.active = false;
            addButton(leftPageButton);
            rightPageButton = new PageButtonWidget(x + backgroundWidth - 19 + pageButtonsXOffset, y + backgroundHeight - 96, 1,
                                                   new TranslatableText("screen.expandedstorage.next_page"), button -> setPage(page, page + 1),
                                                   (button, matrices, bX, bY) -> renderTooltip(matrices, button.getMessage(), bX, bY));
            addButton(rightPageButton);
            pageTextX = (1 + leftPageButton.x + rightPageButton.x - rightPageButton.getWidth() / 2F) / 2F;
        }
    }

    @Override
    protected void drawBackground(final MatrixStack matrices, final float delta, final int mouseX, final int mouseY)
    {
        super.drawBackground(matrices, delta, mouseX, mouseY);
        if (blankArea != null) { blankArea.render(matrices); }
    }

    @Override
    public void resize(final MinecraftClient client, final int width, final int height)
    {
        if (SCREEN_META.PAGES != 1)
        {
            final int currentPage = page;
            if (currentPage != 1)
            {
                handler.resetSlotPositions(false);
                super.resize(client, width, height);
                setPage(1, currentPage);
                return;
            }
        }
        super.resize(client, width, height);
    }

    @Override
    protected void drawForeground(final MatrixStack matrices, final int mouseX, final int mouseY)
    {
        super.drawForeground(matrices, mouseX, mouseY);
        if (currentPageText != null)
        {
            textRenderer.draw(matrices, currentPageText, pageTextX - x, backgroundHeight - 94, 0x404040);
        }
    }

    @Override
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers)
    {
        if (keyCode == 262 || keyCode == 267) // Right Arrow, Page Down
        {
            if (SCREEN_META.PAGES != 1)
            {
                if (hasShiftDown()) { setPage(page, SCREEN_META.PAGES); }
                else { if (page != SCREEN_META.PAGES) { setPage(page, page + 1); } }
                return true;
            }
        }
        else if (keyCode == 263 || keyCode == 266) // Left Arrow, Page Up
        {
            if (SCREEN_META.PAGES != 1)
            {
                if (hasShiftDown()) { setPage(page, 1); }
                else { if (page != 1) { setPage(page, page - 1); } }
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private static class PageButtonWidget extends ButtonWidget
    {
        private static final Identifier TEXTURE = Const.id("textures/gui/page_buttons.png");
        private final int TEXTURE_OFFSET;

        public PageButtonWidget(final int x, final int y, final int textureOffset, final Text text, final PressAction onPress,
                                final TooltipSupplier tooltipSupplier)
        {
            super(x, y, 12, 12, text, onPress, tooltipSupplier);
            TEXTURE_OFFSET = textureOffset;
        }

        public void setActive(final boolean active)
        {
            this.active = active;
            if (!active) { setFocused(false); }
        }

        @Override
        @SuppressWarnings("deprecation")
        public void renderButton(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta)
        {
            final MinecraftClient minecraftClient = MinecraftClient.getInstance();
            minecraftClient.getTextureManager().bindTexture(TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            drawTexture(matrices, x, y, TEXTURE_OFFSET * 12, getYImage(isHovered()) * 12, width, height, 32, 48);
        }

        public void renderTooltip(final MatrixStack matrices, final int mouseX, final int mouseY)
        {
            if (active)
            {
                if (hovered) { renderToolTip(matrices, mouseX, mouseY); }
                else if (isHovered()) { renderToolTip(matrices, x, y); }
            }
        }
    }
}