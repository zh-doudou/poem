package com.zhanghui.poem.dao;

import com.zhanghui.poem.entity.TPoem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (TPoem)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-22 10:10:31
 */
public interface TPoemDao {

    //通过ID查询单条数据
    TPoem queryById(String id);

    List<TPoem> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    //通过实体作为筛选条件查询
    List<TPoem> queryAll(TPoem tPoem);

    //新增数据
    int insert(TPoem tPoem);

    //修改数据
    int update(TPoem tPoem);

    //通过主键删除数据
    int deleteById(String id);

    //查询总条数
    int queryCounts();

    List<TPoem> queryall();


}