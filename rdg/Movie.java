package rdg;
import connection.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class Movie {
    Integer id;
    String name;
    Integer year;
    Integer duration;

    public List<String> getGenres() throws SQLException {
        return MovieFinder.getInstance().getGenres(name, year);
    }

    public Movie() {
    }

    /**
     * Zmaze zaner filmu.
     * @param genre meno zanru
     * @return true ak sa operacia podarila, false ak nie
     */
    public boolean deleteGenre(String genre) throws SQLException {
        List<String> zanre = MovieFinder.getInstance().getGenres(name, year);
        if (!zanre.contains(genre.toUpperCase())){
            System.out.println("Neexistujuci zaner!");
            return false;
        }
            Genre g =  GenreFinder.getInstance().findByName(genre.toUpperCase());
            if (g == null){
                System.out.println("Neexistujuci zaner!");
                return false;
            }
            try (PreparedStatement statement = DbContext.getConnection().prepareStatement("DELETE FROM movie_genres WHERE movie_id = ? AND genres_id = ?")) {
                statement.setInt(1, id);
                statement.setInt(2, g.getId());
                statement.executeUpdate();
                return true;
            }

    }

    /**
     * Prida zaner ku filmu(Movie moze mat viac zanrov)
     * @param genre  meno zanru
     * @return true ak sa operacia podarila, false ak nie
     */
    public boolean setGenres(String genre) throws SQLException {
        Movie film = MovieFinder.getInstance().find(name, year);
        List<String> zanre = MovieFinder.getInstance().getGenres(name, year);
        if (zanre.contains(genre.toUpperCase())){
            System.out.println("Neexistujuci zaner!");
            return false;
        }
        Genre g =  GenreFinder.getInstance().findByName(genre.toUpperCase());
        if (g == null){
            System.out.println("Neexistujuci zaner!");
            return false;
        }
        try (PreparedStatement statement = DbContext.getConnection().prepareStatement("INSERT INTO movie_genres(movie_id, genres_id) VALUES(?,?)")){
            statement.setInt(1, film.getId());
            statement.setInt(2, g.getId());
            statement.executeUpdate();
            return true;
        }
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setYear(Integer year){
        this.year = year;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", year=" + year +
                ", duration=" + duration +
                '}';
    }

    public String getName() {
        return name;
    }

    public Integer getDuration() {
        return duration;
    }


    /**
     *Prida film do databazy.
     */
    public void insert() throws SQLException{
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("INSERT INTO movies(name, year, duration) VALUES(?,?,?)",Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, name);
            statement.setInt(2, year);
            statement.setInt(3, duration);
            statement.executeUpdate();
            try(ResultSet resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                id = resultSet.getInt(1);
            }
        }
    }

    /**
     *Upravy film v databaze.
     */
    public void update() throws SQLException{
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("UPDATE movies SET name=?, year=?, duration=? WHERE id = ?")) {
            statement.setString(1, name);
            statement.setInt(2, year);
            statement.setInt(3, duration);
            statement.setInt(4, id);
            statement.executeUpdate();
        }
    }

    /**
     *Zmaze film z databazy.
     */
    public void delete() throws SQLException {
        List<String> zanre = MovieFinder.getInstance().getGenres(name, year);
        for (String genre : zanre) {
            deleteGenre(genre);
        }
        try (PreparedStatement statement = DbContext.getConnection().prepareStatement("DELETE FROM movies WHERE name = ? AND year = ?")) {
            statement.setString(1, name);
            statement.setInt(2, year);
            statement.executeUpdate();

        }
    }
}
