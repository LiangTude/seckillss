package com.vsnode.seckill_sboot.controller;

import com.vsnode.seckill_sboot.constant.SeckillStateEnum;
import com.vsnode.seckill_sboot.exception.RepeatKillException;
import com.vsnode.seckill_sboot.exception.SeckillCloseException;
import com.vsnode.seckill_sboot.pojo.dto.Exposer;
import com.vsnode.seckill_sboot.pojo.dto.SeckillExecution;
import com.vsnode.seckill_sboot.pojo.entity.Seckill;
import com.vsnode.seckill_sboot.pojo.vo.SeckillResult;
import com.vsnode.seckill_sboot.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Resource
    private SeckillService seckillService;

    @GetMapping("/list")
    public String list(Model model){
        List<Seckill> list=seckillService.getSeckillList();
        model.addAttribute("list",list);
        return "list";
    }

    @GetMapping("/{seckillId}/detail")
    public String detail(@PathVariable("seckillId")Long seckillId,Model model){
        if (seckillId==null){
            return "redirect:/seckill/list";
        }
        Seckill seckill=seckillService.getById(seckillId);
        if (seckill==null){
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }

    //ajax json
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId")Long seckillId){
        SeckillResult<Exposer> result;
        try{
            Exposer exposer=seckillService.exportSeckillUrl(seckillId);
            result=new SeckillResult<Exposer>(true,exposer);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result=new SeckillResult<Exposer>(false,e.getMessage());
        }
        return result;
    }
    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId")Long seckillId,
                                                   @PathVariable("md5")String md5,
                                                   @CookieValue(value = "killPhone",required = false)Long phone){
        //springmvc valid
        if (phone==null){
            return new SeckillResult<SeckillExecution>(false,"未注册");
        }
        SeckillResult<SeckillExecution> result;
        try {
            //存储过程调用
            SeckillExecution execution=seckillService.executeSeckillProcedure(seckillId,phone,md5);
            return new SeckillResult<SeckillExecution>(true,execution);
        }catch (RepeatKillException e){
            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true,execution);
        }catch (SeckillCloseException e){
            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStateEnum.END);
            return new SeckillResult<SeckillExecution>(true,execution);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true,execution);
        }
    }

    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now=new Date();
        return new SeckillResult<Long>(true,now.getTime());
    }
}
