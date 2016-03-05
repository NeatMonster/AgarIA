package fr.neatmonster.agaria.events;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventManager {
    private final Map<Class<? extends Event>, Set<EventExecutor>> executors = new HashMap<>();

    public void callEvent(final Event event) {
        final Set<EventExecutor> executors = this.executors.get(event.getClass());
        if (executors != null)
            for (final EventExecutor executor : executors)
                executor.execute(event);
    }

    @SuppressWarnings("unchecked")
    public void registerEvents(final Listener listener) {
        try {
            final Method[] methods = listener.getClass().getMethods();
            for (final Method method : methods) {
                final EventHandler handler = method.getAnnotation(EventHandler.class);
                if (handler == null)
                    continue;

                final Class<?>[] paramTypes = method.getParameterTypes();
                if (paramTypes.length != 1)
                    continue;
                final Class<?> paramType = paramTypes[0];
                if (!Event.class.isAssignableFrom(paramType))
                    continue;

                method.setAccessible(true);
                for (Class<?> clazz = paramType; Event.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()) {
                    if (!executors.containsKey(clazz))
                        executors.put((Class<? extends Event>) clazz, new HashSet<EventExecutor>());
                    final EventExecutor executor = new EventExecutor() {

                        @Override
                        public void execute(final Event event) {
                            try {
                                method.invoke(listener, event);
                            } catch (final Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    executors.get(clazz).add(executor);
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
