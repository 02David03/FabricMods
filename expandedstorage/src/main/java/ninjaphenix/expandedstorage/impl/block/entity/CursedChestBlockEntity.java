package ninjaphenix.expandedstorage.impl.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.api.EnvironmentInterfaces;
import net.minecraft.block.Block;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import ninjaphenix.expandedstorage.impl.content.ModContent;
import ninjaphenix.expandedstorage.impl.inventory.AbstractContainer;
import ninjaphenix.expandedstorage.impl.inventory.DoubleSidedInventory;
import ninjaphenix.expandedstorage.api.Registries;
import ninjaphenix.expandedstorage.impl.block.CursedChestBlock;
import ninjaphenix.expandedstorage.impl.block.misc.CursedChestType;

import java.util.List;

@EnvironmentInterfaces({ @EnvironmentInterface(value = EnvType.CLIENT, itf = ChestAnimationProgress.class) })
public final class CursedChestBlockEntity extends AbstractChestBlockEntity implements ChestAnimationProgress, Tickable
{
    private float animationAngle;
    private float lastAnimationAngle;
    private int viewerCount;
    private int ticksOpen;

    public CursedChestBlockEntity(final Identifier block) { super(ModContent.CHEST, block); }

    private static int tickViewerCount(World world, CursedChestBlockEntity instance, int ticksOpen, int x, int y, int z, int viewCount)
    {
        if (!world.isClient && viewCount != 0 && (ticksOpen + x + y + z) % 200 == 0)
        {
            int viewers = 0;
            final List<PlayerEntity> playersInRange = world.getNonSpectatingEntities(PlayerEntity.class, new Box(x - 5, y - 5, z - 5, x + 6, y + 6, z + 6));
            for (PlayerEntity player : playersInRange)
            {
                if (player.currentScreenHandler instanceof AbstractContainer)
                {
                    final Inventory inventory = ((AbstractContainer<?>) player.currentScreenHandler).getInventory();
                    if (inventory == instance || inventory instanceof DoubleSidedInventory && ((DoubleSidedInventory) inventory).isPart(instance))
                    {
                        viewers++;
                    }
                }
            }
            return viewers;
        }
        return viewCount;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void initialize(final Identifier block)
    {
        this.block = block;
        defaultContainerName = Registries.CHEST.get(block).getContainerName();
        inventorySize = Registries.CHEST.get(block).getSlotCount();
        inventory = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
        SLOTS = new int[inventorySize];
        for (int i = 0; i < inventorySize; i++) { SLOTS[i] = i; }
    }

    @Override
    public boolean onSyncedBlockEvent(final int actionId, final int value)
    {
        if (actionId == 1)
        {
            viewerCount = value;
            return true;
        }
        else { return super.onSyncedBlockEvent(actionId, value); }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public float getAnimationProgress(final float f) { return MathHelper.lerp(f, lastAnimationAngle, animationAngle); }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void tick()
    {
        viewerCount = tickViewerCount(world, this, ++ticksOpen, pos.getX(), pos.getY(), pos.getZ(), viewerCount);
        lastAnimationAngle = animationAngle;
        if (viewerCount > 0 && animationAngle == 0.0F) { playSound(SoundEvents.BLOCK_CHEST_OPEN); }
        if (viewerCount == 0 && animationAngle > 0.0F || viewerCount > 0 && animationAngle < 1.0F)
        {
            float float_2 = animationAngle;
            if (viewerCount > 0) { animationAngle += 0.1F; }
            else { animationAngle -= 0.1F; }
            animationAngle = MathHelper.clamp(animationAngle, 0, 1);
            if (animationAngle < 0.5F && float_2 >= 0.5F) { playSound(SoundEvents.BLOCK_CHEST_CLOSE); }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void playSound(final SoundEvent soundEvent)
    {
        final CursedChestType chestType = getCachedState().get(CursedChestBlock.TYPE);
        if (!chestType.isMainType()) { return; }
        final double zOffset = chestType == CursedChestType.BOTTOM ? 1 : 0.5;
        final BlockPos otherPos = CursedChestBlock.getPairedPos(world, pos);
        final Vec3d center = Vec3d.of(pos).add(Vec3d.of(otherPos == null ? pos : otherPos));
        world.playSound(null, center.getX() / 2 + 0.5D, center.getY() / 2 + 0.5D, center.getZ() / 2 + zOffset, soundEvent, SoundCategory.BLOCKS, 0.5F,
                world.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void onOpen(final PlayerEntity player)
    {
        if (player.isSpectator()) { return; }
        if (viewerCount < 0) { viewerCount = 0; }
        viewerCount++;
        onInvOpenOrClose();
    }

    @Override
    public void onClose(final PlayerEntity player)
    {
        if (player.isSpectator()) { return; }
        viewerCount--;
        onInvOpenOrClose();
    }

    @SuppressWarnings("ConstantConditions")
    private void onInvOpenOrClose()
    {
        final Block block = getCachedState().getBlock();
        if (block instanceof CursedChestBlock)
        {
            world.addSyncedBlockEvent(pos, block, 1, viewerCount);
            world.updateNeighborsAlways(pos, block);
        }
    }
}
