package com.zhanghui.poem.service.impl;

import com.zhanghui.poem.dao.TCategoryDao;
import com.zhanghui.poem.entity.TCategory;
import com.zhanghui.poem.service.TCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (TCategory)表服务实现类
 *
 * @author makejava
 * @since 2020-06-22 09:59:52
 */
@Service("tCategoryService")
public class TCategoryServiceImpl implements TCategoryService {

    @Resource
    private TCategoryDao tCategoryDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public List<TCategory> selectPoemByTcategoryid(String id) {
        List<TCategory> tCategory = tCategoryDao.selectPoemByTcategoryid(id);
        return tCategory;
    }


    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<TCategory> queryAllByLimit(int offset, int limit) {
        return this.tCategoryDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param tCategory 实例对象
     * @return 实例对象
     */
    @Override
    public TCategory insert(TCategory tCategory) {
        this.tCategoryDao.insert(tCategory);
        return tCategory;
    }

    /**
     * 修改数据
     *
     * @param tCategory 实例对象
     * @return 实例对象
     */
    @Override
    public TCategory update(TCategory tCategory) {
        this.tCategoryDao.update(tCategory);
        //return this.selectById(tCategory.getId());
        return null;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.tCategoryDao.deleteById(id) > 0;
    }
}