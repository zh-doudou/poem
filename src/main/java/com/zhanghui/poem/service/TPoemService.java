package com.zhanghui.poem.service;

import com.zhanghui.poem.entity.TPoem;

import java.util.List;
import java.util.Map;

/**
 * (TPoem)表服务接口
 *
 * @author makejava
 * @since 2020-06-22 10:10:31
 */
public interface TPoemService {
    //查询所有
    List<TPoem> queryall();

    TPoem selectById(String id);

    Map<String, Object> queryAllByLimit(int rowNum, int page);

    TPoem insert(TPoem tPoem);

    TPoem update(TPoem tPoem);

    boolean deleteById(String id);

}