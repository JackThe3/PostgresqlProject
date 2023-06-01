package rdg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.DbContext;

public class TheaterFinder {

    public static final TheaterFinder INSTANCE = new TheaterFinder();

    public static TheaterFinder getInstance(){
        return INSTANCE;
    }

    public TheaterFinder() {}

    /**
     * @return Zoznam vsetkych divadiel.
     */
    public List<Theater> getAll() throws SQLException {
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT * FROM theater")){
            try (ResultSet result = statement.executeQuery()){
                List<Theater> all= new ArrayList<>();
                while (result.next()){
                    Theater theater = new Theater();
                    theater.setId(result.getInt("id"));
                    theater.setName(result.getString("name"));
                    theater.setNumberOfSeats(result.getInt("numberofseats"));
                    all.add(theater);
                }
                return all;
            }
        }

    }

    public Theater find(String name) throws SQLException{
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT * FROM theater WHERE name = ?")){
            statement.setString(1,name);
            try (ResultSet result = statement.executeQuery()){
                if (result.next()){
                    Theater theater = new Theater();
                    theater.setId(result.getInt("id"));
                    theater.setName(result.getString("name"));
                    return theater;
                }
                else {
                    return null;
                }
            }
        }
    }

    public Theater findById(Integer id) throws SQLException{
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT * FROM theater WHERE id = ?")){
            statement.setInt(1,id);
            try (ResultSet result = statement.executeQuery()){
                if (result.next()){
                    Theater theater = new Theater();
                    theater.setId(result.getInt("id"));
                    theater.setName(result.getString("name"));
                    theater.setNumberOfSeats(result.getInt("numberofseats"));
                    return theater;
                }
                else {
                    return null;
                }
            }
        }
    }

}
