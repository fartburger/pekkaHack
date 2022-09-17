package com.fartburger.fartcheat.gui.screen;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.gui.FastTickable;
import com.fartburger.fartcheat.gui.TScreen;
import com.fartburger.fartcheat.gui.base.ScreenBase;
import com.fartburger.fartcheat.util.Utils;
import com.fartburger.fartcheat.util.font.FontRenderers;
import com.fartburger.fartcheat.util.font.adapters.FontAdapter;
import com.fartburger.fartcheat.util.render.Renderer;
import com.fartburger.fartcheat.util.render.textures.Texture;
import lombok.SneakyThrows;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.Level;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoadingScreen extends ScreenBase implements FastTickable {
    static LoadingScreen INSTANCE = null;
    final AtomicBoolean loaded = new AtomicBoolean(false);
    final AtomicBoolean loadInProg = new AtomicBoolean(false);
    String warningIfPresent = "";

    double opacity = 1;
    double clock = 1;
    final FontAdapter fr = FontRenderers.getCustomSize(22);
    protected LoadingScreen(Text title) {
        super(title);
    }
    public static LoadingScreen instance() {
        if (INSTANCE == null) {
            INSTANCE = new LoadingScreen(Text.of(""));
        }
        return INSTANCE;
    }
    @Override
    public void onFastTick() {
        if (FCRMain.client.getOverlay() == null) {
            if (!loadInProg.get()) {
                load();
            }
        }
        if (loaded.get()) {
            opacity -= 0.01;
            opacity = MathHelper.clamp(opacity, 0.001, 1);
        }
    }

    @SneakyThrows
    void load() {
        loadInProg.set(true);

        ExecutorService es = Executors.newFixedThreadPool(3);

        for (Field declaredField : Texture.class.getDeclaredFields()) {
            if (Modifier.isStatic(declaredField.getModifiers()) && Texture.class.isAssignableFrom(declaredField.getType())) {
                Object o = declaredField.get(null);
                Texture tex = (Texture) o;
                es.execute(() -> {
                    FCRMain.log(Level.INFO, "Loading " + tex);
                    try {
                        tex.load();
                        FCRMain.log(Level.INFO, "Loading " + tex);
                    } catch (Throwable t) {
                        FCRMain.log(Level.ERROR, "Failed to load " + tex);
                        t.printStackTrace();
                        warningIfPresent = "Some textures failed to download. They won't show up in game.";
                    }
                });
            }
        }

        new Thread(() -> {
            es.shutdown();
            try {
                //noinspection ResultOfMethodCallIgnored
                es.awaitTermination(99999, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (!warningIfPresent.isEmpty()) {
                    Utils.sleep(2000);
                }
                loaded.set(true);
            }

        }, "Loader").start();
    }

    @Override
    protected void initInternal() {
        TScreen.instance().init(client, width, height);
        if(loaded.get()&&opacity==0.001) {
            client.setScreen(TScreen.instance());
        }
        super.initInternal();
    }
    @Override
    public void renderInternal(MatrixStack stack, int mouseX, int mouseY, float delta) {

        if (loaded.get()) {
            TScreen.instance().renderInternal(stack, mouseX, mouseY, delta);
            if (opacity == 0.001) {
                assert this.client != null;
                this.client.setScreen(TScreen.instance());
                return;
            }
        }
        Renderer.R2D.renderQuad(stack,new Color(0f,0f,0f,(float)opacity),0,0,width,height);
        double anim = (System.currentTimeMillis() % 1000) / 1000d;
        String dots = ".".repeat((int) Math.max(Math.ceil(anim * 3), 1));
        String stauts = loaded.get() ? "Done!" : "Loading";
        fr.drawString(stack, stauts + dots, 3, height - fr.getFontHeight() - 3, 0.7f, 0.7f, 0.7f, (float) opacity);
        fr.drawString(stack,String.valueOf(clock),3,height-fr.getFontHeight()*2-6,0.7f,0.7f,0.7f,(float)opacity);
        stack.push();
    }
}
