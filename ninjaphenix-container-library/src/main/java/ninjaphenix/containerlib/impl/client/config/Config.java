package ninjaphenix.containerlib.impl.client.config;

import blue.endless.jankson.Comment;
import net.minecraft.util.Identifier;
import ninjaphenix.containerlib.api.Constants;

public class Config
{
    @Comment("\nEnables auto focus of the search bar as soon as screen is opened.")
    public final Boolean auto_focus_searchbar = Boolean.FALSE;

    @Comment("\nPrefered container type, set to ninjaphenix-container-lib:auto to reuse selection screen.")
    public Identifier preferred_container_type = Constants.idOf("auto");
}
