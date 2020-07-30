package ninjaphenix.noncorrelatedextras.features;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ninjaphenix.noncorrelatedextras.Main;
import ninjaphenix.noncorrelatedextras.client.StructureCompassModelPredicateProvider;
import ninjaphenix.noncorrelatedextras.config.Config;
import ninjaphenix.noncorrelatedextras.core.Feature;
import ninjaphenix.noncorrelatedextras.core.ItemAdder;
import ninjaphenix.noncorrelatedextras.items.StructureCompassItem;

import java.util.Set;

public class StructureCompassFeature extends Feature implements ItemAdder
{
    @Override
    public void registerItems()
    {
        final Set<Identifier> structures = Config.INSTANCE.getEnabledStructureCompasses();
        final Item.Settings settings = new Item.Settings().maxCount(1).group(ItemGroup.TOOLS);
        for (final Identifier structure : structures)
        {
            Registry.STRUCTURE_FEATURE.getOrEmpty(structure).ifPresent(feature ->
            {
                StructureCompassItem item = new StructureCompassItem(settings, feature);
                if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) { registerItemModelPredicate(item); }
                Registry.register(Registry.ITEM, Main.getId(structure.getPath() + "_compass"), item);
            });

        }
    }

    @Environment(EnvType.CLIENT)
    private void registerItemModelPredicate(final Item item)
    {
        ModelPredicateProviderRegistry.register(item, new Identifier("angle"), new StructureCompassModelPredicateProvider());
    }
}