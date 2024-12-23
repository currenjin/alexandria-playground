package com.currenjin.httpbin.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnythingResponse {
    private String data;

    public AnythingResponse(String data) {
        this.data = data;
    }
}
