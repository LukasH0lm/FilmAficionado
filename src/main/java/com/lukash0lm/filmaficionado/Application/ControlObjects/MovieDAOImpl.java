package com.lukash0lm.filmaficionado.Application.ControlObjects;

import java.sql.*;
import java.util.LinkedList;

public class MovieDAOImpl implements MovieDAO {

    private static Connection con;

    private LinkedList<Movie> movies = new LinkedList<>();


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
        while (rs.next()) {
            movies.add(new Movie(rs.getString("title"), rs.getString("director"), rs.getInt("year"), rs.getString("rating"), rs.getString("description"), rs.getString("image")));
            System.out.println(rs.getInt("ID") + " - " + rs.getString("title"));
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
    public void updateMovie(Movie movie) {
        movies.remove(movie);
        movies.add(movie);
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
