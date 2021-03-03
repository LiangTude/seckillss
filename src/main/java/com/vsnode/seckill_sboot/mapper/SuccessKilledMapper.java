package com.vsnode.seckill_sboot.mapper;

import com.vsnode.seckill_sboot.pojo.entity.SuccessKilled;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SuccessKilledMapper {
    /**
     * 根据买卖明细，可以过滤重复秒杀
     * @param seckillId
     * @param userPhone
     * @return 插入的行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据id查询 successKilled 携带秒杀产品对象
     * @param seckillId
     * @param userPhone
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId")long seckillId, @Param("userPhone")long userPhone);
}
