package com.lukash0lm.filmaficionado.Application.ControlObjects;

import java.sql.*;
import java.util.LinkedList;

public class MovieDAOImpl implements MovieDAO {

    private static Connection con;

    private LinkedList<Movie> movies = new LinkedList<>();

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



            Movie newMovie = new Movie(rs.getInt("ID"), rs.getString("title"), rs.getString("director"), rs.getInt("year"), rs.getDouble("rating"), rs.getString("description"), CategoryDAOImpl.getCategoriesFromID(rs.getInt("ID")));
            movies.add(newMovie);
            System.out.println(newMovie);
        }

    }




    @Override
    public void addMovie(Movie movie) {
        movies.add(movie);
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


    public void addMovieCategory(Movie movie, Category category) throws SQLException {

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



}
