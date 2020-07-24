package ninjaphenix.containerlib.impl.client.config;

import blue.endless.jankson.Comment;
import net.minecraft.util.Identifier;
import ninjaphenix.containerlib.api.Constants;

public class Config
{
    @Comment("\nEnables auto focus of the search bar as soon as screen is opened.\nNo longer used.")
    public final Boolean auto_focus_searchbar = Boolean.FALSE;

    @Comment("\nPrefered container type, set to ninjaphenix-container-lib:auto to reuse selection screen.")
    public Identifier preferred_container_type = Constants.id("auto");

    @Comment("\nOnly allows scrolling in scrollable screen whilst hovering over the scrollbar region.")
    public Boolean restrictive_scrolling = Boolean.FALSE;

    @Comment("\nIf true centers the settings cog to the scrollbar in scrollable screens.")
    public Boolean settings_button_center_on_scrollbar = Boolean.TRUE;
}
