package com.vsnode.seckill_sboot.constant;

/*
    使用枚举表述常量数据字段
    @author www.github.com/Acc2020
    @date  2020/4/20
*/
public enum SeckillStateEnum {
    SUCCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1, "重复秒杀"),
    INNER_ERROR(-2, "系统异常"),
    DATA_REWRITE(-3,"数据篡改");
    private int state;
    private String stateInfo;

    SeckillStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static SeckillStateEnum stateOf(int index){
        for (SeckillStateEnum state : values()) {
            if (state.getState() == index){
                return state;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}