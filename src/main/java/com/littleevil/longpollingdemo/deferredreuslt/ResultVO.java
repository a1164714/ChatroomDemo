package com.littleevil.longpollingdemo.deferredreuslt;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResultVO<T> {
    private int status;
    private T data;
}
