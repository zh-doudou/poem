package com.zhanghui.poem.dao;

import com.zhanghui.poem.entity.TPoem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface TPoemRepository extends ElasticsearchRepository<TPoem, String> {

    /*{"bool" : {"must" : {"field" : {"name" : "?"}}}}*/
/*    List<TPoem> findByName(String name);

    List<TPoem> findByNameAndPrice(String name,Double price);

    List<TPoem> findByNameOrPrice(String name,Double price);

    List<TPoem> findByNameNot(String name);

    List<TPoem> findByPriceBetween(Double first,Double last);

    List<TPoem> findByPriceAfter(String name);

    List<TPoem> findByPriceBefore(Double price);*/
}

