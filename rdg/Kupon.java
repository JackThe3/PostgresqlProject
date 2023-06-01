package rdg;

import connection.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Kupon {
    int id;
    int customerId;
    String code;
    int reservationId;

    public Kupon(int customerId, String code, int reservationId) {
        this.customerId = customerId;
        this.code = code;
        this.reservationId = reservationId;
    }

    @Override
    public String toString() {
        return "Kupon{" +
                "customerId=" + customerId +
                ", code='" + code + '\'' +
                ", reservationId=" + reservationId +
                '}';
    }

    public Kupon(){}

    public void insert() throws SQLException {
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("INSERT INTO kupon(customer_id, code, reservation_Id) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1, customerId);
            statement.setString(2, code);
            if (reservationId == -1){
                statement.setNull(3, java.sql.Types.INTEGER);
            }
            else{
                statement.setInt(3, reservationId);
            }

            statement.executeUpdate();
            try(ResultSet resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                id = resultSet.getInt(1);
            }
        }
    }

    public void delete() throws SQLException {
        try (PreparedStatement statement = DbContext.getConnection().prepareStatement("DELETE FROM kupon WHERE code = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
}
