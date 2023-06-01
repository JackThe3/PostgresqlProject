package Statistika;

import rdg.Movie;
import rdg.MovieFinder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import connection.DbContext;

public class FilmMesiaca {

    List<Movie> filmy = new ArrayList<>();
    List<String> zaner = new ArrayList<>();

    public int frekvencie(List<String> list, String zaner)
    {
        Set<String> st = new HashSet<String>(this.zaner);
        for (String s : st){
            if (zaner.equals(s)){
                return Collections.frequency(list, s);
            }
        }
        return 0;
    }


    public FilmMesiaca(int pocetFilmov) throws SQLException {
        //SELECT DATE_TRUNC('month',expire), sum(reservations.price), movies.name, movies.id  FROM reservations JOIN screening ON screening_id = screening.id JOIN movies ON screening.movie_id = movies.id JOIN movie_genres ON movies.id = movie_genres.movie_id JOIN genres ON genres_id = genres.id WHERE reservations.status = true  GROUP BY DATE_TRUNC('month',expire),movies.name, movies.id HAVING DATE_TRUNC('month',expire) = ?  ORDER BY DATE_TRUNC,sum DESC LIMIT 1;"
        try (PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT DISTINCT sum, movie_id, genre FROM (SELECT sum(reservations.price), movies.id FROM reservations JOIN screening ON " +
                "reservations.screening_id = screening.id " +
                "JOIN movies ON screening.movie_id = movies.id " +
                "WHERE status = true AND expire BETWEEN date_trunc('month', current_timestamp) - (interval '6 month') AND date_trunc('month', current_timestamp) " +
                "GROUP by movies.id) AS sub JOIN movie_genres ON sub.id = movie_genres.movie_id JOIN genres ON movie_genres.genres_id = genres.id " +
                "ORDER BY genre, sum DESC")) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                     int pocet = frekvencie(zaner, (result.getString("genre")));
                     if (pocet < pocetFilmov){
                         zaner.add(result.getString("genre"));
                         filmy.add(MovieFinder.getInstance().find(result.getInt("movie_id")));
                     }
                }
            }
        }
    }

    /**
     * Vypise filmy od tohoto az po tento - 6 mesiacov podla toho ktory kolko zisku.
     */
    public void get() {
        for (int i = 0; i < filmy.size(); i++) {
            System.out.println(filmy.get(i) + " Zaner: " + zaner.get(i));
        }
    }
}
