package com.zhanghui.poem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * indexName :指定操作索引的名字
 * type ：指定操作索引下的类型名
 * */
@Document(indexName = "tpoem", type = "doc")
public class TPoem implements Serializable {
    @Id   //映射id关系
    private String id;
    @Field(type = FieldType.Text, analyzer = "ik_max_word") //按字分词
    private String name;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String author;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String type;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String content;
    private String href;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String authordes;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String origin;
    @Field(type = FieldType.Keyword)
    private String imagepath;
    @Field(type = FieldType.Keyword)
    private String categoryid;


}