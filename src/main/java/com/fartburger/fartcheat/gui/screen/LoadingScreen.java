package com.fartburger.fartcheat.gui.screen;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.gui.FastTickable;
import com.fartburger.fartcheat.gui.TScreen;
import com.fartburger.fartcheat.gui.base.ScreenBase;
import com.fartburger.fartcheat.gui.widget.RoundButton;
import com.fartburger.fartcheat.util.Utils;
import com.fartburger.fartcheat.util.font.FontRenderers;
import com.fartburger.fartcheat.util.font.adapters.FontAdapter;
import com.fartburger.fartcheat.util.render.Renderer;
import com.fartburger.fartcheat.util.render.textures.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Cleanup;
import lombok.SneakyThrows;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import fart.json.*;
import org.apache.commons.net.util.SubnetUtils;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoadingScreen extends ScreenBase implements FastTickable {
    static LoadingScreen INSTANCE = null;
    final AtomicBoolean loaded = new AtomicBoolean(false);
    final AtomicBoolean loadInProg = new AtomicBoolean(false);

    final AtomicBoolean failed = new AtomicBoolean(false);
    String warningIfPresent = "";
    String localver;
    double opacity = 1;
    double clock = 1;
    final FontAdapter fr = FontRenderers.getCustomSize(22);
    protected LoadingScreen(int samples) {
        super(samples);
    }
    public static LoadingScreen instance() {
        if (INSTANCE == null) {
            INSTANCE = new LoadingScreen(4);
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
        HttpClient ocli = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        URI uri = URI.create("https://raw.githubusercontent.com/fartburger/pekkaHack/master/src/main/resources/version.txt");
        HttpRequest get = HttpRequest.newBuilder().uri(uri).header("User-Agent", "pekka/1.0").build();
        HttpResponse<String> send = ocli.send(get, HttpResponse.BodyHandlers.ofString());
        String remotever = send.body();

        HttpClient ocli2 = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        URI uri2 = URI.create("https://api.github.com/repos/fartburger/pekkaHack/commits");
        HttpRequest get2 = HttpRequest.newBuilder().uri(uri2).header("User-Agent", "pekka/1.0").build();
        HttpResponse<String> send2 = ocli2.send(get2, HttpResponse.BodyHandlers.ofString());
        String sha = send2.body();
        JSONArray ja = new JSONArray(sha);
        JSONObject jo = ja.getJSONObject(0);
        JSONObject jo2 = jo.getJSONObject("commit");
        TScreen.latestfromgithub = jo2.getString("message");


        localver = Util.make(() -> {
            try {
                return IOUtils.toString(Objects.requireNonNull(LoadingScreen.class.getClassLoader().getResourceAsStream("version.txt")), StandardCharsets.UTF_8);
            } catch (Exception ignored) {
                return "unknown";
            }
        });

        if(Double.parseDouble(remotever)>Double.parseDouble(localver)) {
            TScreen.outdated=true;
        }


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
                        failed.set(true);
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
        fr.drawString(stack,String.valueOf(localver),3,height-fr.getFontHeight()*2-6,0.7f,0.7f,0.7f,(float)opacity);
        Renderer.R2D.renderLoadingSpinner(stack,150,this.width/2,this.height/2,20d,20d,55d);

        if(failed.get()) {
            FontRenderers.getCustomSize(35).drawString(stack,"something went wrong",this.width/2-FontRenderers.getCustomSize(45).getStringWidth("something went wrong")/2,this.height/2- textRenderer.fontHeight/2,0xFF2222);
        }
        stack.push();
    }
}
