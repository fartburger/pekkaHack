package com.fartburger.fartcheat.modules;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.modules.hacks.*;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModuleRegistry {
    static final List<Module> vanillaModules = new ArrayList<>();
    static final List<Module> sharedModuleList = new ArrayList<>();
    static final AtomicBoolean reloadInProgress = new AtomicBoolean(false);
    static final AtomicBoolean initialized = new AtomicBoolean(false);
    static final Map<Class<? extends Module>, Module> cachedModuleClassMap = new ConcurrentHashMap<>();



    private static void rebuildSharedModuleList() {
        awaitLockOpen();
        reloadInProgress.set(true);
        sharedModuleList.clear();
        cachedModuleClassMap.clear();
        sharedModuleList.addAll(vanillaModules);
        /*
        for (AddonModuleEntry customModule : customModules) {
            sharedModuleList.add(customModule.module);
        }
         */
        reloadInProgress.set(false);
    }

    public static void init() {
        try {
            initInner();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void registerModule(Class<? extends Module> moduleClass) {
        Module instance = null;
        for (Constructor<?> declaredConstructor : moduleClass.getDeclaredConstructors()) {
            if (declaredConstructor.getParameterCount() != 0) {
                throw new IllegalArgumentException(moduleClass.getName() + " has invalid constructor: expected " + moduleClass.getName() + "(), got " + declaredConstructor);
            }
            try {
                instance = (Module) declaredConstructor.newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to make instance of " + moduleClass.getName(), e);
            }
        }
        if (instance == null) {
            throw new IllegalArgumentException("Failed to make instance of " + moduleClass.getName());
        }
        vanillaModules.add(instance);
    }

    private static void initInner() {
        if (initialized.get()) {
            return;
        }
        initialized.set(true);
        vanillaModules.clear();

        registerModule(ClickGUI.class);
        registerModule(SettingGUI.class);
        registerModule(NoFall.class);
        registerModule(Flight.class);
        registerModule(Jesus.class);
        registerModule(NoLevitation.class);
        registerModule(NoSlow.class);
        registerModule(Xray.class);
        registerModule(FullBright.class);
        registerModule(ShulkerBlocker.class);
        registerModule(FireballBlocker.class);
        registerModule(StorageESP.class);
        registerModule(PingSpoof.class);
        registerModule(KillAura.class);
        registerModule(Criticals.class);
        registerModule(Reach.class);
        registerModule(AutoTool.class);
        registerModule(SuperReach.class);
        registerModule(AllHearingEar.class);
        registerModule(AutoLavacast.class);
        registerModule(Freecam.class);
        registerModule(AllowFormatCodes.class);
        registerModule(PortalGUI.class);
        registerModule(InstaBow.class);
        registerModule(ESP.class);
        registerModule(SpinBot.class);
        registerModule(Speed.class);
        registerModule(Step.class);
        registerModule(NoBreakDelay.class);
        registerModule(BlockTagViewer.class);
        registerModule(AirPlace.class);
        registerModule(Zoom.class);
        registerModule(Tracers.class);
        registerModule(SpeedMine.class);
        registerModule(BaseFinder.class);
        registerModule(AutoClicker.class);
        registerModule(Tower.class);
        registerModule(Phase.class);
        registerModule(AirJump.class);
        registerModule(RangeNotifier.class);
        registerModule(Bart.class);
        registerModule(SnowWalk.class);
        registerModule(AntiCactus.class);
        registerModule(TrueSight.class);
        registerModule(ColorSign.class);
        registerModule(PuddleStep.class);
        registerModule(ChatEncryption.class);
        registerModule(EntityControl.class);
        registerModule(Hud.class);
        registerModule(BoatFly.class);
        registerModule(BedrockBridging.class);
        registerModule(PacketCanceller.class);
        registerModule(BlockHighlight.class);
        registerModule(Headless.class);
        registerModule(bomb.class);


        rebuildSharedModuleList();

        //FCRMain.log(Level.INFO, "Initialized modules. Vanilla modules:", vanillaModules.size(), "Addon modules:", customModules.size());
    }

    public static List<Module> getModules() {
        if (!initialized.get()) {
            init();
        }
        awaitLockOpen();
        return sharedModuleList;
    }

    private static void awaitLockOpen() {
        if (reloadInProgress.get()) {
            FCRMain.log(Level.INFO, "Locking for some time for reload to complete");
            long lockStart = System.currentTimeMillis();
            long lockStartns = System.nanoTime();
            while (reloadInProgress.get()) {
                Thread.onSpinWait();
            }
            FCRMain.log(Level.INFO, "Lock opened within " + (System.currentTimeMillis() - lockStart) + " ms (" + (System.nanoTime() - lockStartns) + " ns)");
        }

    }

    @SuppressWarnings("unchecked")
    public static <T extends Module> T getByClass(Class<T> clazz) {
        if (!initialized.get()) {
            init();
        }
        if (cachedModuleClassMap.containsKey(clazz)) {
            return (T) cachedModuleClassMap.get(clazz);
        }
        awaitLockOpen();
        for (Module module : getModules()) {
            if (module.getClass() == clazz) {
                cachedModuleClassMap.put(clazz, module);
                return (T) module;
            }
        }
        throw new IllegalStateException("Unregistered module: " + clazz.getName());
    }

    public static Module getByName(String n) {
        if (!initialized.get()) {
            init();
        }
        awaitLockOpen();
        for (Module module : getModules()) {
            if (module.getName().equalsIgnoreCase(n)) {
                return module;
            }
        }
        return null;
    }

    //public record AddonModuleEntry(Addon addon, Module module) {
    //}
}
