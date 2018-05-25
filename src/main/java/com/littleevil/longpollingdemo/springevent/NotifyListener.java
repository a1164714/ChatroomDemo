package com.littleevil.longpollingdemo.springevent;

import com.littleevil.longpollingdemo.deferredreuslt.ResultVO;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotifyListener {
    private static Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap();

    public void addSseEmitters(String id, SseEmitter sseEmitter) {
        sseEmitters.put(id, sseEmitter);
    }

    @EventListener
    public void deployEventHandler(NotifyEvent notifyEvent) {
        String payload = notifyEvent.getPayload();
        String id = notifyEvent.getId();
        sseEmitters.keySet().stream().filter(sseEmitter -> !sseEmitter.equals(id)).forEach(sseEmitterKey -> {
            try {
                sseEmitters.get(sseEmitterKey).send(new ResultVO<>().setStatus(1).setData(payload));
            } catch (IOException e) {
                System.out.println(e.getStackTrace());
            }
        });
    }

    public void removeSseEmitter(String id) {
        sseEmitters.remove(id);
    }
}
