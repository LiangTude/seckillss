package com.vsnode.seckill_sboot.service.impl;

import com.vsnode.seckill_sboot.constant.SeckillStateEnum;
import com.vsnode.seckill_sboot.exception.RepeatKillException;
import com.vsnode.seckill_sboot.exception.SeckillCloseException;
import com.vsnode.seckill_sboot.exception.SeckillException;
import com.vsnode.seckill_sboot.mapper.SeckillMapper;
import com.vsnode.seckill_sboot.mapper.SuccessKilledMapper;
import com.vsnode.seckill_sboot.pojo.dto.Exposer;
import com.vsnode.seckill_sboot.pojo.dto.SeckillExecution;
import com.vsnode.seckill_sboot.pojo.entity.Seckill;
import com.vsnode.seckill_sboot.pojo.entity.SuccessKilled;
import com.vsnode.seckill_sboot.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService {
    //日志对象
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Resource
    private SeckillMapper seckillMapper;

    @Resource
    private SuccessKilledMapper successKilledMapper;

    //md5盐值字符串，用于混淆md5
    private final String slat="dfsdlgfhopsdfsald*&^HG211sdadf[gs";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillMapper.queryAll(0,10);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillMapper.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill=seckillMapper.queryById(seckillId);
        if (seckill==null){
            return new Exposer(false,seckillId);
        }
        Date startTime=seckill.getStartTime();
        Date endTime=seckill.getEndTime();
        //系统时间
        Date nowTime=new Date();
        //判断时间关系是否在秒杀时间内
        if (nowTime.getTime()<startTime.getTime()||nowTime.getTime()<endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        // 配置 URL 加密，MD5 不可逆
        String md5=getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }
    private String getMD5(long seckillId){
        String base=seckillId+"/"+slat;
        String md5=DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional
    /**
     * 使用注解控制事务方法的优点：
     * 1、开发团队达成一致约定，明确标注事务方法的编程风格。
     * 2、保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部
     * 3、不是所有方法都需要事务，如只有一条修改操作，只读操作不需要事务控制。
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws RepeatKillException, SeckillCloseException, SeckillException {
        if(md5==null||!md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        // 减少库存
        Date nowTime = new Date();
        try {
            //  减少库存成功，记录购买行为
            int insertCount = successKilledMapper.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0){
                // 重复秒杀
                throw new RepeatKillException("seckill repeat");
            } else {
                int updateCount = seckillMapper.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0){
                    // 没有更新记录 rollback
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    // 秒杀成功 commit
                    SuccessKilled successKilled = successKilledMapper.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,successKilled);
                }
            }
        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            // 编译器异常改成运行期异常，方便事务回滚
            throw new SeckillException("seckill inner error"+e.getMessage());
        }

    }

    @Override
    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        if(md5==null||!md5.equals(getMD5(seckillId))){
            return new SeckillExecution(seckillId,SeckillStateEnum.DATA_REWRITE);
        }
        Date killTime=new Date();
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("seckillId",seckillId);
        map.put("phone",userPhone);
        map.put("killTime",killTime);
        map.put("result",null);
        //执行储存过程，result被复制
        try {
            seckillMapper.killByProcedure(map);
            //获取result
            int result=MapUtils.getInteger(map,"result",-2);
            if (result==1){
                SuccessKilled sk=successKilledMapper.queryByIdWithSeckill(seckillId,userPhone);
                return new SeckillExecution(seckillId,SeckillStateEnum.SUCCESS,sk);
            }else {
                return new SeckillExecution(seckillId,SeckillStateEnum.stateOf(result));
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new SeckillExecution(seckillId,SeckillStateEnum.INNER_ERROR);

        }

    }
}
