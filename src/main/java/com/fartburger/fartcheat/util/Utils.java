package com.fartburger.fartcheat.util;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.mixin.ClientWorldMixin;
import com.fartburger.fartcheat.mixinUtil.IMinecraftClient;
import com.fartburger.fartcheat.mixin.MinecraftClientMixin;
import com.fartburger.fartcheat.util.render.Texture;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.Packet;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.registry.Registry;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class Utils {
    public boolean clickguiactive = false;
    public static boolean sendPackets = true;

    public static void registerBufferedImageTexture(Texture i, BufferedImage bi) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);
            byte[] bytes = baos.toByteArray();

            ByteBuffer data = BufferUtils.createByteBuffer(bytes.length).put(bytes);
            data.flip();
            NativeImageBackedTexture tex = new NativeImageBackedTexture(NativeImage.read(data));
            FCRMain.client.execute(() -> FCRMain.client.getTextureManager().registerTexture(i, tex));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ignored) {
        }
    }

    public static void throwIfAnyEquals(String message, Object ifEquals, Object... toCheck) {
        for (Object o : toCheck) {
            if (o == ifEquals) {
                throw new IllegalArgumentException(message);
            }
        }
    }

    public static void getEnchantments(ItemStack itemStack, Object2IntMap<Enchantment> enchantments) {
        enchantments.clear();

        if (!itemStack.isEmpty()) {
            NbtList listTag = itemStack.getItem() == Items.ENCHANTED_BOOK ? EnchantedBookItem.getEnchantmentNbt(itemStack) : itemStack.getEnchantments();

            for (int i = 0; i < listTag.size(); ++i) {
                NbtCompound tag = listTag.getCompound(i);

                Registries.ENCHANTMENT.getOrEmpty(Identifier.tryParse(tag.getString("id"))).ifPresent((enchantment) -> enchantments.put(enchantment, tag.getInt("lvl")));
            }
        }
    }

    public static boolean hasEnchantments(ItemStack itemStack, Enchantment... enchantments) {
        if (itemStack.isEmpty()) return false;

        Object2IntMap<Enchantment> itemEnchantments = new Object2IntArrayMap<>();
        getEnchantments(itemStack, itemEnchantments);
        for (Enchantment enchantment : enchantments) if (!itemEnchantments.containsKey(enchantment)) return false;

        return true;
    }

    public static Vec3d getInterpolatedEntityPosition(Entity entity) {
        Vec3d a = entity.getPos();
        Vec3d b = new Vec3d(entity.prevX, entity.prevY, entity.prevZ);
        float p = FCRMain.client.getTickDelta();
        return new Vec3d(MathHelper.lerp(p, b.x, a.x), MathHelper.lerp(p, b.y, a.y), MathHelper.lerp(p, b.z, a.z));
    }

    public static void chatLog(String message) {
        if(FCRMain.client.player!=null) {
            FCRMain.client.player.sendMessage(Text.of(Formatting.BLUE+message));
        }
    }

    public static void chatError(String message) {
        if(FCRMain.client.player!=null) {
            FCRMain.client.player.sendMessage(Text.of(Formatting.RED+"[ERROR]: "+message));
        }
    }

    public static String nameToTitle(String name) {
        return Arrays.stream(name.split("-")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
    }

    public static void sendPacket(Packet<?> packet) {
        sendPackets = false;
        FCRMain.client.player.networkHandler.sendPacket(packet);
        sendPackets = true;
    }

    public static Color getCurrentRGB() {
        return Color.getHSBColor((System.currentTimeMillis() % 4750) / 4750f, 0.5f, 1);
    }

    public static PendingUpdateManager getUpdateManager(ClientWorld world) {
        return ((ClientWorldMixin) world).acquirePendingUpdateManager();
    }
    public static int increaseAndCloseUpdateManager(ClientWorld world) {
        PendingUpdateManager pum = getUpdateManager(world);
        int p = pum.getSequence();
        pum.close();
        return p;
    }

    public static void leftClick() {
        FCRMain.client.options.attackKey.setPressed(true);
        ((MinecraftClientMixin) FCRMain.client).leftClick();
        FCRMain.client.options.attackKey.setPressed(false);
    }
    public static void rightClick() {
        ((IMinecraftClient) FCRMain.client).rightClick();
    }

    public static class TickManager {

        static final java.util.List<TickEntry> entries = new ArrayList<>();
        static final List<Runnable> nextTickRunners = new ArrayList<>();

        public static void runInNTicks(int n, Runnable toRun) {
            entries.add(new TickEntry(n, toRun));
        }

        public static void tick() {
            Logging.sendMessages();
            for (TickEntry entry : entries.toArray(new TickEntry[0])) {
                entry.v--;
                if (entry.v <= 0) {
                    entry.r.run();
                    entries.remove(entry);
                }
            }
        }

        public static void runOnNextRender(Runnable r) {
            nextTickRunners.add(r);
        }

        public static void render() {
            for (Runnable nextTickRunner : nextTickRunners) {
                nextTickRunner.run();
            }
            nextTickRunners.clear();
        }

        static class TickEntry {

            final Runnable r;
            int v;

            public TickEntry(int v, Runnable r) {
                this.v = v;
                this.r = r;
            }
        }
    }

    public static class Logging {
        static final Queue<Text> messageQueue = new ArrayDeque<>();

        static void sendMessages() {
            if (FCRMain.client.player != null) {
                Text next;
                while ((next = messageQueue.poll()) != null) {
                    FCRMain.client.player.sendMessage(next, false);
                }
            }
        }

        public static void warn(String n) {
            message(n, Color.YELLOW);
        }

        public static void success(String n) {
            message(n, new Color(65, 217, 101));
        }

        public static void error(String n) {
            message(n, new Color(214, 93, 62));
        }

        public static void message(String n) {
            message(n, Color.WHITE);
        }

        public static void message(Text text) {
            messageQueue.add(text);
        }

        public static void message(String n, Color c) {
            MutableText t = Text.literal(n);
            t.setStyle(t.getStyle().withColor(TextColor.fromRgb(c.getRGB())));
            message(t);
        }

    }

    public static class Math {

        public static double roundToDecimal(double n, int point) {
            if (point == 0) {
                return java.lang.Math.floor(n);
            }
            double factor = java.lang.Math.pow(10, point);
            return java.lang.Math.round(n * factor) / factor;
        }

        public static int tryParseInt(String input, int defaultValue) {
            try {
                return Integer.parseInt(input);
            } catch (Exception ignored) {
                return defaultValue;
            }
        }

        public static Vec3d getRotationVector(float pitch, float yaw) {
            float f = pitch * 0.017453292F;
            float g = -yaw * 0.017453292F;
            float h = MathHelper.cos(g);
            float i = MathHelper.sin(g);
            float j = MathHelper.cos(f);
            float k = MathHelper.sin(f);
            return new Vec3d(i * j, -k, h * j);
        }

        public static boolean isABObstructed(Vec3d a, Vec3d b, World world, Entity requester) {
            RaycastContext rcc = new RaycastContext(a, b, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, requester);
            BlockHitResult bhr = world.raycast(rcc);
            return !bhr.getPos().equals(b);
        }
    }
}
