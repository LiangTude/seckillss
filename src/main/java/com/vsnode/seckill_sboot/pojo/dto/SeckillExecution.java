package com.vsnode.seckill_sboot.pojo.dto;

import com.vsnode.seckill_sboot.constant.SeckillStateEnum;
import com.vsnode.seckill_sboot.pojo.entity.SuccessKilled;

/**
 * 封装秒杀执行后结果
 */
public class SeckillExecution {
    private long seckillId;
    // 秒杀执行结果状态
    private int state;
    // 秒杀返回信息：状态表示
    private String stateInfo;
    // 秒杀成功对象
    private SuccessKilled successKilled;

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "seckillId=" + seckillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", successKilled=" + successKilled +
                '}';
    }

    public SeckillExecution(long seckillId, SeckillStateEnum statEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    // 返回失败的构造函数
    public SeckillExecution(long seckillId, SeckillStateEnum statEnum) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
    }
}
