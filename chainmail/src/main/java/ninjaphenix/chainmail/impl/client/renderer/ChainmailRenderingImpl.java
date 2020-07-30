package ninjaphenix.chainmail.impl.client.renderer;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ninjaphenix.chainmail.api.client.render.ChainmailRendering;
import ninjaphenix.chainmail.api.client.render.ItemStackRenderFunction;
import ninjaphenix.chainmail.impl.mixinhelpers.BuiltinModelItemRendererExtensions;

import java.util.function.Predicate;

public class ChainmailRenderingImpl implements ChainmailRendering
{

    public static final BuiltinModelItemRendererExtensions ext = (BuiltinModelItemRendererExtensions) BuiltinModelItemRenderer.INSTANCE;

    @Override
    public void registerBlockEntityItemStackRenderer(final BlockEntityType<?> type, final ItemStackRenderFunction renderFunction)
    {
        ext.chainmail_addRenderer((stack) -> {
            final Item item = stack.getItem();
            return item instanceof BlockItem && type.supports(((BlockItem) item).getBlock());
        }, renderFunction);
    }
}