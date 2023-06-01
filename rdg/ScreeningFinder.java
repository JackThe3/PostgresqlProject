package rdg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import connection.DbContext;

public class ScreeningFinder {
    private static final ScreeningFinder INSTANCE = new ScreeningFinder();
    public static ScreeningFinder getInstance() {
        return INSTANCE;
    }
    public ScreeningFinder(){}

    /**
     * Najde nadchadzajuce premietania. Ak je 40 minut pred zaciatkom.
     * @return zoznam Screening
     */
    public List<Screening> getUpcoming() throws SQLException {
        List<Screening> all= new ArrayList<>();
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT * FROM screening WHERE opening BETWEEN CURRENT_TIMESTAMP AND ?")){
            long desat = VALUES.NOW().getTime() + VALUES.DESAT * 4L; //40min od teraz
            statement.setTimestamp(1,new Timestamp(desat));
            try (ResultSet result = statement.executeQuery()){
                while (result.next()){
                    Screening screening = new Screening();
                    screening.setId(result.getInt("id"));
                    screening.setPrice(result.getBigDecimal("price"));
                    screening.setOpening(result.getTimestamp("opening"));
                    screening.setMovie(MovieFinder.getInstance().find(result.getInt("movie_id")));
                    screening.setTheater(TheaterFinder.getInstance().findById(result.getInt("theater_id")));
                    all.add(screening);
                }
                return all;
            }
        }
    }

    /**
     * Najde vsetky nadchadzajuce screening.
     * @return zoznam Screening
     */
    public List<Screening> getAllUpcoming() throws SQLException{
        List<Screening> all= new ArrayList<>();
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT * FROM screening WHERE opening > CURRENT_TIMESTAMP")){
            try (ResultSet result = statement.executeQuery()){
                while (result.next()){
                    Screening screening = new Screening();
                    screening.setId(result.getInt("id"));
                    screening.setPrice(result.getBigDecimal("price"));
                    screening.setOpening(result.getTimestamp("opening"));
                    screening.setMovie(MovieFinder.getInstance().find(result.getInt("movie_id")));
                    screening.setTheater(TheaterFinder.getInstance().findById(result.getInt("theater_id")));
                    all.add(screening);
                }
                return all;
            }
        }
    }


    public Screening findById(Integer id) throws SQLException{
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT * FROM screening WHERE id = ?")){
            statement.setInt(1,id);
            try (ResultSet result = statement.executeQuery()){
                if (result.next()){
                    Screening screening = new Screening();
                    screening.setId(result.getInt("id"));
                    screening.setPrice(result.getBigDecimal("price"));
                    screening.setOpening(result.getTimestamp("opening"));
                    screening.setMovie( MovieFinder.getInstance().find(result.getInt("movie_id")));
                    screening.setTheater(TheaterFinder.getInstance().findById(result.getInt("theater_id")));
                    return screening;
                }
                else {
                    return null;
                }
            }
        }
    }
}
