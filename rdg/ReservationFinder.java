package rdg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.DbContext;

public class ReservationFinder {
    private static final ReservationFinder INSTANCE = new ReservationFinder();
    public static ReservationFinder getInstance() {
        return INSTANCE;
    }
    ReservationFinder(){}

    public Reservation findById(int id) throws SQLException{
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT * FROM reservations WHERE id = ? AND expire > current_timestamp AND status = false")){
            statement.setInt(1,id);
            try (ResultSet result = statement.executeQuery()){
                if (result.next()){
                    Reservation reservation = new Reservation();
                    reservation.setId(result.getInt("id"));
                    reservation.setPrice(result.getBigDecimal("price"));
                    reservation.setExpire(result.getTimestamp("expire"));
                    reservation.setCustomerId(result.getInt("customer_id"));
                    reservation.setScreeningId(result.getInt("screening_id"));
                    if (result.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return reservation;
                }
                else {
                    return null;
                }
            }
        }
    }

    /**
     * Najde uzivatelove rezervacie
     * @param customerId
     * @return List Rezervacii
     */
    public List<Reservation> customerReservations(int customerId) throws SQLException {
        try (PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT * FROM reservations WHERE customer_id = ?")) {
            statement.setInt(1, customerId);
            List<Reservation> all = new ArrayList<>();
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Reservation reservation = new Reservation();
                    reservation.setId(result.getInt("id"));
                    reservation.setPrice(result.getBigDecimal("price"));
                    reservation.setExpire(result.getTimestamp("expire"));
                    reservation.setCustomerId(result.getInt("customer_id"));
                    reservation.setScreeningId(result.getInt("screening_id"));
                    reservation.setStatus(result.getBoolean("status"));
                    all.add(reservation);
                }
                return all;
            }
        }
    }
}
