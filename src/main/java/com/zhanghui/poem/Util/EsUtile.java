package com.zhanghui.poem.Util;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class EsUtile {
    /**
     * 1..
     * Class<T>clzz:需要的是一个类对象,返回的就是泛型
     * aggregatedPage：
     * index:索引名字
     * type：索引的类型
     * searchField：匹配查询的字段
     * searchFieldValue:匹配字段的值
     * descField；排序的字段
     */
    public static <T> List<T> queryInfo(Class<T> clazz, ElasticsearchTemplate elasticsearchTemplate,
                                        TPoemEs tPoemEs
    ) {
        log.info("传过来的对象为：{}", tPoemEs);
        HighlightBuilder.Field field = new HighlightBuilder.Field("*");
        NativeSearchQuery searchQuery = null;
        //判断条件1  如果类别都是为所有    作者 不为所有  执行精准查询
        if ("所有".equals(tPoemEs.getTypevalue()) && !"所有".equals(tPoemEs.getAuthorvalue())) {
            log.info("1.-------------------------------按作者开始查询----------------------");

            //构建查询对象
            searchQuery = new NativeSearchQueryBuilder()
                    //指定查询的索引  查询的类型
                    .withIndices(tPoemEs.getIndex()).withTypes(tPoemEs.get_type())
                    //指定查询方式      匹配查询
                    .withQuery(QueryBuilders.matchQuery(tPoemEs.getAuthor(), tPoemEs.getAuthorvalue()))
                    //指定分页
                    .withPageable(PageRequest.of(0, 30))
                    //指定排序方式                  按价格排序      排序方式:降序
                    //.withSort(SortBuilders.fieldSort(secach.getDescField()).order(SortOrder.DESC))
                    //高亮查询
                    .withHighlightFields(field)
                    .build();
            //判断条件2 ：如果输入的类型值 不为所有      &&   作者不为所有 就进行过滤查询
        } else if (!"所有".equals(tPoemEs.getTypevalue()) && tPoemEs.getTypevalue() != null
                && !"所有".equals(tPoemEs.getAuthorvalue()) && tPoemEs.getAuthorvalue() != null) {
            log.info("2.--------------------------------过滤查询开始----------------------");
            searchQuery = new NativeSearchQueryBuilder()
                    //指定查询的索引  查询的类型
                    .withIndices(tPoemEs.getIndex()).withTypes(tPoemEs.get_type())
                    //指定查询方式  bool综合查询
                    .withQuery(QueryBuilders.boolQuery()
                            //添加过滤查询  类别
                            .filter(QueryBuilders.termQuery(tPoemEs.getType(), tPoemEs.getTypevalue()))
                            //精准查询  作者
                            .must(QueryBuilders.matchQuery(tPoemEs.getAuthor(), tPoemEs.getAuthorvalue())))
                    //指定分页
                    .withPageable(PageRequest.of(0, 30))
                    //高亮查询
                    .withHighlightFields(field)
                    .build();
            //判断3： 如果输入的类型值 为所有  &&  输入的作者值为所有 ----过滤类型下的所有
        } else if (!"所有".equals(tPoemEs.getTypevalue()) && "所有".equals(tPoemEs.getAuthorvalue())) {
            log.info("3.-----------------分类查询开始-------------");
            searchQuery = new NativeSearchQueryBuilder()
                    //指定查询的索引  查询的类型
                    .withIndices(tPoemEs.getIndex()).withTypes(tPoemEs.get_type())
                    //指定查询方式  bool综合查询
                    .withQuery(QueryBuilders.boolQuery()
                            //添加过滤查询  类别
                            .filter(QueryBuilders.termQuery(tPoemEs.getType(), tPoemEs.getTypevalue()))
                            //查询所有作者
                            .must(QueryBuilders.matchAllQuery()))
                    //指定分页
                    .withPageable(PageRequest.of(0, 30))
                    //指定排序方式                  按价格排序      排序方式:降序
                    //.withSort(SortBuilders.fieldSort("_score").order(SortOrder.DESC))
                    //高亮查询
                    .withHighlightFields(field)
                    .build();
        } else if (tPoemEs.getSearchFieldValue() != null) {
            log.info("搜索的内容为：{}", tPoemEs.getSearchFieldValue());
            searchQuery = new NativeSearchQueryBuilder()
                    //指定查询的索引  查询的类型
                    .withIndices(tPoemEs.getIndex()).withTypes(tPoemEs.get_type())
                    //指定查询方式  bool综合查询
                    .withQuery(QueryBuilders.boolQuery()
                            .must(QueryBuilders.termQuery("name", tPoemEs.getSearchFieldValue())))
                    //指定分页
                    .withPageable(PageRequest.of(0, 30))
                    //高亮查询
                    .withHighlightFields(field)
                    .build();
        }

        //执行查询
        AggregatedPage<T> aggregatedPage = elasticsearchTemplate.queryForPage(searchQuery, clazz, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> aClass, Pageable pageable) {
                //获取查询结果中的所有文档
                SearchHit[] hits = response.getHits().getHits();
                //最终要求返回的为一个List集合
                ArrayList<T> list = new ArrayList<>();
                for (SearchHit hit : hits) {
                    //获取id
                    String id = hit.getId();
                    //获取原生的结果
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                    sourceAsMap.put("_id", id);
                    //log.info("原生map结果为：{}", sourceAsMap);
                    // 获取的高亮map集合为:{name=[name], fragments[[<em>peach</em>]]}
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    // log.info("获取的高亮map集合为:{}", highlightFields);

                    /**
                     entrySet是 ：java中 键-值 对的集合，Set里面的类型是Map.Entry，
                     一般可以通过map.entrySet()得到。
                     */
                    Set<Map.Entry<String, Object>> entries = sourceAsMap.entrySet();
                    //log.info("sourceAsMap.entrySet键值对转化后:{}", entries);
                    //键值对遍历
                    for (Map.Entry<String, Object> entry : entries) {
                        //高亮map集合highlightFields      通过getkey(entries集合中的getkey)
                        HighlightField highlightField = highlightFields.get(entry.getKey());
                        //如果获取的key都可以和字段匹配上
                        if (highlightField != null) {
                            //System.out.println("highlightField.fragments()[0].toString()"+highlightField.fragments()[0].toString());
                            //highlightField.fragments()[0].toString():将高亮字段转化为字符串
                            //根据键  把当前遍历的值 重新添加到原生map集合中  相当于覆盖
                            sourceAsMap.put(entry.getKey(), highlightField.fragments()[0].toString());
                        }

                    }
                    // log.info("重新设置后的原生map为：{}", sourceAsMap);


                    try {
                        /**
                         *   自定义结果集的封装
                         newInstance:反射创建类的对象
                         * */

                        T t = (T) clazz.newInstance();
                        // 反射获取所有的类中的字段
                        Field[] fields = clazz.getDeclaredFields();
                        for (Field field : fields) {
                            // 设置运行反射操作属性
                            field.setAccessible(true);
                            // 获取属性名
                            String name = field.getName();
                            // log.info("循环遍历获取的属性name名为:{}", name);
                            // 获取属性类型
                            Class<?> type = field.getType();
                            //log.info("循环遍历获取的类型type为:{}", type);
                            //根据当前这个字段的名字，到map中获取值，而得到的值就是要封装到当前字段的值
                            Object value = sourceAsMap.get(name);
                            // log.info("封装之前字段值为：{}", value);
                            // 给对象的属性反射赋值的方法 : 参数一 需要指定操作的对象   参数二 是给属性的赋值
                            if (type.equals(Double.class)) {
                                String strValue = (String) value;
                                Double doubleValue = Double.valueOf(strValue);
                                field.set(t, doubleValue);
                            } else if (type.equals(Integer.class)) {
                                String strValue = (String) value;
                                Integer integerValue = Integer.valueOf(strValue);
                                field.set(t, integerValue);
                            } else if (type.equals(Date.class)) {
                                String strValue = (String) value;
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = dateFormat.parse(strValue);
                                field.set(t, date);
                            } else {
                                field.set(t, value);
                            }
                        }
                        list.add(t);
                    } catch (ParseException | IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
                //将新设置后的map集合装载到list集合中用来返回
                //创建一个新的载体  用来传输 list
                return new AggregatedPageImpl(list);
            }
        });
        return aggregatedPage.getContent();
    }
}
