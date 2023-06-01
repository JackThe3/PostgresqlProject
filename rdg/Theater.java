package rdg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import connection.DbContext;

public class Theater {
    public int id;
    public int numberOfSeats;
    public String name;

    public Theater() {}

    @Override
    public String toString() {
        return "rdg.Theater{" +
                "id=" + id +
                ", numberOfSeats=" + numberOfSeats +
                ", name='" + name + '\'' +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void insert() throws SQLException {
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("INSERT INTO theater(numberofseats, name) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1, numberOfSeats);
            statement.setString(2, name);
            statement.executeUpdate();
            try(ResultSet resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                id = resultSet.getInt(1);
            }
        }
    }

    public void setNumberOfSeats(Integer numberOfSeats){
        this.numberOfSeats = numberOfSeats;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
