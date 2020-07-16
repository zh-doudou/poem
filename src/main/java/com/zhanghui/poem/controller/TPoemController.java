package com.zhanghui.poem.controller;

import com.zhanghui.poem.dao.TPoemRepository;
import com.zhanghui.poem.entity.TPoem;
import com.zhanghui.poem.service.TPoemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

/**
 * (TPoem)表控制层
 *
 * @author makejava
 * @since 2020-06-22 10:10:31
 */
@RestController
@RequestMapping("poem")
@Slf4j
public class TPoemController {
    /**
     * 服务对象
     */
    @Autowired
    private TPoemService tPoemService;

    @Autowired
    private TPoemRepository tPoemRepository;

    @RequestMapping("findByPage")
    public Map<String, Object> findByPage(Integer rows, Integer page) {
        log.info("页面传来的rows为：{}", rows);
        log.info("页面传来的page为：{}", page);
        Map<String, Object> map = tPoemService.queryAllByLimit(rows, page);
        return map;
    }

    @RequestMapping("edit")
    public String edit(TPoem tPoem, String oper, HttpServletRequest request) {
        log.info("得到的图书数据为：{}", tPoem);

        if ("del".equals(oper)) {
            log.info("oper请求为:{}", oper);
            log.info("--------------开始执行删除--------------");
            boolean b = tPoemService.deleteById(tPoem.getId());
            log.info("删除结果为：{}" + b);
            return "delok";
        } else if ("add".equals(oper)) {
            log.info("得到的请求为：{}", oper);
            log.info("添加的信息为：{}", tPoem);
            tPoem.setId(UUID.randomUUID().toString());
            log.info("--------------开始执行添加操作--------------");
            TPoem r = tPoemService.insert(tPoem);
            log.info("添加成功，添加之后的用户数据为：{}", r);
            return "addok";
        } else if ("edit".equals(oper)) {
            log.info("得到的请求为：{}", oper);
            log.info("--------------开始执行修改操作--------------");
            TPoem update = tPoemService.update(tPoem);
            log.info("修改成功,修改之后的数据为：{}", update);
            return "editok";
        }

        return null;
    }


}