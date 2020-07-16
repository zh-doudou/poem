package com.zhanghui.poem;

import com.zhanghui.poem.entity.TCategory;
import com.zhanghui.poem.service.TCategoryService;
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
class PoemApplicationTests {
    @Autowired
    private TCategoryService tCategoryService;

    //查询所有分类
    @Test
    void contextLoads() {
        List<TCategory> tCategories = tCategoryService.queryAllByLimit(0, 10);
        log.info("类别查询结果：{}", tCategories);
    }

}
