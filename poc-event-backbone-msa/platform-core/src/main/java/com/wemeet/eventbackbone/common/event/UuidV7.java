package com.wemeet.eventbackbone.common.event;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * UUIDv7 생성기 (§7.1.1) — 앞 48비트가 Unix ms 타임스탬프라 <b>시간순 정렬</b>이 된다.
 * outbox의 event_id PK/인덱스가 삽입 순서와 거의 일치해 B-tree 지역성이 좋다(랜덤 v4 대비).
 * (예제용 최소 구현. 실무는 검증된 라이브러리 권장.)
 */
public final class UuidV7 {

    private static final SecureRandom RANDOM = new SecureRandom();

    private UuidV7() {}

    public static UUID generate() {
        long timestamp = System.currentTimeMillis();     // 48-bit unix ms
        byte[] v = new byte[16];
        v[0] = (byte) (timestamp >>> 40);
        v[1] = (byte) (timestamp >>> 32);
        v[2] = (byte) (timestamp >>> 24);
        v[3] = (byte) (timestamp >>> 16);
        v[4] = (byte) (timestamp >>> 8);
        v[5] = (byte) (timestamp);

        byte[] rnd = new byte[10];
        RANDOM.nextBytes(rnd);
        System.arraycopy(rnd, 0, v, 6, 10);

        v[6] = (byte) ((v[6] & 0x0F) | 0x70);             // version 7
        v[8] = (byte) ((v[8] & 0x3F) | 0x80);             // variant 10xx

        long msb = 0, lsb = 0;
        for (int i = 0; i < 8; i++) msb = (msb << 8) | (v[i] & 0xFF);
        for (int i = 8; i < 16; i++) lsb = (lsb << 8) | (v[i] & 0xFF);
        return new UUID(msb, lsb);
    }
}
