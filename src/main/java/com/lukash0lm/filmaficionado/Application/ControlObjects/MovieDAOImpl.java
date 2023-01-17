package com.lukash0lm.filmaficionado.Application.ControlObjects;

import java.sql.*;
import java.util.LinkedList;
import java.util.Objects;

public class MovieDAOImpl implements MovieDAO {

    private static Connection con;

    private static final LinkedList<Movie> movies = new LinkedList<>();

    CategoryDAOImpl categoryDAO = new CategoryDAOImpl();


    public MovieDAOImpl() throws SQLException {
        try{
            con = DriverManager.getConnection("jdbc:sqlserver://LAPTOP-LHL6K2JV\\MSSQLSERVER:1433;database=FilmAficionado;userName=guest;password=guest;encrypt=true;trustServerCertificate=true");
        } catch (SQLException e){
            System.err.println("can not create connection" + e.getErrorCode() + e.getMessage());
        }

        System.out.println("connected to the database... ");



        PreparedStatement ps = con.prepareStatement("SELECT * FROM Movie;");
        ResultSet rs = ps.executeQuery();



        System.out.println("Movies in database:");
        LinkedList<Category> Moviecategories = new LinkedList<>();
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

                ps2.setString(1, movie.getTitle());
                ps2.setInt(2, movie.getID());
                ps2.setInt(3, (int) movie.getRating());
                ps2.setString(4, movie.getDirector());
                ps2.setInt(5, Integer.parseInt(movie.getYear()));
                ps2.setDouble(6, movie.getImdbRating());
                ps2.setString(7, movie.getDescription());

                ps2.executeUpdate();
                System.out.println(movie.getTitle() + " has been added to database");

                Movie.addToList(movie);
                movies.add(movie);


            } catch (SQLException e) {

                System.err.println(e.getErrorCode() + " : " + e.getMessage());


            }


            for (Category category : movie.getCategories()) {
                MovieDAOImpl.addMovieCategory(movie, category);

            }
        }
    }

    public static void updateMovie(Movie movie) throws SQLException {
        PreparedStatement ps = con.prepareStatement("UPDATE Movie " +
                "SET title = ?, " +
                "director = ?, " +
                "year = ?, " +
                "rating = ?, " +
                "imdbRating = ?, " +
                "description = ? " +
                "WHERE ID = ?;");

        ps.setString(1, movie.getTitle());
        ps.setString(2, movie.getDirector());
        ps.setInt(3, Integer.parseInt(movie.getYear()));
        ps.setDouble(4, movie.getRating());
        ps.setDouble(5, movie.getImdbRating());
        ps.setString(6, movie.getDescription());
        ps.setInt(7, movie.getID());

        ps.executeUpdate();

    }

    @Override
    public void deleteMovie(Movie movie) {
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

        PreparedStatement ps = con.prepareStatement("INSERT INTO CatMovie VALUES (?,?) ;");

        ps.setInt(1, movie.getID());
        ps.setInt(2, category.getID());
        ps.executeUpdate();

    }


    @Override
    public Movie getMovie(String title) {
        for (Movie m : movies) {
            if (m.getTitle().equals(title)) {
                return m;
            }
        }
        return null;
    }

    @Override
    public LinkedList<Movie> getAllMovies() {
        return movies;
    }


    public void updateMovieDescription(Movie currentMovie) throws SQLException {

        PreparedStatement ps = con.prepareStatement("UPDATE Movie SET description = ? WHERE ID = ?;");
        ps.setString(1,currentMovie.getDescription());
        ps.setInt(2, currentMovie.getID());
        ps.executeUpdate();

    }

    public LinkedList<Category> getBestInCategories(Movie currentMovie) throws SQLException {

        LinkedList<Category> Categories = new LinkedList<>();

        PreparedStatement ps = con.prepareStatement("SELECT * FROM BestMovieInCategory Where MovieID=?;");
        ps.setInt(1, currentMovie.getID());
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            Categories.add(CategoryDAOImpl.getCategoryFromID(rs.getInt("CategoryID")));

        }
        return Categories;

    }
}
