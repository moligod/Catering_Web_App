package com.example.service.impl;

import com.example.dao.MenuDAO;
import com.example.model.dto.MenuDTO;
import com.example.model.entity.Menu;
import com.example.model.entity.User;
import com.example.model.vo.MenuVO;
import com.example.model.vo.ResponseVO;
import com.example.service.MenuService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class MenuServiceImpl implements MenuService {
    private MenuDAO menuDao;
    public MenuServiceImpl (MenuDAO menuDao) {
        this.menuDao = menuDao;
    }
    //查询user列表
    @Override
    public ResponseVO<List<Menu>> getMenuList(int pageSize, int page) {
        List<Menu> Menulist = menuDao.findmenulist(pageSize,page);
        for (Menu M : Menulist) {
            System.out.println(M.getId());
        }
        return new ResponseVO<>(200, "正在测试", Menulist);
    }

    @Override
    public ResponseVO<Integer> getMenulistCount() {
        int count = menuDao.findmenulistCount();
        System.out.println("总数是"+count);
        return new ResponseVO<>(200, "正在测试", count);
    }

    @Override
    public ResponseVO<Integer> addMenu(MenuDTO menuDTO) {
        Menu menu = new Menu(menuDTO.getName(),menuDTO.getDescription(),new BigDecimal((menuDTO.getPrice())),menuDTO.getCategory(),Integer.valueOf(menuDTO.getStock()));

        boolean kaiguan = menuDao.addMenu(menu);
        if (kaiguan){
            return new ResponseVO<>(200, "新增成功",null);

        }
        return new ResponseVO<>(401, "新增失败",null);
    }

    @Override
    public ResponseVO<Void> deleteMenu(String id) {
        boolean kaiguan = menuDao.delectMenu(id);
        if (kaiguan){
            return new ResponseVO<>(200, "删除成功",null);

        }
        return new ResponseVO<>(401, "删除失败",null);
    }
    @Override
    public ResponseVO<Integer> updateMenu(MenuDTO menuDTO, String id) {
        Menu menu = new Menu(Long.parseLong(id),menuDTO.getName(),menuDTO.getDescription(),new BigDecimal((menuDTO.getPrice())),menuDTO.getCategory(),Integer.valueOf(menuDTO.getStock()));

        boolean kaiguan = menuDao.updateMenu(menu);
        if (kaiguan){
            return new ResponseVO<>(200, "新增成功",null);

        }
        return new ResponseVO<>(401, "新增失败",null);
    }

}
