package com.littleevil.longpollingdemo.deferredreuslt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Collection;

@Controller
@RequestMapping("/defer")
public class ChatController {

    @Autowired
    private DeferredResultUtil deferredResultUtil;

    @GetMapping("/push/{id}")
    @ResponseBody
    public DeferredResult<ResultVO> push(@PathVariable String id) {
        DeferredResult deferredResult = new DeferredResult((long) 30000);
        deferredResult.onTimeout(() -> {
            if (!deferredResult.isSetOrExpired()) {
                deferredResult.setResult(new ResultVO<>().setStatus(2));
            }
        });
        deferredResultUtil.getCacheMap().put(id, deferredResult);
        return deferredResult;
    }

    @ResponseBody
    @GetMapping("/notify/{msg}")
    public String notify(@PathVariable String msg) {
        Collection<DeferredResult> deferredResults = deferredResultUtil.getCacheMap().values();
        deferredResults.stream().forEach(deferredResult -> {
            deferredResult.setResult(new ResultVO<>().setStatus(1).setData(msg));
        });
        deferredResultUtil.getCacheMap().clear();
        return "success";
    }

    @GetMapping("/chatroom")
    public String chatroom() {
        return "defer-chatroom";
    }

}
