package com.wemeet.eventbackbone.ems.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 최소 확인용 엔드포인트 — 서비스가 부팅됐는지 확인한다.
 * (이벤트 백본은 아직 안 붙었다: @EnableEventBackbone·@EventHandler·구독은 이후 단계.)
 */
@RestController
public class EmsController {

    @GetMapping("/demo/hello")
    public Map<String, String> hello() {
        return Map.of("service", "ems", "status", "up");
    }
}
