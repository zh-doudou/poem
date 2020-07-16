package com.zhanghui.poem;

import com.zhanghui.poem.dao.TPoemRepository;
import com.zhanghui.poem.entity.PosEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = PoemApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class TesCustomIK {
    @Autowired
    private TPoemRepository tPoemRepository;

    @Test
    public void add() {
        List<PosEntity> lists = new ArrayList<>();
        lists.add(new PosEntity("1", "张辉", "河北省衡水市武邑县"));
        lists.add(new PosEntity("2", "小灰灰", "阿弥托福万佛保佑"));
        //tPoemRepository.saveAll(lists);
    }
}
