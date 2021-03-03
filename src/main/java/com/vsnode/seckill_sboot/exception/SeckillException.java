package com.vsnode.seckill_sboot.exception;

/**
 * 秒杀相关业务异常
 */
public class SeckillException extends RuntimeException{
    public SeckillException() {
        super();
    }

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
