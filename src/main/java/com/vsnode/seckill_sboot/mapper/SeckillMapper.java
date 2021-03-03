package com.vsnode.seckill_sboot.mapper;

import com.vsnode.seckill_sboot.pojo.entity.Seckill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface SeckillMapper {
    /**
     * 减库存
     * @param seckill
     * @param killTime
     * @return 表示更新记录行数
     */
    int reduceNumber(@Param("seckillId") long seckill,@Param("killTime") Date killTime);

    /**
     * 根据id查询秒杀对象
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset")int offset,@Param("limit")int limit);

    /**
     * 使用储存过程执行秒杀
     * @param paramMap
     */
    void killByProcedure(Map<String,Object> paramMap);
}
