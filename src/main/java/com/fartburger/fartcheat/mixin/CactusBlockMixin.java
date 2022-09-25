package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.hacks.AntiCactus;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CactusBlock.class)
public class CactusBlockMixin extends Block {

    @Shadow @Final protected static VoxelShape COLLISION_SHAPE;

    public CactusBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if(!ModuleRegistry.getByClass(AntiCactus.class).isEnabled()) return COLLISION_SHAPE;
        return VoxelShapes.fullCube();
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if(!ModuleRegistry.getByClass(AntiCactus.class).isEnabled()) entity.damage(DamageSource.CACTUS, 1.0F);
    }
}
