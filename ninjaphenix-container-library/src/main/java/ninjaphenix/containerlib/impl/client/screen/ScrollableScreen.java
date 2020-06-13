package ninjaphenix.containerlib.impl.client.screen;


import ninjaphenix.containerlib.api.client.screen.AbstractScreen;
import ninjaphenix.containerlib.api.client.screen.widget.ScreenTypeSelectionScreenButton;
import ninjaphenix.containerlib.impl.inventory.ScrollableContainer;

public class ScrollableScreen<T extends ScrollableContainer> extends AbstractScreen<T>
{
    private Rectangle blankArea = null;

    public ScrollableScreen(T container)
    {
        super(container, (screenMeta) -> (screenMeta.WIDTH * 18 + 14) / 2 - 80);
        containerWidth = 14 + 18 * SCREEN_META.WIDTH;
        containerHeight = 17 + 97 + 18 * SCREEN_META.HEIGHT;
    }

    @Override
    protected void init()
    {
        super.init();
        addButton(new ScreenTypeSelectionScreenButton(x + containerWidth - 19, y + 4));
        final int totalSlots = container.slots.size() - 36;
        final int diff = SCREEN_META.TOTAL_SLOTS - totalSlots;
        if (diff > 0)
        {
            final int xOffset = 7 + (SCREEN_META.WIDTH - diff) * 18;
            blankArea = new Rectangle(x + xOffset, y + containerHeight - 115, diff * 18, 18,
                    xOffset, containerHeight, SCREEN_META.TEXTURE_WIDTH, SCREEN_META.TEXTURE_HEIGHT);
        }

    }

    @Override
    protected void drawBackground(float delta, int mouseX, int mouseY)
    {
        super.drawBackground(delta, mouseX, mouseY);
        if (blankArea != null) { blankArea.render(); }
    }
}


