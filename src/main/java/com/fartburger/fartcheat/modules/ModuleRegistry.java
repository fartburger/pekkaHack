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
        //CoffeeMain.log(Level.INFO, "Initialized " + instance.getName() + " via " + moduleClass.getName());
        vanillaModules.add(instance);
    }

    private static void initInner() {
        if (initialized.get()) {
            return;
        }
        initialized.set(true);
        vanillaModules.clear();

        registerModule(ClickGUI.class);
        registerModule(NoFall.class);
        registerModule(Flight.class);
        registerModule(Jesus.class);
        registerModule(NoLevitation.class);
        registerModule(NoSlow.class);
        registerModule(Xray.class);
        registerModule(FullBright.class);

        rebuildSharedModuleList();

        for (Module module : getModules()) {
            //module.postModuleInit();
        }
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
