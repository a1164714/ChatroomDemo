package com.littleevil.longpollingdemo.springevent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Controller
@RequestMapping("/sse")
public class SseChatController {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private NotifyListener notifyListener;

    @GetMapping("/push/{id}")
    @ResponseBody
    public SseEmitter push(@PathVariable String id) {
        final SseEmitter emitter = new SseEmitter();
        emitter.onCompletion(() -> {
            notifyListener.removeSseEmitter(id);
        });
        emitter.onError((e) -> {
            emitter.complete();
            notifyListener.removeSseEmitter(id);
        });
        emitter.onTimeout(() -> {
            try {
                emitter.send("timeout");
            } catch (IOException e) {
                System.out.println(e.getStackTrace());
            }catch (IllegalStateException e){
                System.out.println("ResponseBodyEmitter is already set complete");
            }
            notifyListener.removeSseEmitter(id);
        });
        try {
            notifyListener.addSseEmitters(id, emitter);
        } catch (Exception e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    @GetMapping("/notify/{id}")
    @ResponseBody
    public String notify(@PathVariable String id, @RequestParam String payload) {
        applicationContext.publishEvent(new NotifyEvent(this).setPayload(payload).setId(id));
        return "请到监听处查看消息";
    }

    @GetMapping("/logout/{id}")
    @ResponseBody
    public String logout(@PathVariable String id) {
        notifyListener.removeSseEmitter(id);
        return "退出登陆";
    }

    @GetMapping("/chatroom")
    public String chatroom() {
        return "sse-chatroom";
    }

}
