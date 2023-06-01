package ts;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import connection.DbContext;
import rdg.*;

public class ReservationDomain {

    /**
     * Prida rezervaciu do databazy.
     */
    public static void createReservation(int screeningId, int userId) throws SQLException {
        Connection conn = DbContext.getConnection();
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED); //iba vkladam
        try {
            if (!screenCheck(screeningId)){
                System.out.println("nepodarilo sa rezervovat");
            }
            else{
                Reservation reservation = new Reservation(screeningId, userId);
                reservation.insert(conn);
                conn.commit();
                System.out.println("Reservation kompletna, vyber sedadla. Id rezervacie je " + reservation.getId());
                System.out.println("Platnost vyprsi o: " + reservation.getExpire());
            }
        }
        catch (SQLException e){
            System.out.println("Nastal problem s databazou: " + e.getMessage());
            conn.rollback();
        }
        finally {
            conn.setAutoCommit(true);
        }
    }


    /**
     * Skontrojule ci je 40 min pred premietanim.
     * @param id premietania.
     * @return true ak je mozne vytvorit objednavku, inak false
     */
    public static boolean screenCheck(int id) throws SQLException {
        Screening screening = ScreeningFinder.getInstance().findById(id);
        if (screening == null){
            return false;
        }
        Timestamp time = screening.getOpening();
        Timestamp now = VALUES.NOW();
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.add(Calendar.MINUTE, -40);
        Timestamp time40 = new Timestamp(c.getTimeInMillis());
        return now.before(time) && now.after(time40);
    }

    /**
     * Potvrdi rezervaciu.
     * @param id rezervacie
     */
    public static void confirmReservation(int id) throws Exception{
        Connection conn = DbContext.getConnection();
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ); //nesmie precitat riadky ktore uz citala

        Reservation r = ReservationFinder.getInstance().findById(id);

        //Ak rezervacie vyprsala(neexistuje)
        if (r == null){
            conn.rollback();
            conn.setAutoCommit(true);
            throw new Exception("Reservation vyprsala");
        }

        //Spocita pocet sedadiel priradenich ku rezervacii
        int pocet = 0;
        try (PreparedStatement statement = conn.prepareStatement("SELECT count(*) from seat WHERE reservation_id = ?")) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()){
                if (result.next()){
                    pocet = result.getInt("count");
                }
            }
        }

        //je potrebne mat vybrate sedadla.
        if (pocet == 0){
            conn.rollback();
            conn.setAutoCommit(true);
            throw new Exception("vyber sedadla!");
        }

        BigDecimal screeningPrice = ScreeningFinder.getInstance().findById(r.getScreeningId()).getPrice();

        //cena je podla poctu sedadiel
        BigDecimal price = new BigDecimal(pocet).multiply(screeningPrice);

        //nahra potvrdenie do databazy.
        try(PreparedStatement statement = conn.prepareStatement("UPDATE reservations SET status=true, expire=current_timestamp, price=? WHERE id = ?")) {
            statement.setBigDecimal(1, price);
            statement.setInt(2, id);
            statement.executeUpdate();
            System.out.println("Reservation: " + id + " -> Potvrdena!");
        }
        conn.commit();
        conn.setAutoCommit(true);
    }


    /**
     * Zmaze nepotvrdene miesta a nepotrvdene rezervacie.
     */
    public static void cleanExpired() throws SQLException {
        Connection conn = DbContext.getConnection();
//        try(PreparedStatement statement = conn.prepareStatement("DELETE FROM seat WHERE id IN (SELECT seat.id FROM reservations JOIN seat ON reservation_id = reservations.id WHERE expire < current_timestamp AND reservations.status = false)")){
//            statement.executeUpdate();
//        }
        try(PreparedStatement statement = conn.prepareStatement("DELETE FROM reservations WHERE current_timestamp > expire AND status = false;")){
            statement.executeUpdate();
        }
    }

    /**
     * Pouzije kupon.
     * @param code kod kuponu
     * @param userId id pouzivatela
     * @param reservationId rezervacia na ktoru chceme kupon pouzit.
     */
    public static void useKupon(String code, int userId, int reservationId) throws SQLException{
        Connection conn = DbContext.getConnection();
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        Kupon kupon = KuponFinder.getInstance().findByCode(code);
        Reservation reservation = ReservationFinder.getInstance().findById(reservationId);

        if (reservation == null){
            System.out.println("uz nie je mozne pouzit kupon.");
            conn.rollback();
            conn.setAutoCommit(true);
            return;
        }
        if (userId != kupon.getCustomerId()){
            System.out.println("Nie je tvoj kupon");
            conn.rollback();
            conn.setAutoCommit(true);
            return;
        }
        if (kupon.getReservationId() != -1){
            System.out.println("Uz pouzity kupon.");
            conn.rollback();
            conn.setAutoCommit(true);
            return;
        }
        try(PreparedStatement statement = conn.prepareStatement("UPDATE kupon SET reservation_Id=? WHERE id = ?")) {
            statement.setInt(1, reservationId);
            statement.setInt(2, kupon.getId());
            statement.executeUpdate();
        }
        conn.commit();
        conn.setAutoCommit(true);
        System.out.println("kupon bol pouzity");
    }

//TODO RESERVATON FIDNER PRE USRA KUPON
    public static void kompenzacia(int id) throws SQLException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter userid:");
        int userId = Integer.parseInt(br.readLine());

        List<String> all= new ArrayList<>();
        List<Integer> odporucane = new ArrayList<>();

        //SELECT genres.id ,genre FROM screening JOIN movies ON movie_id = movies.id JOIN movie_genres ON movies.id = movie_genres.movie_id JOIN genres ON genres.id = movie_genres.genres_id WHERE screening.id = 27
        Connection conn = DbContext.getConnection();
        //Najde zanre filmu ktore chceme odporucit.
            try (PreparedStatement statement = conn.prepareStatement("SELECT genres.id ,genre FROM screening JOIN movies ON movie_id = movies.id JOIN movie_genres ON movies.id = movie_genres.movie_id JOIN genres ON genres.id = movie_genres.genres_id WHERE screening.id = ?")) {
                statement.setInt(1, id);
                try (ResultSet result = statement.executeQuery()){

                    while (result.next()){
                        all.add(result.getString("genre"));
                    }
                }
            }
             //SELECT * FROM screening JOIN movies ON movie_id = movies.id JOIN movie_genres ON movies.id = movie_genres.movie_id JOIN genres ON genres.id = movie_genres.genres_id WHERE screening.id != 27 AND opening > current_timestamp
            try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM screening JOIN movies ON movie_id = movies.id JOIN movie_genres ON movies.id = movie_genres.movie_id JOIN genres ON genres.id = movie_genres.genres_id WHERE screening.id != ? AND opening > current_timestamp")) {
                statement.setInt(1, id);
                try (ResultSet result = statement.executeQuery()){
                    while (result.next()){
                        String zaner = result.getString("genre");
                        //skontoruje ci film ma tento zaner ak ako nahra ho ku odporucanim.
                        if (all.contains(zaner)){
                            odporucane.add(result.getInt("id"));
                        }
                    }
                }
            }
        List<Screening> upcoming = ScreeningFinder.getInstance().getUpcoming();
            List<Screening> recommmended = new ArrayList<>();
        for (Screening s: upcoming) {
            if (odporucane.contains(s.getId())){
                recommmended.add(s);
            }
        }

        if (recommmended.isEmpty()){
            //ak nieje mozne prejst na objednavku tak vytvory kupon
            String code = "kupon" + userId + "S" + id;
            Kupon kupon = new Kupon(userId, code, -1);
            kupon.insert();
            System.out.println("Dostal si kupon: " + code);
        }
        else{
            //vytvor nahradnu objednavku.
            System.out.println("vyber si screening");
            for (Screening s:recommmended) {
                System.out.println(s);
            }

            System.out.println("Enter screening id:");
            int screeningId = Integer.parseInt(br.readLine());

            ReservationDomain.createReservation(screeningId, userId);
        }
        }
}
