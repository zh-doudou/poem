package com.zhanghui.poem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * (TCategory)实体类
 *
 * @author makejava
 * @since 2020-06-22 09:59:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TCategory implements Serializable {

    private String id;
    private String name;
    private List<TPoem> tpoems;


}