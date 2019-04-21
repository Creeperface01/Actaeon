package me.onebone.actaeon.hook;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author CreeperFace
 */
public class HookManager {

    private final Set<Entry> hooks = new LinkedHashSet<>();
    private final Set<Entry> activeHooks = new LinkedHashSet<>();

    private int tickRate = 3;

    public void addHook(int priority, MovingEntityHook hook) {
        hooks.add(new Entry(priority, hook));
    }

    public void removeHook(MovingEntityHook hook) {
        new ArrayList<>(hooks).forEach((e) -> {
            if (e.hook == hook) {
                if (e.active) {
                    e.active = false;
                    e.hook.reset();
                    this.activeHooks.remove(e);
                }

                hooks.remove(e);
            }
        });
    }

    public void onUpdate(final int tick) {
        if (tick % tickRate == 0) {
            for (Entry entry : hooks) {
                if (entry.active) {
                    if (!canActivate(entry) || !entry.hook.canContinue()) {
                        entry.active = false;
                        entry.hook.reset();
                        activeHooks.remove(entry);
                    }
                } else if (canActivate(entry) && entry.hook.shouldExecute()) {
                    entry.active = true;
                    entry.hook.startExecuting();
                    this.activeHooks.add(entry);
                }
            }
        } else {
            new ArrayList<>(activeHooks).forEach((e) -> {
                if (!e.hook.canContinue()) {
                    e.active = false;
                    e.hook.reset();
                    activeHooks.remove(e);
                }
            });
        }

        if (!activeHooks.isEmpty()) {
            activeHooks.forEach((e) -> e.hook.onUpdate(tick));
        }
    }

    private boolean canActivate(Entry entry) {
        if (this.activeHooks.isEmpty())
            return true;

        for (Entry running : activeHooks) {
            if (running != entry) {
                if (entry.priority >= running.priority) {
                    if (!areHooksCompatibile(running.hook, entry.hook)) {
                        return false;
                    }
                } else if (!running.hook.isInterruptible()) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean areHooksCompatibile(MovingEntityHook hook, MovingEntityHook hook2) {
        return (hook.getCompatibility() & hook2.getCompatibility()) == 0;
    }

    private class Entry {
        public final MovingEntityHook hook;
        public final int priority;
        public boolean active;

        public Entry(int priorityIn, MovingEntityHook hook) {
            this.priority = priorityIn;
            this.hook = hook;
        }

        public boolean equals(Object obj) {
            return this == obj || (obj instanceof Entry && this.hook.equals(((Entry) obj).hook));
        }

        public int hashCode() {
            return this.hook.hashCode();
        }
    }
}
