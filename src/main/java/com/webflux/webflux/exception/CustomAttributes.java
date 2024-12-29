package com.webflux.webflux.exception;


import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest webRequest, ErrorAttributeOptions options) {
        Map<String,Object> errorAttributes = new HashMap<>();
        Throwable throwable = super.getError(webRequest);
        if(throwable instanceof CustomException customException){
            errorAttributes.put("status",customException.getStatus());
            errorAttributes.put("message",customException.getMessage());
        }
        return  errorAttributes;
    }
}

