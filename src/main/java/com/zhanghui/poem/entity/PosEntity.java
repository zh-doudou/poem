package com.zhanghui.poem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

//添加扩展词库
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "customik", type = "doc")
public class PosEntity {
    @Id
    private String posId;
    @Field(type = FieldType.Text, analyzer = "ik_max_word") //使用ik分词器
    private String posName;
    @Field(type = FieldType.Text, analyzer = "ik_max_word") //使用ik分词器
    private String posAddress;
}