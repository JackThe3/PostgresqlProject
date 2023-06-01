package rdg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import connection.DbContext;
import ts.*;

public class Seat {
    Integer id;
    Integer seatNumber;
    Integer reservationId;
    Integer screeningId;

    public Seat(Integer seatNumber, Integer reservationId) throws SQLException {
        Reservation find = ReservationFinder.getInstance().findById(reservationId);
        if (find == null) throw new IllegalArgumentException("reservation vyprsala");
        this.seatNumber = seatNumber;
        this.reservationId = reservationId;
        this.screeningId = ScreeningFinder.getInstance().findById(find.screeningId).getId();
    }

    /**
     *Prida sedadlo do databaze.
     */
    public void insert() throws SQLException {
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("INSERT INTO seat(number, reservation_id, screening_id) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1, seatNumber);
            statement.setInt(2, reservationId);
            statement.setInt(3, screeningId);
            statement.executeUpdate();
            try(ResultSet resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                id = resultSet.getInt(1);

            }
        }
    }
}
