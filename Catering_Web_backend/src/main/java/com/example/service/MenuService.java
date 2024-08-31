package com.example.service;

import com.example.model.dto.MenuDTO;
import com.example.model.entity.Menu;
import com.example.model.vo.MenuVO;
import com.example.model.vo.ResponseVO;
import com.example.model.vo.UserVO;

import java.util.List;

public interface MenuService {
    //分页查询
    public ResponseVO<List<Menu>> getMenuList(int pageSize, int page);
    //查询总数
    public ResponseVO<Integer> getMenulistCount();
    //新增菜单
    public ResponseVO<Integer> addMenu(MenuDTO menuDTO);
    //删除菜单
    public ResponseVO<Void> deleteMenu(String id);
    //更新菜单
    public ResponseVO<Integer> updateMenu(MenuDTO menuDTO,String id);

}
