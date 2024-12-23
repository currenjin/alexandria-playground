package com.currenjin.httpbin.adapter;

import com.currenjin.httpbin.response.AnythingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "httpBinClient", url = "${client.httpbin-api.access-url}", decode404 = true)
public interface HttpBinFeignClient {
    @GetMapping("/anything/{id}")
    AnythingResponse getAnythingById(@PathVariable("id") Long id);
}
