package com.example.dao.impl;

import com.example.dao.MenuDAO;
import com.example.model.entity.Menu;
import com.example.model.entity.User;
import com.example.model.vo.MenuVO;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MenuDAOImpl implements MenuDAO {
    private Connection connection;

    public MenuDAOImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public List<Menu> findmenulist(int pageSize, int page) {
        String sql = "SELECT * FROM menus ORDER BY id LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            //从第一页开始，不是从第0页开始
            stmt.setInt(2, (page - 1) * pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // 处理每行数据
                return mapResultSetToMenus(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int findmenulistCount() {
        String sql = "SELECT COUNT(*) FROM menus";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                return rs.getInt("COUNT(*)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean addMenu(Menu menu) {
        String sql = "INSERT INTO menus (name,description,price,category,stock,created_at,updated_at) VALUES (?,?,?,?,?,?,?)";
        try  (PreparedStatement statement = connection.prepareStatement(sql)){
//            System.out.println(menu.getCategory()+menu.getStock());
            statement.setString(1, menu.getName());
            statement.setString(2, menu.getDescription());
            statement.setBigDecimal(3, menu.getPrice());
            statement.setString(4, menu.getCategory());
            statement.setInt(5, menu.getStock());
            statement.setTimestamp(6, java.sql.Timestamp.valueOf(menu.getCreatedAt()));
            statement.setTimestamp(7, java.sql.Timestamp.valueOf(menu.getUpdatedAt()));
            //返回的整数表示执行 SQL 语句后受影响的行数（正整数：表示受影响的行数，0：表示没有行受影响。-1：表示执行了一个返回行计数的 SQL 语句）
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean delectMenu(String id) {
        String sql = "DELETE FROM menus WHERE id = ?";
        try  (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, id);
            //返回的整数表示执行 SQL 语句后受影响的行数（正整数：表示受影响的行数，0：表示没有行受影响。-1：表示执行了一个返回行计数的 SQL 语句）
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateMenu(Menu menu) {
        String sql = "UPDATE menus SET name=?, description=?, price=?, category=?, stock=?, updated_at=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, menu.getName());
            statement.setString(2, menu.getDescription());
            statement.setBigDecimal(3, menu.getPrice());
            statement.setString(4, menu.getCategory());
            statement.setInt(5, menu.getStock());
            statement.setTimestamp(6, java.sql.Timestamp.valueOf(menu.getUpdatedAt())); // 注意这里使用UpdatedAt
            statement.setLong(7, menu.getId()); // 假设menu对象有getId()方法获取ID

            // 返回的整数表示执行 SQL 语句后受影响的行数（正整数：表示受影响的行数，0：表示没有行受影响。-1：表示执行了一个返回行计数的 SQL 语句）
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //    把从数据库查询到的数据遍历到User实体类中-多数据
    private List<Menu> mapResultSetToMenus(ResultSet resultSet) throws SQLException {
        List<Menu> Menus = new ArrayList<>();
            do {
                Menu menu = new Menu();
                menu.setId(resultSet.getLong("id"));
                menu.setName(resultSet.getString("name"));
                menu.setDescription(resultSet.getString("description"));
                menu.setPrice(resultSet.getBigDecimal("price"));
                menu.setCategory(resultSet.getString("category"));
                menu.setStock(resultSet.getInt("stock"));
                menu.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                menu.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
                Menus.add(menu);
                System.out.println("id是" + menu.getId());
            } while (resultSet.next());

        return Menus;
    }
}
