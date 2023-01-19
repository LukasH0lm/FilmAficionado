package com.lukash0lm.filmaficionado.Application.ControlObjects;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.Objects;

public class MovieDAOImpl implements MovieDAO {

    private static Connection con;

    private static final LinkedList<Movie> movies = new LinkedList<>();

    public MovieDAOImpl() throws SQLException, IOException {
        try {
            con = DriverManager.getConnection("jdbc:sqlserver://LAPTOP-LHL6K2JV\\MSSQLSERVER:1433;database=FilmAficionado;userName=guest;password=guest;encrypt=true;trustServerCertificate=true");
        } catch (SQLException e) {
            System.err.println("can not create connection" + e.getErrorCode() + e.getMessage());
        }

        System.out.println("connected to the database... ");


        PreparedStatement ps = con.prepareStatement("SELECT * FROM Movie;");
        ResultSet rs = ps.executeQuery();


        System.out.println("Movies in database:");
        while (rs.next()) {


            Movie newMovie = new Movie(rs.getInt("ID"), rs.getString("title"), rs.getString("director"), rs.getInt("year"), rs.getDouble("rating"), rs.getDouble("imdbRating"), rs.getString("description"), CategoryDAOImpl.getCategoriesFromID(rs.getInt("ID")));
            movies.add(newMovie);
            System.out.println(newMovie);
        }

    }


    public static void addMovie(Movie movie) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM Movie;");
        ResultSet rs = ps.executeQuery();

        boolean isDuplicate = false;

        while (rs.next()) {
            if (Objects.equals(movie.getTitle(), rs.getString("title"))) {
                isDuplicate = true;
                System.out.println(movie.getTitle() + " is already in database");
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
                movie.setID(currentID);


                PreparedStatement ps2 = con.prepareStatement("INSERT INTO Movie VALUES (?,?,?,?,?,?,?);");

                ps2.setInt(1, movie.getID());
                ps2.setString(2, movie.getTitle());
                ps2.setString(3, movie.getDirector());
                ps2.setInt(4, Integer.parseInt(movie.getYear()));
                ps2.setString(5, movie.getDescription());
                ps2.setDouble(6, movie.getImdbRating());
                ps2.setInt(7, (int) movie.getRating());

                ps2.executeUpdate();
                System.out.println(movie.getTitle() + " has been added to database");

                Movie.addToList(movie);
                movies.add(movie);

                for (Category category : movie.getCategories()) {
                    MovieDAOImpl.addMovieCategory(movie, category);

                }


            } catch (SQLException e) {

                System.err.println(e.getErrorCode() + " : " + e.getMessage());


            }
        }
    }

    public static void updateMovie(Movie movie) throws SQLException {
        PreparedStatement ps = con.prepareStatement("UPDATE Movie " +
                "SET title = ?, " +
                "director = ?, " +
                "year = ?, " +
                "description = ?, " +
                "rating = ?, " +
                "imdbRating = ? " +
                "WHERE ID = ?;");

        ps.setString(1, movie.getTitle());
        ps.setString(2, movie.getDirector());
        ps.setInt(3, Integer.parseInt(movie.getYear()));
        ps.setString(4, movie.getDescription());
        ps.setDouble(5, movie.getRating());
        ps.setDouble(6, movie.getImdbRating());
        ps.setInt(7, movie.getID());

        ps.executeUpdate();

        removeMovieCategories(movie);

        for (Category category : movie.getCategories()) {
            addMovieCategory(movie, category);

        }

    }

    private static void removeMovieCategories(Movie movie) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM CatMovie WHERE movieID = ?;");
            ps.setInt(1, movie.getID());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getErrorCode() + " : " + e.getMessage());
        }
    }

    public static LinkedList<Movie> getLowestRatedMovies() {
        LinkedList<Movie> lowestRatedMovies = new LinkedList<>();
        for (Movie movie : movies) {
            if (movie.getRating() < 3) {
                lowestRatedMovies.add(movie);
            }
        }
        return lowestRatedMovies;
    }


    public static void deleteMovie(Movie movie) throws SQLException {

        //slet bestMovieInCategory
        CategoryDAOImpl.deleteBestMovieFromAllCategories(movie);
        //slet movieCategory
        CategoryDAOImpl.deleteMovieFromAllCategories(movie);
        //slet movie
        PreparedStatement ps = con.prepareStatement("DELETE FROM Movie WHERE ID = ?;");
        ps.setInt(1, movie.getID());
        ps.executeUpdate();
        movies.remove(movie);
    }

    @Override
    public void updateMovieRating(Movie movie) throws SQLException {

        PreparedStatement ps = con.prepareStatement("UPDATE Movie SET rating = ? WHERE ID = ?;");

        ps.setInt(1, (int) movie.getRating());
        ps.setInt(2, movie.getID());
        ps.executeUpdate();

    }


    public static void addMovieCategory(Movie movie, Category category) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM CatMovie;");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            if (rs.getInt("movieID") == movie.getID() && rs.getInt("categoryID") == category.getID()) {
                System.out.println(movie.getTitle() + " is already in category " + category.getTitle());
                return;
            }
        }

        PreparedStatement ps1 = con.prepareStatement("INSERT INTO CatMovie VALUES (?,?) ;");

        ps1.setInt(1, category.getID());
        ps1.setInt(2, movie.getID());
        ps1.executeUpdate();
        System.out.println("added: " + movie.getTitle() + " to category " + category.getTitle());

    }


    @Override
    public LinkedList<Movie> getAllMovies() {
        return movies;
    }


    public void updateMovieDescription(Movie currentMovie) throws SQLException {

        PreparedStatement ps = con.prepareStatement("UPDATE Movie SET description = ? WHERE ID = ?;");
        ps.setString(1, currentMovie.getDescription());
        ps.setInt(2, currentMovie.getID());
        ps.executeUpdate();

    }

    public LinkedList<Category> getBestInCategories(Movie currentMovie) throws SQLException {

        LinkedList<Category> Categories = new LinkedList<>();

        PreparedStatement ps = con.prepareStatement("SELECT * FROM BestMovieInCategory Where MovieID=?;");
        ps.setInt(1, currentMovie.getID());
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Categories.add(CategoryDAOImpl.getCategoryFromID(rs.getInt("CategoryID")));

        }
        return Categories;

    }
}
