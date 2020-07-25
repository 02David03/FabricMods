package ninjaphenix.creativebuttonmover.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import ninjaphenix.chainmail.api.config.JanksonConfigParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;

import java.nio.file.Path;

public class CreativeButtonMover implements ClientModInitializer
{
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("creativebuttonmover.json");
    public static final Logger LOGGER = LogManager.getLogger("creativebuttonmover");
    private static final JanksonConfigParser CONFIG_PARSER = new JanksonConfigParser.Builder().build();


    // public static LinkedHashMap<Identifier, Vec2i> PreviousButtonTextures = new LinkedHashMap<>(0);
    // public static LinkedHashMap<Identifier, Vec2i> NextButtonTextures = new LinkedHashMap<>(0);

    // private static Optional<Pair<Identifier, Vec2i>> tryReadPngSize(Resource resource)
    // {
    // 	final TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
    // 	textureManager.bindTexture(resource.getId());
    // 	final int width = GlStateManager.getTexLevelParameter(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH);
    // 	final int height = GlStateManager.getTexLevelParameter(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT);
    // 	if (height % 3.0D == 0 && height > 0 && width > 0) { return Optional.of(new Pair<>(resource.getId(), new Vec2i(width, height))); }
    // 	LOGGER.error("Texture, " + resource.getId().toString() + ", is not valid, width must be non-zero and height must be multiple of 3 and non-zero.");
    // 	return Optional.empty();
    // }

    public static void loadConfig()
    {
        Config.INSTANCE = CONFIG_PARSER.load(Config.class, Config::new, CONFIG_PATH, new MarkerManager.Log4jMarker("creativebuttonmover"));
    }

    public static void saveConfig()
    {
        CONFIG_PARSER.save(Config.INSTANCE, CONFIG_PATH, new MarkerManager.Log4jMarker("creativebuttonmover"));
    }

    @Override
    public void onInitializeClient()
    {
		/*
		ResourceManagerHelper.get(CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener()
		{
			@Override
			public Identifier getFabricId() { return new Identifier("creativebuttonmover", "button_finder"); }

			@Override
			public void apply(ResourceManager manager)
			{
				final Predicate<String> isPNG = (name) -> name.endsWith(".png");
				final Function<Identifier, Optional<Resource>> resourceGetter = id -> {
					try (Resource res = manager.getResource(id)) { return Optional.of(res); }
					catch (IOException ioError)
					{
						return Optional.empty();
					}
				};
				final BinaryOperator<Vec2i> keyMerger = (u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); };

				NextButtonTextures = manager.findResources("textures/gui/creativebuttons/next/", isPNG)
											.stream()
											.map(resourceGetter)
											.filter(Optional::isPresent)
											.map(Optional::get)
											.map(CreativeButtonMover::tryReadPngSize)
											.filter(Optional::isPresent)
											.map(Optional::get)
											.collect(Collectors.toMap(Pair::getLeft, Pair::getRight, keyMerger, LinkedHashMap::new));

				PreviousButtonTextures = manager.findResources("textures/gui/creativebuttons/prev/", isPNG)
												.stream()
												.map(resourceGetter)
												.filter(Optional::isPresent)
												.map(Optional::get)
												.map(CreativeButtonMover::tryReadPngSize)
												.filter(Optional::isPresent)
												.map(Optional::get)
												.collect(Collectors.toMap(Pair::getLeft, Pair::getRight, keyMerger, LinkedHashMap::new));

			}
		});
		 */
        loadConfig();
    }
}
