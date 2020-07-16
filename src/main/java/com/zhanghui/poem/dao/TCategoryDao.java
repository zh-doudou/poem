package com.zhanghui.poem.dao;

import com.zhanghui.poem.entity.TCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (TCategory)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-22 09:59:51
 */
public interface TCategoryDao {

    //级联查询
    List<TCategory> selectPoemByTcategoryid(String id);

    List<TCategory> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    List<TCategory> queryAll(TCategory tCategory);

    int insert(TCategory tCategory);

    int update(TCategory tCategory);

    int deleteById(String id);


}