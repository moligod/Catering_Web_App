package com.example.dao;

import com.example.model.entity.Menu;
import com.example.model.vo.MenuVO;

import java.util.List;

public interface MenuDAO {
    //分页查询
    List<Menu> findmenulist(int pageSize, int page);
    //获取总页数
    int findmenulistCount();
    //添加菜单
    boolean addMenu(Menu menu);
    //删除菜单
    boolean delectMenu(String id);
//    更新
    boolean updateMenu(Menu menu);
}
