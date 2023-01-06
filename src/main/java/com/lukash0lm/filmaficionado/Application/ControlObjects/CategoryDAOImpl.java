package com.lukash0lm.filmaficionado.Application.ControlObjects;

import java.sql.*;
import java.util.LinkedList;

public class CategoryDAOImpl implements CategoryDAO {

    static Connection con;
    LinkedList<Category> categories = new LinkedList<>();
    public CategoryDAOImpl() throws SQLException {
        try{
            con = DriverManager.getConnection("jdbc:sqlserver://LAPTOP-LHL6K2JV\\MSSQLSERVER:1433;database=FilmAficionado;userName=guest;password=guest;encrypt=true;trustServerCertificate=true");
        } catch (SQLException e){
            System.err.println("can not create connection" + e.getErrorCode() + e.getMessage());
        }

        System.out.println("connected to the database... ");


        PreparedStatement ps = con.prepareStatement("SELECT * FROM Category;");
        ResultSet rs = ps.executeQuery();
        System.out.println("Categories in database:");
        while (rs.next()) {
            categories.add(new Category(rs.getInt("ID"), rs.getString("name"), rs.getString("description")));
                    System.out.println(rs.getInt("ID") + " - " + rs.getString("name"));
        }

    }



    @Override
    public void addCategory(Category category) {
    }

    @Override
    public void deleteCategory(Category category) {
    }

    @Override
    public void updateCategory(Category category) {
    }

    @Override
    public Category getCategory(String title) {
        return null;
    }
    public static LinkedList<Category> getCategoriesFromID(int id) throws SQLException {

        PreparedStatement ps = con.prepareStatement("SELECT * FROM CatMovie WHERE MovieId=?;");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        LinkedList<Category> categories = new LinkedList<>();
        while (rs.next()){
            categories.add(Category.getCategoryFromID( rs.getInt("CategoryId")));
        }

        return categories;
    }

    @Override
    public LinkedList<Category> getAllCategories() {
        return categories;
    }
}
