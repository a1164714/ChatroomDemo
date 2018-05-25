package com.littleevil.longpollingdemo.springevent;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@Accessors(chain = true)
public class NotifyEvent extends ApplicationEvent {
    private String payload;
    private String id;

    public NotifyEvent(Object source) {
        super(source);
    }
}
