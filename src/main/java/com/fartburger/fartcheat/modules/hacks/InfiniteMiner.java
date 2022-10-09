package com.fartburger.fartcheat.modules.hacks;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.Settings;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.process.ICustomGoalProcess;
import baritone.api.process.IMineProcess;
import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.config.BooleanSetting;
import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.config.StringSetting;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.FindItemResult;
import com.fartburger.fartcheat.util.InvUtils;
import com.fartburger.fartcheat.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class InfiniteMiner extends Module {

    StringSetting blockName = this.config.create(new StringSetting.Builder("diamond_ore").name("Blocks")
            .description("Blocks to mine, seperated by ';' (example: diamond_ore;diamond_block;ancient_debris")
            .get());

    DoubleSetting repair = this.config.create(new DoubleSetting.Builder(20).name("RepairPercent")
            .description("When to start repairing the tool")
            .min(10)
            .max(50)
            .precision(0)
            .get());
    DoubleSetting mine = this.config.create(new DoubleSetting.Builder(70).name("MinePercent")
            .description("When to resume mining after repairing")
            .min(50)
            .max(100)
            .precision(0)
            .get());

    BooleanSetting returnHome = this.config.create(new BooleanSetting.Builder(true).name("ReturnHome")
            .description("Should return after inventory is full")
            .get());

    BooleanSetting logout = this.config.create(new BooleanSetting.Builder(false).name("LogOut")
            .description("Should log out after inventory is full (Note; will return to home first if ReturnHome is set to true)")
            .get());

    BooleanSetting panic = this.config.create(new BooleanSetting.Builder(true).name("Panic")
            .description("Should you leave the game if health drops below a certain point")
            .get());

    DoubleSetting panicHealth = this.config.create(new DoubleSetting.Builder(7).name("PanicHealth")
            .description("What health must you be at to activate panic mode")
            .min(3)
            .max(19)
            .precision(0)
            .get());

    BlockPos.Mutable homepos = new BlockPos.Mutable();

    static boolean isrepairing = false;

    static List<Item> targetItems = new ArrayList<>();

    private final Settings baritoneSettings = BaritoneAPI.getSettings();
    private final IBaritone baritone = BaritoneAPI.getProvider().getPrimaryBaritone();
    private boolean wasminescandropitems;
    private boolean repairing;

    static String mrecentDmg;

    private static List<String> blocks2mine = new ArrayList<>();
    private static List<Block> repairBlocks = new ArrayList<>();


    public InfiniteMiner() {
        super("InfiniMiner","Mines a specific block indefinitely", ModuleType.MISC);
    }



    @Override
    public void enable() {
        Utils.chatLog("Reminder: do NOT pause baritone while using this. Just use the stop command. Your fps will drop to 0.");
        homepos.set(FCRMain.client.player.getBlockPos());
        wasminescandropitems = baritoneSettings.mineScanDroppedItems.value;
        baritoneSettings.mineScanDroppedItems.value = true;
        targetItems.add(Items.DIAMOND);
        targetItems.add(Items.ANCIENT_DEBRIS);
        Collections.addAll(blocks2mine, blockName.getValue().split(";"));
        repairBlocks.add(Blocks.COAL_ORE);
        repairBlocks.add(Blocks.REDSTONE_ORE);
        repairBlocks.add(Blocks.NETHER_QUARTZ_ORE);
    }

    @Override
    public void disable() {
        baritoneSettings.mineScanDroppedItems.value = wasminescandropitems;
        baritone.getPathingBehavior().cancelEverything();
    }

    private boolean isFull() {
        for (int i = 0; i <= 35; i++) {
            ItemStack itemStack = FCRMain.client.player.getInventory().getStack(i);

            for (Item item : targetItems) {
                if ((itemStack.getItem() == item && itemStack.getCount() < itemStack.getMaxCount())
                        || itemStack.isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void tick() {
        if(FCRMain.client.player.getDamageTracker().getMostRecentDamage()!=null) {
            mrecentDmg = Objects.requireNonNull(FCRMain.client.player.getDamageTracker().getMostRecentDamage()).getDamageSource().name;
        }
        if(isFull()) {
            if(returnHome.getValue()) {
                if(isBaritoneNotWalking()) {
                    //Utils.chatLog("[BARITONE] Returning Home");
                    baritone.getCustomGoalProcess().setGoalAndPath(new GoalBlock(homepos));
                } else if(FCRMain.client.player.getBlockPos().equals(homepos)&&logout.getValue()) {
                    logOut();
                }
            } else if(logout.getValue()) {
                logOut();
            } else {
                toggle();

                return;
            }
        }
        if(FCRMain.client.player.getHealth()<panicHealth.getValue()&&panic.getValue()) {
            panic();

            return;
        }

        if(isCreeperBouttaBlow()) {
            creeperPanic();

            return;
        }

        if (!findPickaxe()) {
            //Utils.chatError("Could not find a usable mending pickaxe.");
            toggle();
            return;
        }

        if (!checkThresholds()) {
            //Utils.chatError("Start mining value can't be lower than start repairing value.");
            toggle();
            return;
        }

        if (repairing) {
            if (!needsRepair()) {
                //Utils.chatError("Finished repairing, going back to mining.");
                repairing = false;
                mineTargetBlocks();
                return;
            }

            if (isBaritoneNotMining()) mineRepairBlocks();
        }
        else {
            if (needsRepair()) {
                //Utils.chatLog("Pickaxe needs repair, beginning repair process");
                repairing = true;
                mineRepairBlocks();
                return;
            }

            if (isBaritoneNotMining()) mineTargetBlocks();
        }
    }



    private boolean isCreeperBouttaBlow() {
        if(client.world==null) return false;
        for(Entity ent : FCRMain.client.world.getEntities()) {
            if(!(ent instanceof CreeperEntity)) continue;
            if(ent.getBlockPos().getSquaredDistance(FCRMain.client.player.getPos().x,FCRMain.client.player.getPos().y,FCRMain.client.player.getPos().z)<=6) {
                return true;
            }
        }
        return false;
    }

    private boolean needsRepair() {
        ItemStack itemStack = FCRMain.client.player.getMainHandStack();
        double toolPercentage = ((itemStack.getMaxDamage() - itemStack.getDamage()) * 100f) / (float) itemStack.getMaxDamage();
        return !(toolPercentage > mine.getValue() || (toolPercentage > repair.getValue() && !repairing));
    }

    private boolean findPickaxe() {
        Predicate<ItemStack> pickaxePredicate = (stack -> stack.getItem() instanceof PickaxeItem
                && Utils.hasEnchantments(stack, Enchantments.MENDING)
                && !Utils.hasEnchantments(stack, Enchantments.SILK_TOUCH));
        FindItemResult bestPick = InvUtils.findInHotbar(pickaxePredicate);

        if (bestPick.isOffhand()) InvUtils.quickMove().fromOffhand().toHotbar(FCRMain.client.player.getInventory().selectedSlot);
        else if (bestPick.isHotbar(bestPick.slot())) InvUtils.swap(bestPick.slot(), false);

        return InvUtils.testInMainHand(pickaxePredicate);
    }

    private boolean checkThresholds() {
        return repair.getValue() < mine.getValue();
    }

    private void mineTargetBlocks() {
        String[] array = new String[blocks2mine.size()];

        baritone.getPathingBehavior().cancelEverything();
        baritone.getMineProcess().mineByName(blocks2mine.toArray(array));
    }

    private void mineRepairBlocks() {
        Block[] array = new Block[repairBlocks.size()];

        baritone.getPathingBehavior().cancelEverything();
        baritone.getMineProcess().mine(repairBlocks.toArray(array));
    }

    private void logOut() {
        toggle();
        FCRMain.client.player.networkHandler.getConnection().disconnect(Text.of(Formatting.BLUE+"Inventory was full. Disconnected."));
    }

    private void panic() {
        toggle();
        FCRMain.client.player.networkHandler.getConnection().disconnect(Text.of(Formatting.RED+"Panic mode was activated. Your health dropped below "+panicHealth.getValue().toString()));
    }

    private void creeperPanic() {
        toggle();
        FCRMain.client.player.networkHandler.getConnection().disconnect(Text.of(Formatting.RED+"Panic mode was activated. You are in imminent danger of being blown up by a creeper."));
    }

    private boolean isBaritoneNotMining() {
        return !(baritone.getPathingControlManager().mostRecentInControl().orElse(null) instanceof IMineProcess);
    }

    private boolean isBaritoneNotWalking() {
        return !(baritone.getPathingControlManager().mostRecentInControl().orElse(null) instanceof ICustomGoalProcess);
    }

    private boolean filterBlocks(Block block) {
        return block != Blocks.AIR && block.getDefaultState().getHardness(FCRMain.client.world, null) != -1 && !(block instanceof FluidBlock);
    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}
