package com.vsnode.seckill_sboot.service;

import com.vsnode.seckill_sboot.exception.RepeatKillException;
import com.vsnode.seckill_sboot.exception.SeckillCloseException;
import com.vsnode.seckill_sboot.exception.SeckillException;
import com.vsnode.seckill_sboot.pojo.dto.Exposer;
import com.vsnode.seckill_sboot.pojo.dto.SeckillExecution;
import com.vsnode.seckill_sboot.pojo.entity.Seckill;

import java.util.List;

public interface SeckillService {
    /**
     * 查询所有秒杀记录
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录通过id
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开始时才输出秒杀地址，否则输出系统时间和秒杀时间，用于等待，
     * 不到时间不放出秒杀地址，地址使用加密
     * @param seckillId
     * @return
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     *执行秒杀操作
     */
    SeckillExecution executeSeckill(long seckillId,long userPhone,String md5) throws RepeatKillException, SeckillCloseException, SeckillException;

    /**
     * 执行秒杀操作by储存过程
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    SeckillExecution executeSeckillProcedure(long seckillId,long userPhone,String md5);


}
