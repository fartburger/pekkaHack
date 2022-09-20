package com.fartburger.fartcheat.event;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.events.Event;
import org.apache.logging.log4j.Level;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Events {
    static final List<ListenerEntry> entries = new CopyOnWriteArrayList<>();

    public static ListenerEntry registerEventHandler(int uniqueId, EventType event, Consumer<? extends Event> handler, int prio) {
        return registerEventHandler(uniqueId, event, handler, Events.class, prio);
    }

    public static ListenerEntry registerEventHandler(int uniqueId, EventType event, Consumer<? extends Event> handler, Class<?> owner, int prio) {
        if (entries.stream().noneMatch(listenerEntry -> listenerEntry.id == uniqueId)) {
            ListenerEntry le = new ListenerEntry(uniqueId, event, handler, owner, prio);
            entries.add(le);
            entries.sort(Comparator.comparingInt(ListenerEntry::prio));
            return le;
        } else {
            FCRMain.log(Level.WARN, uniqueId + " tried to register " + event.name() + " multiple times, unregistering previous and adding new");
            unregister(uniqueId);
            return registerEventHandler(uniqueId, event, handler, owner, prio); // yes this is recursive and no this will not repeat again because we unregistered
        }
    }

    public static void unregister(int id) {
        entries.removeIf(listenerEntry -> listenerEntry.id == id);
    }

    public static ListenerEntry registerEventHandler(EventType event, Consumer<? extends Event> handler, int prio) {
        return registerEventHandler((int) Math.floor(Math.random() * 0xFFFFFF), event, handler, prio);
    }

    public static void unregisterEventHandlerClass(Object instance) {
        for (ListenerEntry entry : new ArrayList<>(entries)) {
            if (entry.owner.equals(instance.getClass()) && !entry.type.isShouldStayRegisteredForModules()) {
                FCRMain.log(Level.INFO, "Unregistering " + entry.type + ":" + entry.id);
                entries.remove(entry);
            }
        }
    }

    public static void registerTransientEventHandlerClassEvents(Object instance) {
        for (Method declaredMethod : instance.getClass().getDeclaredMethods()) {
            for (Annotation declaredAnnotation : declaredMethod.getDeclaredAnnotations()) {
                if (declaredAnnotation.annotationType() == EventListener.class) {
                    EventListener ev = (EventListener) declaredAnnotation;
                    if (!ev.value().isShouldStayRegisteredForModules()) {
                        continue;
                    }
                    Class<?>[] params = declaredMethod.getParameterTypes();
                    if (params.length != 1 || !params[0].isAssignableFrom(ev.value().getExpectedType())) {
                        throw new IllegalArgumentException(String.format("Invalid signature: Expected %s.%s(%s) -> void, got %s.%s(%s) -> %s. Listener: %s",
                                instance.getClass().getSimpleName(),
                                declaredMethod.getName(),
                                ev.value().getExpectedType().getSimpleName(),
                                instance.getClass().getSimpleName(),
                                declaredMethod.getName(),
                                Arrays.stream(params).map(Class::getSimpleName).collect(Collectors.joining(", ")),
                                declaredMethod.getReturnType().getName(),
                                ev.value().name()));
                    } else {
                        declaredMethod.setAccessible(true);

                        ListenerEntry l = registerEventHandler((instance.getClass().getName() + declaredMethod.getName()).hashCode(), ev.value(), event -> {
                            try {
                                declaredMethod.invoke(instance, event);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }, instance.getClass(), 0);
                        FCRMain.log(Level.INFO, "Registered event handler " + declaredMethod + " with id " + l.id);
                    }
                }
            }
        }
    }

    public static void registerEventHandlerClass(Object instance) {
        for (Method declaredMethod : instance.getClass().getDeclaredMethods()) {
            for (Annotation declaredAnnotation : declaredMethod.getDeclaredAnnotations()) {
                if (declaredAnnotation.annotationType() == EventListener.class) {
                    EventListener ev = (EventListener) declaredAnnotation;
                    Class<?>[] params = declaredMethod.getParameterTypes();
                    if (params.length != 1 || !params[0].isAssignableFrom(ev.value().getExpectedType())) {
                        throw new IllegalArgumentException(String.format("Invalid signature: Expected %s.%s(%s) -> void, got %s.%s(%s) -> %s. Listener: %s",
                                instance.getClass().getSimpleName(),
                                declaredMethod.getName(),
                                ev.value().getExpectedType().getSimpleName(),
                                instance.getClass().getSimpleName(),
                                declaredMethod.getName(),
                                Arrays.stream(params).map(Class::getSimpleName).collect(Collectors.joining(", ")),
                                declaredMethod.getReturnType().getName(),
                                ev.value().name()));
                    } else {
                        declaredMethod.setAccessible(true);

                        ListenerEntry l = registerEventHandler((instance.getClass().getName() + declaredMethod.getName()).hashCode(), ev.value(), event -> {
                            try {
                                declaredMethod.invoke(instance, event);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }, instance.getClass(), 0);
                        FCRMain.log(Level.INFO, "Registered event handler " + declaredMethod + " with id " + l.id);
                    }
                }
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static boolean fireEvent(EventType event, Event argument) {

        if (!event.getExpectedType().equals(argument.getClass())) {
            throw new IllegalArgumentException(String.format("Attempted to invoke event %s with %s as event data, expected %s",
                    event.name(),
                    argument.getClass().getName(),
                    event.getExpectedType().getName()));
        }
        List<ListenerEntry> le = entries.stream().filter(listenerEntry -> listenerEntry.type == event).toList();
        if (le.size() == 0) {
            return false;
        }
        for (ListenerEntry entry : le) {
            ((Consumer) entry.eventListener()).accept(argument);
        }
        return argument.isCancelled();
    }

    public record ListenerEntry(int id, EventType type, Consumer<? extends Event> eventListener, Class<?> owner, int prio) {
    }
}
