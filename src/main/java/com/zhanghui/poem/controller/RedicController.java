package com.zhanghui.poem.controller;

import com.zhanghui.poem.Util.JedisPoolUntil;
import com.zhanghui.poem.entity.HostWord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("dic")
public class RedicController {
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("findRedisKeywords")
    public List<HostWord> findRedisKeywords() {
        List<HostWord> lists = new ArrayList<>();
        //调用工具类
        Jedis jedis = JedisPoolUntil.getJedis();
        //根据分值降序排序 得到搜索的内容  取前三
        Set<String> hostword1 = jedis.zrevrange("hostword", 0, 3);
        for (String s : hostword1) {
            //根据名字查询他的评分
            Double scorebys = jedis.zscore("hostword", s);
            log.info("根据名字查询的评分为：{}", scorebys);
            HostWord hostWord = new HostWord();
            hostWord.setValue(s);
            hostWord.setScore(scorebys);
            //重新设置新的集合
            lists.add(hostWord);
        }
        System.out.println(hostword1);
        return lists;
    }
}
