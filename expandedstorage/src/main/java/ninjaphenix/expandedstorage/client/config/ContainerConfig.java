package ninjaphenix.expandedstorage.client.config;

import blue.endless.jankson.Comment;
import net.minecraft.util.Identifier;
import ninjaphenix.expandedstorage.common.Const;

@SuppressWarnings("CanBeFinal")
public final class ContainerConfig
{
    @Comment("\nPrefered container type, set to expandedstorage:auto to reuse selection screen.")
    public Identifier preferred_container_type = Const.id("auto");

    @Comment("\nOnly allows scrolling in scrollable screen whilst hovering over the scrollbar region.")
    public Boolean restrictive_scrolling = Boolean.FALSE;

    @Comment("\nIf true centers the settings cog to the scrollbar in scrollable screens.")
    public Boolean settings_button_center_on_scrollbar = Boolean.TRUE;
}