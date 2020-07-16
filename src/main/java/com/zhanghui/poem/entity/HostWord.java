package com.zhanghui.poem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//热搜词添加
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HostWord {
    private String value;
    private Double score;
}

