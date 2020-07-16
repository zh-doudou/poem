package com.zhanghui.poem;

import com.zhanghui.poem.Util.JedisPoolUntil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.Set;

@SpringBootTest(classes = PoemApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class redisTest {
    //添加
    @Test
    public void test1() {
        Jedis jedis = JedisPoolUntil.getJedis();
        //jedis.zadd("hostword",0.01,"哈哈");
        //jedis.zadd("hostword",0.51,"李白");
        //jedis.zadd("hostword",0.53,"杜甫");

        Set<String> hostword = jedis.zrange("hostword", 0, 3);
        log.info("获取得到的集合：{}", hostword);
        Set<String> hostword1 = jedis.zrevrange("hostword", 0, 3);

        for (String s : hostword1) {
            Double scorebys = jedis.zscore("hostword", s);
            log.info("根据名字查询的评分为：{}", scorebys);
            log.info(s);
        }
        System.out.println(hostword1);
    }

}