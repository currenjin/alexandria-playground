package com.wemeet.core.event.contract;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UuidV7Test {

    @Test
    void 버전7_IETF_변형() {
        UUID u = UuidV7.generate();
        assertThat(u.version()).isEqualTo(7);
        assertThat(u.variant()).isEqualTo(2); // IETF
    }

    @Test
    void 매번_다른_값() {
        assertThat(UuidV7.generate()).isNotEqualTo(UuidV7.generate());
    }

    @Test
    void 앞_48비트_타임스탬프는_시간순_비감소() {
        long a = UuidV7.generate().getMostSignificantBits() >>> 16;
        long b = UuidV7.generate().getMostSignificantBits() >>> 16;
        assertThat(b).isGreaterThanOrEqualTo(a);
    }
}
