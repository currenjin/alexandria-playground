package com.wemeet.eventbackbone.common.event;


import java.util.HashMap;
import java.util.Map;

/**
 * 이벤트 타입 레지스트리 (§7.1.1 계약 레지스트리의 예제판).
 * class ↔ eventType 양방향 매핑 + 토픽 유도(§7.1.2: 앞 두 마디).
 * 예제는 명시 등록. 실제는 contracts 모듈 스캔 + CI 게이트.
 */
public final class EventTypes {
    private static final Map<Class<?>, String> classToType = new HashMap<>();
    private static final Map<String, Class<?>> typeToClass = new HashMap<>();

    private EventTypes() {}

    public static void register(Class<? extends DomainEvent> clazz) {
        EventContract c = clazz.getAnnotation(EventContract.class);
        if (c == null) throw new IllegalArgumentException("@EventContract 없음: " + clazz);
        classToType.put(clazz, c.type());
        typeToClass.put(c.type(), clazz);
    }

    public static String typeOf(Class<?> clazz) {
        String t = classToType.get(clazz);
        if (t == null) throw new IllegalStateException("미등록 이벤트: " + clazz);
        return t;
    }
    public static int versionOf(Class<?> clazz) {
        return clazz.getAnnotation(EventContract.class).version();
    }
    public static Class<?> classOf(String type) {
        Class<?> c = typeToClass.get(type);
        if (c == null) throw new IllegalStateException("미등록 타입: " + type);
        return c;
    }

    /** 토픽 = eventType 앞 두 마디 (§7.1.2). a.b.created → a.b */
    public static String topicOf(String eventType) {
        String[] p = eventType.split("\\.");
        return p.length >= 2 ? p[0] + "." + p[1] : eventType;
    }
}
