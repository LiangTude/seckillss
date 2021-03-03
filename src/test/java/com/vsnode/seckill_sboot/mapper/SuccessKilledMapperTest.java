package com.vsnode.seckill_sboot.mapper;

import com.vsnode.seckill_sboot.pojo.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SuccessKilledMapperTest {

    @Autowired
    private SuccessKilledMapper successKilledMapper;

    @Test
    public void insertSuccessKilled() {
        long id =1000L;
        long phone = 18226561002L;
        int i = successKilledMapper.insertSuccessKilled(id, phone);
        System.out.println(i);

    }

    @Test
    public void queryByIdWithSeckill() {
        long id =1000L;
        long phone = 18226561002L;
        SuccessKilled successKilled = successKilledMapper.queryByIdWithSeckill(id, phone);
        System.out.println(successKilled);
    }
}