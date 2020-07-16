package com.zhanghui.poem.service.impl;

import com.zhanghui.poem.dao.TPoemDao;
import com.zhanghui.poem.entity.TPoem;
import com.zhanghui.poem.service.TPoemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (TPoem)表服务实现类
 *
 * @author makejava
 * @since 2020-06-22 10:10:31
 */
@Service("tPoemService")
public class TPoemServiceImpl implements TPoemService {
    @Resource
    private TPoemDao tPoemDao;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public List<TPoem> queryall() {
        List<TPoem> queryall = tPoemDao.queryall();
        return queryall;
    }

    @Override
    public TPoem selectById(String id) {
        TPoem tPoem = tPoemDao.queryById(id);
        return tPoem;
    }

    @Override
    public Map<String, Object> queryAllByLimit(int rowNum, int page) {
        HashMap<String, Object> map = new HashMap<>();
        /*
         *page：当前页
         *rows：数据
         *records：总条数
         *total ：总页数
         * */
        map.put("page", page);
        map.put("rows", tPoemDao.queryAllByLimit((page - 1) * rowNum, page * rowNum));
        int counts = tPoemDao.queryCounts();
        map.put("records", counts);
        map.put("total", (int) Math.ceil(1.0 * counts / rowNum));
        return map;
    }

    @Override
    public TPoem insert(TPoem tPoem) {
        int insert = tPoemDao.insert(tPoem);
        if (insert > 0) {
            return tPoemDao.queryById(tPoem.getId());
        }
        return null;
    }

    @Override
    public TPoem update(TPoem tPoem) {
        this.tPoemDao.update(tPoem);
        return this.selectById(tPoem.getId());
    }

    @Override
    public boolean deleteById(String id) {
        return tPoemDao.deleteById(id) > 0;
    }
}