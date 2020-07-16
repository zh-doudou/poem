package com.zhanghui.poem.controller;

import com.zhanghui.poem.service.TCategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (TCategory)表控制层
 *
 * @author makejava
 * @since 2020-06-22 09:59:52
 */
@RestController
@RequestMapping("tCategory")
public class TCategoryController {
    /**
     * 服务对象
     */
    @Resource
    private TCategoryService tCategoryService;


}