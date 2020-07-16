package com.zhanghui.poem.Util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TPoemEs {
    private String index;   //索引名字  唯一
    private String _type = "doc";  //索引的类型   唯一
    private String type;   //查询的类型
    private String typevalue; //类型的值
    private String author;  //匹配查询的 作者字段
    private String authorvalue;  //匹配查询作者字段的值
    private String searchFieldValue; //匹配字段的值
}
