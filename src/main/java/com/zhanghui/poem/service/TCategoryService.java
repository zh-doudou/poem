package com.zhanghui.poem.service;

import com.zhanghui.poem.entity.TCategory;

import java.util.List;

/**
 * (TCategory)表服务接口
 *
 * @author makejava
 * @since 2020-06-22 09:59:52
 */
public interface TCategoryService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    List<TCategory> selectPoemByTcategoryid(String id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<TCategory> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param tCategory 实例对象
     * @return 实例对象
     */
    TCategory insert(TCategory tCategory);

    /**
     * 修改数据
     *
     * @param tCategory 实例对象
     * @return 实例对象
     */
    TCategory update(TCategory tCategory);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

}