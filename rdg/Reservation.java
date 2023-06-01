package rdg;

import java.math.BigDecimal;
import java.sql.*;


public class Reservation {


    Integer id;
    Timestamp expire;
    BigDecimal price;
    Integer customerId;
    Integer screeningId;
    boolean status;

    public Reservation() {}

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public Reservation (Integer screeningId, Integer userId){
        this.customerId = userId;
        this.screeningId = screeningId;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", expire=" + expire +
                ", price=" + price +
                ", customerId=" + customerId +
                ", screeningId=" + screeningId +
                ", status=" + status +
                '}';
    }

    /**
     *Prida objednavku do databazy. Uzivatel ma nasledne 10 minut na to aby ju potvrdil.
     */
    public void insert(Connection conn) throws SQLException {
        try(PreparedStatement statement = conn.prepareStatement("INSERT INTO reservations(expire, price, customer_id, screening_id) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)){
            long desat = VALUES.NOW().getTime() + VALUES.DESAT;
            statement.setTimestamp(1,new Timestamp(desat));
            statement.setBigDecimal(2, new BigDecimal(0));
            statement.setInt(3, customerId);
            statement.setInt(4, screeningId);
            statement.executeUpdate();
            try(ResultSet resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                id = resultSet.getInt(1);
                expire = new Timestamp(desat);
            }
        }
    }

    public Timestamp getExpire() {
        return expire;
    }

    public void setExpire(Timestamp expire) {
        this.expire = expire;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(Integer screeningId) {
        this.screeningId = screeningId;
    }

}
