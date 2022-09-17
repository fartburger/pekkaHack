package com.fartburger.fartcheat;

import com.fartburger.fartcheat.config.ConfigManager;
import com.fartburger.fartcheat.gui.FastTickable;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.util.Utils;
import com.fartburger.fartcheat.util.font.FontRenderers;
import com.fartburger.fartcheat.util.font.adapters.QuickFontAdapter;
import com.fartburger.fartcheat.util.font.renderer.FontRenderer;
import com.fartburger.fartcheat.modules.Module;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class FCRMain implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger();
	public static final MinecraftClient client = MinecraftClient.getInstance();
	public static File BASE = new File(MinecraftClient.getInstance().runDirectory,"pekka");
	public static Thread FAST_TICKER;
	public static FCRMain INSTANCE;

	public static void log(Level level, Object... message) {
		LOGGER.log(level, Arrays.stream(message).map(Object::toString).collect(Collectors.joining(" ")));
	}

	@Override
	public void onInitialize() {
		INSTANCE = this;
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution
		Runtime.getRuntime().addShutdownHook(new Thread(ConfigManager::saveState));
		if (BASE.exists() && !BASE.isDirectory()) {
			BASE.delete();
		}
		if (!BASE.exists()) {
			BASE.mkdir();
		}
		ConfigManager.loadState();
		log(Level.INFO,"initializing...");
	}

	void initFonts() {
		try {
			int fsize = 18 * 2;
			FontRenderers.setRenderer(new QuickFontAdapter(new FontRenderer(Font.createFont(Font.TRUETYPE_FONT,
					Objects.requireNonNull(FCRMain.class.getClassLoader().getResourceAsStream("Font.ttf"))).deriveFont(Font.PLAIN, fsize), fsize)));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	void tickModules() {
		for (Module module : ModuleRegistry.getModules()) {
			try {
				if (module.isEnabled()) {
					module.onFastTick();
				}
			} catch (Exception ignored) {
			}
		}
	}
	void tickGuiSystem() {
		try {
			if (client.currentScreen != null) {
				if (client.currentScreen instanceof FastTickable tickable) {
					tickable.onFastTick();
				}
				for (Element child : new ArrayList<>(client.currentScreen.children())) { // wow, I hate this
					if (child instanceof FastTickable t) {
						t.onFastTick();
					}
				}
			}
		} catch (Exception ignored) {

		}
	}

	public void postWindowInit() {
		initFonts();
		FAST_TICKER = new Thread(() -> {
			while (true) {
				Utils.sleep(10);
				tickGuiSystem(); // gui elements
				tickModules(); // hacks
				if (client.player == null || client.world == null) {
					continue;
				}
				 // updates rotations, again only if we are in a world
			}
		}, "Animation ticker");
		FAST_TICKER.start();
		log(Level.INFO,"the gears of time are now ticking...");
	}
}
