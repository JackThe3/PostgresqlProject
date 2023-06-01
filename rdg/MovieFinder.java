package rdg;
import connection.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MovieFinder{
    /**
     * finder Prebrany zo vzoroveho projektu
     */
    private static final MovieFinder INSTANCE = new MovieFinder();
    public static MovieFinder getInstance() {
        return INSTANCE;
    }

    public MovieFinder() {}

    /**
     * Najde film v databaze.
     * @param id filmu
     * @return Movie, null ak sa nenasiel.
     */
    public Movie find(Integer id) throws SQLException{
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT * FROM movies WHERE id = ?")){
            statement.setInt(1,id);
            try (ResultSet result = statement.executeQuery()){
                if (result.next()){
                    Movie movie = new Movie();
                    movie.setId(result.getInt("id"));
                    movie.setName(result.getString("name"));
                    movie.setYear(result.getInt("year"));
                    movie.setDuration(result.getInt("duration"));
                    if (result.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return movie;
                }
                else {
                    return null;
                }
            }
        }
    }

    /**
     * Najde film v databaze.
     * @param name meno filmu
     * @param year rok filmu
     * @return Movie, null ak sa nenasiel.
     */
    public  Movie find(String name, Integer year) throws SQLException{
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT * FROM movies WHERE name = ? AND year = ?")){
            statement.setInt(2,year);
            statement.setString(1, name);
            try (ResultSet result = statement.executeQuery()){
                if (result.next()){
                    Movie movie = new Movie();
                    movie.setId(result.getInt("id"));
                    movie.setName(result.getString("name"));
                    movie.setYear(result.getInt("year"));
                    movie.setDuration(result.getInt("duration"));
                    if (result.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return movie;
                }
                else {
                    return null;
                }
            }
        }
    }

    /**
     *
     * @param name meno filmu
     * @param year rok filmu
     * @return zoznam zanrov ktore ma film.
     */
    public List<String> getGenres(String name, Integer year) throws SQLException{
        List<String> genres = new ArrayList<>();
        //"SELECT * FROM movie_genres JOIN genres ON genres.id = movie_genres.genres_id WHERE name = ? AND year = ?"
        try (PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT movies.name,movies.year , genres.genre FROM movie_genres JOIN genres ON genres.id = movie_genres.genres_id JOIN movies ON movie_id = movies.id WHERE movies.name = ? AND movies.year = ?")){
            statement.setString(1, name);
            statement.setInt(2, year);
            try (ResultSet result = statement.executeQuery()){
                while (result.next()){
                    genres.add(result.getString("genre"));
                }
            }
        }
        return genres;
    }

    /**
     * @return zoznam vsetkych filmov v databaze.
     */
    public List<Movie> getAll() throws SQLException{
        List<Movie> all= new ArrayList<>();
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT * FROM movies")){
            try (ResultSet result = statement.executeQuery()){
                while (result.next()){
                    Movie movie = new Movie();
                    movie.setId(result.getInt("id"));
                    movie.setName(result.getString("name"));
                    movie.setYear(result.getInt("year"));
                    movie.setDuration(result.getInt("duration"));
                    all.add(movie);
                }
            }
        }
        return all;
    }
}
