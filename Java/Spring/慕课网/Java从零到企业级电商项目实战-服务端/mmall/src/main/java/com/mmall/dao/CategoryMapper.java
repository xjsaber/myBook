package com.mmall.dao;

import com.mmall.pojo.Category;

import java.util.List;

/**
 * @author xjsaber
 */
public interface CategoryMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    Category selectCategory(Integer categoryId);

    List<Category> selectChildrenParallelCategory(Integer parentId);
}