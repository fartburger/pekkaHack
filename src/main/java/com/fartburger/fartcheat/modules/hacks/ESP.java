package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.config.BooleanSetting;
import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.config.EnumSetting;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.Utils;
import com.fartburger.fartcheat.util.render.Renderer;
import com.fartburger.fartcheat.util.vertex.DumpVertexConsumer;
import com.fartburger.fartcheat.util.vertex.DumpVertexProvider;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ESP extends Module {
    //static DumpVertexConsumer consumer = new DumpVertexConsumer();
    static DumpVertexProvider provider;
    public final EnumSetting<Mode> outlineMode = this.config.create(new EnumSetting.Builder<>(Mode.Filled).name("OutlineMode")
            .description("How to render the outline")
            .get());
    public final EnumSetting<ShaderMode> shaderMode = this.config.create(new EnumSetting.Builder<>(ShaderMode.Simple).name("ShaderMode")
            .description("How to render the shader esp")
            .get());
    public final BooleanSetting entities = this.config.create(new BooleanSetting.Builder(true).name("ShowEntities").description("Render entities").get());
    public final BooleanSetting players = this.config.create(new BooleanSetting.Builder(true).name("ShowPlayers").description("Render players").get());
    public final List<double[]> vertexDumps = new ArrayList<>();
    final DoubleSetting range = this.config.create(new DoubleSetting.Builder(64).name("Range")
            .description("How far to render the entities")
            .min(32)
            .max(128)
            .precision(1)
            .get());
    public boolean recording = false;

    public ESP() {
        super("ESP", "Shows where entities are", ModuleType.RENDER);
        shaderMode.showIf(() -> outlineMode.getValue() == Mode.Shader);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return null;
    }

    public boolean shouldRenderEntity(Entity e) {
        return (e instanceof PlayerEntity && players.getValue() || entities.getValue()) && !(e instanceof ItemEntity || e instanceof FallingBlockEntity || e instanceof ExperienceOrbEntity || e instanceof ThrownEntity);
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (outlineMode.getValue() == Mode.Model) {
            float alpha = 1f;
            List<double[]> vertBuffer = new ArrayList<>();
            List<double[][]> verts = new ArrayList<>();
            for (double[] vertexDump : vertexDumps) {
                if (vertexDump.length == 0) {
                    verts.add(vertBuffer.toArray(double[][]::new));
                    vertBuffer.clear();
                } else {
                    vertBuffer.add(vertexDump);
                }
            }
            verts.add(vertBuffer.toArray(double[][]::new));
            vertBuffer.clear();
            vertexDumps.clear();

            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            GL11.glDepthFunc(GL11.GL_LEQUAL);
            double p;
            BufferBuilder buffer = Tessellator.getInstance().getBuffer();
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            for (double[][] vert : verts) {

                for (double[] vertexDump : vert) {
                    p = (((/*vertexDump[0]+vertexDump[1]+*/vertexDump[2]) % 10) / 10 + (System.currentTimeMillis() % 2000) / 2000d) % 1;
                    int col = Color.HSBtoRGB((float) p, .6f, 1f);
                    float red = (col >> 16 & 0xFF) / 255f;
                    float green = (col >> 8 & 0xFF) / 255f;
                    float blue = (col & 0xFF) / 255f;
                    buffer.vertex(vertexDump[0], vertexDump[1], vertexDump[2]).color(red, green, blue, alpha).next();
                }

            }
            BufferRenderer.drawWithGlobalProgram(buffer.end());

            GL11.glDepthFunc(GL11.GL_LEQUAL);
            RenderSystem.disableBlend();
            return;
        }
        if (FCRMain.client.world == null || FCRMain.client.player == null) {
            return;
        }

        for (Entity entity : FCRMain.client.world.getEntities()) {
            if (entity.squaredDistanceTo(FCRMain.client.player) > Math.pow(range.getValue(), 2)) {
                continue;
            }
            if (entity.getUuid().equals(FCRMain.client.player.getUuid())) {
                continue;
            }
            if (shouldRenderEntity(entity)) {
                Color c = entity.isPlayer() ? new Color(11, 204, 204,140) : new Color(70, 220, 33,140);
                Vec3d eSource = Utils.getInterpolatedEntityPosition(entity);
                switch (outlineMode.getValue()) {
                    case Filled -> Renderer.R3D.renderFilled(matrices,
                            c,
                            eSource.subtract(new Vec3d(entity.getWidth(), 0, entity.getWidth()).multiply(0.5)),
                            new Vec3d(entity.getWidth(), entity.getHeight(), entity.getWidth()));
                    case Rect -> renderOutline(entity, c, matrices);
                    case Outline -> Renderer.R3D.renderOutline(matrices,
                            c,
                            eSource.subtract(new Vec3d(entity.getWidth(), 0, entity.getWidth()).multiply(0.5)),
                            new Vec3d(entity.getWidth(), entity.getHeight(), entity.getWidth()));
                    case Shader -> renderShaderOutline(entity, matrices);
                }
            }
        }
    }

    void renderShaderOutline(Entity e, MatrixStack stack) {
        if (provider == null) {
            provider = new DumpVertexProvider();
        }

        Vec3d origin = Utils.getInterpolatedEntityPosition(e);

        List<Vec3d> boxPoints = new ArrayList<>();
        if (shaderMode.getValue() == ShaderMode.Accurate) {
            EntityRenderer<? super Entity> eRenderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(e);
            eRenderer.render(e, e.getYaw(), MinecraftClient.getInstance().getTickDelta(), Renderer.R3D.getEmptyMatrixStack(), provider, 0);
            for (DumpVertexConsumer consumer : provider.getBuffers()) {
                for (DumpVertexConsumer.VertexData vertexData : consumer.getStack()) {
                    if (vertexData.getPosition() != null) {
                        boxPoints.add(vertexData.getPosition().add(origin));
                    }
                }
                consumer.clear();
            }
        } else {
            double w = e.getWidth();
            double h = e.getHeight();

            Vec3d o = origin.subtract(w / 2d, 0, w / 2d);

            boxPoints.addAll(List.of(new Vec3d(o.x + 0, o.y, o.z + 0),
                    new Vec3d(o.x + w, o.y, o.z + 0),
                    new Vec3d(o.x + 0, o.y, o.z + w),
                    new Vec3d(o.x + w, o.y, o.z + w),

                    new Vec3d(o.x + 0, o.y + h, o.z + 0),
                    new Vec3d(o.x + w, o.y + h, o.z + 0),
                    new Vec3d(o.x + 0, o.y + h, o.z + w),
                    new Vec3d(o.x + w, o.y + h, o.z + w)));
        }

        Vec3d[] screenSpace = boxPoints.stream().map(ee -> Renderer.R2D.getScreenSpaceCoordinate(ee, stack)).toList().toArray(Vec3d[]::new);

        if (screenSpace.length == 0) {
            return;
        }

        Vec3d leastX = screenSpace[0];
        Vec3d mostX = screenSpace[0];
        Vec3d leastY = screenSpace[0];
        Vec3d mostY = screenSpace[0];
        for (Vec3d vec3d : screenSpace) {
            if (!Renderer.R2D.isOnScreen(vec3d)) {
                return;
            }
            if (vec3d.x < leastX.x) {
                leastX = vec3d;
            }
            if (vec3d.x > mostX.x) {
                mostX = vec3d;
            }
            if (vec3d.y < leastY.y) {
                leastY = vec3d;
            }
            if (vec3d.y > mostY.y) {
                mostY = vec3d;
            }
        }
        Vec3d finalLeastX = leastX;
        Vec3d finalLeastY = leastY;
        Vec3d finalMostX = mostX;
        Vec3d finalMostY = mostY;
        Utils.TickManager.runOnNextRender(() -> {
            //Renderer.R2D.renderRoundedQuad(Renderer.R3D.getEmptyMatrixStack(),new Color(255,255,255,100),finalLeastX.x,finalLeastY.y,finalMostX.x,finalMostY.y,5,20);
            float x1 = (float) finalLeastX.x;
            float y1 = (float) finalLeastY.y;
            float x2 = (float) finalMostX.x;
            float y2 = (float) finalMostY.y;
            float r = 1f;
            float g = 1f;
            float b = 1f;
            float a = 1f;
            float desiredHeight = (float) ((finalMostY.y - finalLeastY.y) / 3f);
            float desiredWidth = (float) ((finalMostX.x - finalLeastX.x) / 3f);
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            Renderer.setupRender();
            RenderSystem.disableCull();
            renderCorner(bufferBuilder, r, g, b, a, x1, y1, desiredHeight, desiredWidth, 1, 1);
            renderCorner(bufferBuilder, r, g, b, a, x2, y1, desiredHeight, desiredWidth, -1, 1);
            renderCorner(bufferBuilder, r, g, b, a, x2, y2, desiredHeight, desiredWidth, -1, -1);
            renderCorner(bufferBuilder, r, g, b, a, x1, y2, desiredHeight, desiredWidth, 1, -1);
            RenderSystem.enableCull();
            Renderer.endRender();

        });
    }

    void renderCorner(BufferBuilder bb, float r, float g, float b, float a, float x, float y, float height, float topWidth, float xMul, float yMul) {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bb.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        float width = 1;
        /*
        4---------5
        |         |
        |    1----6
        |    |
        |    |
        3----2
        */
        //matrix.multiply(new Quaternion(0,0,(float) rotation,true));
        float[][] verts = new float[][] { new float[] { 0, 0 }, new float[] { 0, height }, new float[] { -width, height }, new float[] { -width, -width },
                new float[] { topWidth, -width }, new float[] { topWidth, 0 }, new float[] { 0, 0 } };
        for (float[] vert : verts) {
            bb.vertex(x + vert[0] * xMul, y + vert[1] * yMul, 0f).color(r, g, b, a).next();
        }
        BufferRenderer.drawWithGlobalProgram(bb.end());
    }

    @Override
    public void onHudRender() {

    }

    void renderOutline(Entity e, Color color, MatrixStack stack) {
        Vec3d eSource = new Vec3d(MathHelper.lerp(FCRMain.client.getTickDelta(), e.prevX, e.getX()),
                MathHelper.lerp(FCRMain.client.getTickDelta(), e.prevY, e.getY()),
                MathHelper.lerp(FCRMain.client.getTickDelta(), e.prevZ, e.getZ()));
        float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;
        float alpha = color.getAlpha() / 255f;
        Camera c = FCRMain.client.gameRenderer.getCamera();
        Vec3d camPos = c.getPos();
        Vec3d start = eSource.subtract(camPos);
        float x = (float) start.x;
        float y = (float) start.y;
        float z = (float) start.z;

        double r = Math.toRadians(-c.getYaw() + 90);
        float sin = (float) (Math.sin(r) * (e.getWidth() / 1.7));
        float cos = (float) (Math.cos(r) * (e.getWidth() / 1.7));
        stack.push();

        Matrix4f matrix = stack.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableBlend();
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix, x + sin, y, z + cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin, y, z - cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin, y, z - cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin, y + e.getHeight(), z - cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin, y + e.getHeight(), z - cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin, y + e.getHeight(), z + cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin, y + e.getHeight(), z + cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin, y, z + cos).color(red, green, blue, alpha).next();

        BufferRenderer.drawWithGlobalProgram(buffer.end());
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        RenderSystem.disableBlend();
        stack.pop();
    }

    public enum ShaderMode {
        Accurate, Simple
    }

    public enum Mode {
        Filled, Rect, Outline, Model, Shader
    }
}
