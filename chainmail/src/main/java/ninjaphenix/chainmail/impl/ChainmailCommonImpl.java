package ninjaphenix.chainmail.impl;

import net.minecraft.item.ItemGroup;
import ninjaphenix.chainmail.api.ChainmailCommonApi;
import ninjaphenix.chainmail.impl.mixinhelpers.ItemGroupArrayExpander;

import java.util.function.IntFunction;

public class ChainmailCommonImpl implements ChainmailCommonApi
{
    public static final ChainmailCommonApi INSTANCE = new ChainmailCommonImpl();

    @Override
    public <T extends ItemGroup> T registerItemGroup(final IntFunction<T> itemGroup)
    {
        final ItemGroup buildingBlocks = ItemGroup.BUILDING_BLOCKS;
        if (buildingBlocks instanceof ItemGroupArrayExpander)
        {
            final int index = ((ItemGroupArrayExpander) buildingBlocks).chainmail_expandArraySize();
            final T group = itemGroup.apply(index);
            ItemGroup.GROUPS[index] = group;
            return group;
        }
        throw new RuntimeException("[chainmail-common-api] Item group mixin failed to apply. Unable to register item groups.");
    }
}