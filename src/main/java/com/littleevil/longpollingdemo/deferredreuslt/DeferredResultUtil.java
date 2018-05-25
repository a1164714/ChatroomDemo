package com.littleevil.longpollingdemo.deferredreuslt;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DeferredResultUtil {
    public Map<String, DeferredResult> getCacheMap() {
        return cacheMap;
    }

    private Map<String, DeferredResult> cacheMap;

    public DeferredResultUtil() {
        this(200);
    }

    public DeferredResultUtil(int cap) {
        cacheMap = new ConcurrentHashMap<>(cap);
    }
}
