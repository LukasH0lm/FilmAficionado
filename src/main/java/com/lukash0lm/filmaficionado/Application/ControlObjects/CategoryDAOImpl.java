package com.lukash0lm.filmaficionado.Application.ControlObjects;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CategoryDAOImpl implements CategoryDAO {

    static Connection con;
    static LinkedList<Category> categories = new LinkedList<>();

    public CategoryDAOImpl() throws SQLException {
        try {
            con = DriverManager.getConnection("jdbc:sqlserver://LAPTOP-LHL6K2JV\\MSSQLSERVER:1433;database=FilmAficionado;userName=guest;password=guest;encrypt=true;trustServerCertificate=true");
        } catch (SQLException e) {
            System.err.println("can not create connection" + e.getErrorCode() + e.getMessage());
        }

        System.out.println("connected to the database... ");


        PreparedStatement ps = con.prepareStatement("SELECT * FROM Category;");
        ResultSet rs = ps.executeQuery();



        System.out.println("Categories in database:");
        categories.clear();
        while (rs.next()) {

            categories.add(new Category(rs.getInt("ID"), rs.getString("name"), rs.getString("description"), null));
            System.out.println(rs.getInt("ID") + " - " + rs.getString("name"));
        }


        PreparedStatement ps1 = con.prepareStatement("SELECT * FROM BestMovieInCategory;");
        ResultSet rs1 = ps1.executeQuery();

        while (rs1.next()) {
            for (Category category : categories) {
                if (category.getId() == rs1.getInt("CategoryID")) {
                    category.setBestMovie(rs1.getInt("MovieID"));
                }
            }
        }





    }

    public static Category getCategoryFromID(int categoryID) throws SQLException {
        for (Category category : CategoryDAOImpl.getAllCategories()) {
            if (category.getId() == categoryID) {
                return category;
            }
        }
        return null;
    }


    @Override
    public void addCategory(Category category) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM Category;");
        ResultSet rs = ps.executeQuery();

        boolean isDuplicate = false;

        while (rs.next()) {
            if (Objects.equals(category.getTitle(), rs.getString("name")) && (Objects.equals(category.getDescription(), rs.getString("description")))) {
                isDuplicate = true;
                System.out.println(category.getTitle() + " is already in database");
                break;
            }
        }


        if (!isDuplicate) {

            try {

                int currentID = 100;

                ResultSet rs2 = ps.executeQuery();
                while (rs2.next()) {
                    if (rs2.getInt("ID") != currentID) {
                        break;
                    }
                    currentID++;
                }
                category.setID(currentID);


                PreparedStatement ps2 = con.prepareStatement("INSERT INTO Category VALUES (?,?,?);");

                ps2.setInt(1, category.getID());
                ps2.setString(2, category.getTitle());
                ps2.setString(3, category.getDescription());


                ps2.executeUpdate();
                System.out.println(category.getTitle() + " has been added to database");

                Category.addToList(category);
                categories.add(category);


            } catch (SQLException e) {

                System.err.println(e.getErrorCode() + " : " + e.getMessage());


            }
        }
    }

    @Override
    public void deleteCategory(Category category) throws SQLException {


        PreparedStatement ps = con.prepareStatement("DELETE FROM CatMovie WHERE ID=" + category.getID() + ";");
        ps.executeUpdate();

        PreparedStatement ps2 = con.prepareStatement("DELETE FROM Category WHERE ID=" + category.getID() + ";");
        ps2.executeUpdate();

        categories.remove(category);
        System.out.println(category.getTitle() + " has been deleted from database");

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
        while (rs.next()) {
            categories.add(Category.getCategoryFromID(rs.getInt("CategoryId")));
        }

        return categories;
    }

    public static LinkedList<Category> getAllCategories() throws SQLException {

        return categories;
        /*
        LinkedList<Category> categories1 = new LinkedList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Category;");
            ResultSet rs = ps.executeQuery();

            Category category;
            while (rs.next()) {
                int id = rs.getInt(1);
                String title = rs.getString(2);
                String description = rs.getString(3);


                category = new Category(id, title, description,null);


                categories1.add(category);

                System.out.println(category);
            }

            categories = categories1;
            return categories1;
        } catch (SQLException e) {
            System.err.println(e.getErrorCode() + " : " + e.getMessage());
            return null;
        }*/

        //possibly redundant code, don't know if it's needed

    }

    public void AddingBestMovieToCategory(Movie movie, Category category) throws SQLException {

        checkForCategoryInBestMovieInCategory(category);

        PreparedStatement ps2 = con.prepareStatement("UPDATE BestMovieInCategory SET MovieID=? WHERE CategoryID=?;");
        ps2.setInt(1, movie.getID());
        ps2.setInt(2, category.getID());
        ps2.executeUpdate();
        System.out.println("Best movie in category " + category.getTitle() + " has been updated to " + movie.getTitle());


    }

    public void checkForCategoryInBestMovieInCategory(Category category) throws SQLException {
            if (category.getBestMovie() == null) {
                try {
                    PreparedStatement ps = con.prepareStatement("INSERT INTO BestMovieInCategory VALUES (?,?);");
                    ps.setInt(1, category.getID());
                    ps.setInt(2, 100);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    System.err.println(e.getErrorCode() + " : " + e.getMessage());
                }
            }
        }

    public Movie getBestMovieInCategory(Category category) {
        return Movie.getMovieFromID(category.getBestMovie().getID());
    }
}



