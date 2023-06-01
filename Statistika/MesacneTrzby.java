package Statistika;

import connection.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MesacneTrzby {
    //SELECT sum(reservations.price), genre FROM reservations JOIN screening ON reservations.screening_id = screening.id JOIN movies ON movie_id = movies.id JOIN movie_genres ON movies.id = movie_genres.movie_id JOIN genres ON genres.id = movie_genres.genres_id WHERE date_trunc('month', expire) = date_trunc('month', current_timestamp) AND reservations.status = true GROUP BY genre
    List<String> zaner = new ArrayList<>();
    List<Integer> suma = new ArrayList<>();

    public MesacneTrzby() throws SQLException {
        try (PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT sum(reservations.price), genre FROM reservations JOIN screening ON reservations.screening_id = screening.id JOIN movies ON movie_id = movies.id JOIN movie_genres ON movies.id = movie_genres.movie_id JOIN genres ON genres.id = movie_genres.genres_id WHERE date_trunc('month', expire) = date_trunc('month', current_timestamp) AND reservations.status = true GROUP BY genre")) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    suma.add(result.getInt("sum"));
                    zaner.add(result.getString("genre"));
                }
            }
        }
        System.out.println("Tento mesiac:");
        for (int i = 0; i < zaner.size(); i++) {
            System.out.println(zaner.get(i) + ": " + suma.get(i) + "€");
        }
        List<String> zaner = new ArrayList<>();
        List<Integer> suma = new ArrayList<>();

        try (PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT sum(reservations.price), genre FROM reservations JOIN screening ON reservations.screening_id = screening.id JOIN movies ON movie_id = movies.id JOIN movie_genres ON movies.id = movie_genres.movie_id JOIN genres ON genres.id = movie_genres.genres_id WHERE date_trunc('month', expire) = date_trunc('month', current_timestamp - interval '1' month) AND reservations.status = true GROUP BY genre")) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    suma.add(result.getInt("sum"));
                    zaner.add(result.getString("genre"));
                }
            }
        }
        System.out.println();
        System.out.println("Minuly mesiac:");
        for (int i = 0; i < zaner.size(); i++) {
            System.out.println(zaner.get(i) + ": " + suma.get(i) + "€");
        }
    }
}
