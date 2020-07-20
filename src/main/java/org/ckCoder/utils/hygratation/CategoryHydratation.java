package org.ckCoder.utils.hygratation;

import org.ckCoder.models.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryHydratation {

    public static Category categorHidratation(ResultSet res) throws SQLException {
        Category category = new Category();
        while (res.next()) {
            category.setId(res.getInt("id"));
            category.setFlag(res.getString("flag"));
            category.setDescription(res.getString("description"));
        }
        return category;
    }
}
