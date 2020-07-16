package org.ckCoder.service;

import org.ckCoder.models.Category;
import org.ckCoder.service.contract.IService;
import org.ckCoder.service.contract.Service;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class CategoryService extends Service implements IService<Category, Long> {

    @Override
    public Category create(Category category) {

        try {
            CallableStatement statement = connection.prepareCall("CALL save_category(?,?)");
            statement.setString(1, category.getFlag());
            statement.setString(2, category.getDescription());

            boolean result = statement.execute();
            if (result) {
                ResultSet set = statement.getResultSet();
                while (set.next()) {
                    category.setId(set.getInt("id"));
                    category.setFlag(set.getString("flag"));
                    category.setDescription(set.getString("flag"));
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return category;
    }

    @Override
    public Category update(Category category) {

        try {
            CallableStatement statement = connection.prepareCall("CALL update_category(?,?,?)");
            statement.setInt(1, (int) category.getId());
            statement.setString(2, category.getFlag());
            statement.setString(3, category.getDescription());

            boolean result = statement.execute();
            if (result) {
                ResultSet set = statement.getResultSet();
                while (set.next()) {
                    category.setId(set.getInt("id"));
                    category.setFlag(set.getString("flag"));
                    category.setDescription(set.getString("flag"));
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return category;
    }

    @Override
    public boolean delete(Long aLong) {
        return false;
    }

    @Override
    public Set<Category> findAll(Category category) {

        Set<Category> categories = new HashSet<>();
        try {
            CallableStatement statement = connection.prepareCall("CALL fetch_category(?)");
            statement.setInt(1, (int) category.getId());

            boolean result = statement.execute();
            if (result) {
                ResultSet set = statement.getResultSet();
                while (set.next()) {
                    categories.add(this.getCategoryFromResulset(set));
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return categories;
    }

    @Override
    public Category findById(Long aLong) {
        return null;
    }

    private Category getCategoryFromResulset(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setId(resultSet.getInt("id"));
        category.setFlag(resultSet.getString("flag"));
        category.setDescription(resultSet.getString("flag"));

        return category;
    }

}
