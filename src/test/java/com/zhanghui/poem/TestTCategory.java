package com.zhanghui.poem;

import com.zhanghui.poem.dao.TPoemDao;
import com.zhanghui.poem.dao.TPoemRepository;
import com.zhanghui.poem.entity.TCategory;
import com.zhanghui.poem.entity.TPoem;
import com.zhanghui.poem.service.TCategoryService;
import com.zhanghui.poem.service.TPoemService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = PoemApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class TestTCategory {
    @Autowired
    private TCategoryService tCategoryService;
    //根据类别查询所有
    @Autowired
    private TPoemService tPoemService;
    @Autowired
    private TPoemDao tPoemDao;
    @Autowired
    private TPoemRepository tPoemRepository;

    @Test
    public void esaddall() {
        List<TPoem> queryall = tPoemService.queryall();
        System.out.println("'查询结果为：" + queryall);
        for (TPoem tPoem : queryall) {
            log.info("添加对象为：{}", tPoem);
            tPoemRepository.save(tPoem);
            log.info("----------执行成功----------------");
        }

    }

    @Test
    public void queryallbytccatrgoryid() {
        List<TCategory> tCategories = tCategoryService.selectPoemByTcategoryid("ed2e25b9-e885-4985-80a7-0501e2661cfc");
        log.info("根据id查询的类别为:{}", tCategories);
        for (TCategory tCategory : tCategories) {

            List<TPoem> tpoems = tCategory.getTpoems();
            for (TPoem tpoem : tpoems) {
                log.info("每个tpoem为:{}", tpoem);
            }
        }
    }

    //分页查询所有
    @Test
    public void queryall() {
        //List<TPoem> tPoems = tPoemService.queryAllByLimit(0, 20);
        //log.info("分页查询结果:{}",tPoems);
        int i = tPoemDao.queryCounts();
        log.info("查询总条数为：{}", i);
    }

}
