package ninjaphenix.expandedstorage.impl.screen;

import net.minecraft.util.Identifier;
import ninjaphenix.expandedstorage.api.screen.ScreenMeta;

public final class SingleScreenMeta extends ScreenMeta
{
    public final int BLANK_SLOTS;

    public SingleScreenMeta(int width, int height, int totalSlots, Identifier texture, int textureWidth, int textureHeight)
    {
        super(width, height, totalSlots, texture, textureWidth, textureHeight);
        BLANK_SLOTS = width * height - totalSlots;
    }
}
