package com.zhanghui.poem.controller;

import com.zhanghui.poem.Util.EsUtile;
import com.zhanghui.poem.Util.JedisPoolUntil;
import com.zhanghui.poem.Util.TPoemEs;
import com.zhanghui.poem.dao.TPoemRepository;
import com.zhanghui.poem.entity.TPoem;
import com.zhanghui.poem.service.TPoemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.List;

@RestController
@RequestMapping("elastic")
@Slf4j
public class ElasticsearchController {

    @Autowired
    private TPoemService tPoemService;

    @Autowired
    private TPoemRepository tPoemRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    //删除索引
    @RequestMapping("deleteAll")
    public void delectall() {
        tPoemRepository.deleteAll();
        log.info("删除全部开始执行-----------------------");
    }

    //重新添加索引
    @RequestMapping("insert")
    public void insertindex() {
        List<TPoem> queryall = tPoemService.queryall();
        System.out.println("'查询结果为：" + queryall);
        tPoemRepository.saveAll(queryall);
      /*  for (TPoem tPoem : queryall) {
            log.info("添加对象为：{}", tPoem)
            tPoemRepository.save(tPoem);
            log.info("----------执行成功----------------");
        }
*/
    }

    //查询所有
    @RequestMapping("findAllKeywords")
    public Iterable<TPoem> findAllKeywords() {
        Iterable<TPoem> all = tPoemRepository.findAll();
        return all;
    }

    //通过指定类类型查询
    @RequestMapping("findAllByCondition")
    public Iterable<TPoem> FindAllByCondition(TPoemEs tPoemEs) {
        log.info("搜索内容为:{}", tPoemEs.getSearchFieldValue());
        log.info("页面传来的查询索引名称为--：{}", tPoemEs.getIndex());
        log.info("页面传来的查询字段类型为--：{}", tPoemEs.getType());
        log.info("页面传来的查询Type字类型值为--：{}", tPoemEs.getTypevalue());
        log.info("页面传来的查询字段的类型为--：{}", tPoemEs.getAuthor());
        log.info("页面传来的查询作者字段的值为--：{}", tPoemEs.getAuthorvalue());
        //如果类型是所有的 作者是所有查询所有
        if ("所有".equals(tPoemEs.getTypevalue()) && "所有".equals(tPoemEs.getAuthorvalue())) {
            log.info("页面传来的查询Type字类型值为--：{}", tPoemEs.getTypevalue());
            log.info("页面传来的查询作者字段的值为--：{}", tPoemEs.getAuthorvalue());
            Iterable<TPoem> all = tPoemRepository.findAll();
            log.info("全部查询结果为:{}", all);
            return all;
        }
        log.info("页面传来的查询Type字类型值为--：{}", tPoemEs.getTypevalue());
        log.info("页面传来的查询作者字段的值为--：{}", tPoemEs.getAuthorvalue());
        //否则执行下一步进入util高亮查询
        List<TPoem> tPoems = EsUtile.queryInfo(TPoem.class, elasticsearchTemplate, tPoemEs);
        log.info("精准查询结果为:{}", tPoems);
        return tPoems;
    }

    //通过索引查询
    @RequestMapping("searchquery")
    public List<TPoem> searchquery(TPoemEs tPoemEs) {
        log.info("前台传递的内容为：{}", tPoemEs);
        List<TPoem> tPoems = EsUtile.queryInfo(TPoem.class, elasticsearchTemplate, tPoemEs);
        log.info("----搜索返回结果:{}", tPoems);
        Jedis jedis = JedisPoolUntil.getJedis();
        //根据键值判断此值是否存在
        log.info("搜索的内容为:{}", tPoemEs.getSearchFieldValue());
        Double keword = jedis.zscore("hostword", tPoemEs.getSearchFieldValue());
        log.info("查询的值为：{}" + keword);
        log.info("根据名字查询的评分为：{}", keword);
        if (keword == null) {
            //添加新的词
            jedis.zadd("hostword", 0.01, tPoemEs.getSearchFieldValue());
        } else {
            jedis.zadd("hostword", keword + 0.01, tPoemEs.getSearchFieldValue());
            //直接设置评分
        }
        return tPoems;
    }
}
