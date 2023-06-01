package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import rdg.*;
import connection.DbContext;
import ts.*;

public class ScreeningMenu extends Menu {
    /**Menu je prebrane zo vzoroveho projektu.
     **/
    @Override
    public void print() {
        System.out.println("*********************************");
        System.out.println("* 1. upcoming screenings        *");
        System.out.println("* 2. all upcoming screenings    *");
        System.out.println("* 3. create screening           *");
        System.out.println("*********************************");
        System.out.println("* 4. free   seats               *");
        System.out.println("* 5. choose seats               *");
        System.out.println("*********************************");
        System.out.println("* 6. create reservation         *");
        System.out.println("* 7. confirm reservation        *");
        System.out.println("* 8. customer reservations      *");
        System.out.println("* exit                          *");
        System.out.println("*********************************");
    }

    @Override
    public void handle(String option){
        try {
            switch (option) {
                case "1" -> getUpcoming();
                case "2" -> getAllUpcoming();
                case "3" -> createScreening();
                case "4" -> freeSeats();
                case "5" -> chooseSeats();
                case "6" -> createReservation();
                case "7" -> confirmReservation();
                case "8" -> customerReservations();
                case "exit" -> exit();
                default -> System.out.println("Unknown option");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Vsetky nadchadzajuce.
     */
    private void getAllUpcoming() {
        System.out.println("get All Upcoming: ");
        try {
            List<Screening> screenings = new ScreeningFinder().getAllUpcoming();
            if (screenings.isEmpty()) {
                System.out.println("No upcoming");
            }
            else{
                for (Screening screening : screenings) {
                    System.out.println(screening);
                }
            }
        }catch (SQLException e){
            System.out.println("Nastal problem s databazou: " + e.getMessage());
        }
    }

    /**
     *Vypise vsetky reservacie uzivatela.
     */
    private void customerReservations(){
        System.out.println("Create reservations: ");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter customer id:");
            int id = Integer.parseInt(br.readLine());
            List<Reservation> k = ReservationFinder.getInstance().customerReservations(id);
            for (Reservation reservation:k) {
                if (reservation.getStatus()){
                    //Ak bola rezervacia ukoncena tak
                    System.out.println(reservation + " dokoncena");
                }
                else{
                    System.out.println(reservation + " NEdokoncena");
                }

            }
        }catch (SQLException e){
            System.out.println("Nastal problem s databazou: " + e.getMessage());
        } catch (IOException e){
            System.out.println("zly vstup: " + e.getMessage());
        }

    }

    /**
     *Potvrdi rezervaciu.
     */
    private void confirmReservation(){
        System.out.println("Confirm reservation: ");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter reservation id:");
            int id = Integer.parseInt(br.readLine());
            //Reservation find = ReservationFinder.getInstance().findById(id);
            try {
                ReservationDomain.confirmReservation(id);
            }
            catch (Exception e){
                System.out.println("nepodarilo sa potvrdit objednavku: " + e.getMessage());
            }
        }catch (IOException e){
            System.out.println("zly vstup: " + e.getMessage());
        }

    }

    private void freeSeats(){
        System.out.println("Free seats: ");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter screening id:");
            int id = Integer.parseInt(br.readLine());
            ReservationDomain.cleanExpired();
            List<Integer> zoz = new ArrayList<>();
            try(PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT * FROM seat WHERE screening_id = ?")){
                statement.setInt(1,id);
                try (ResultSet result = statement.executeQuery()){
                    while (result.next()){
                        zoz.add(result.getInt("number"));
                    }
                }
            }

            Screening find = ScreeningFinder.getInstance().findById(id);
            if (find == null){
                System.out.println("Divadlo neexistuje");
                return;
            }

            Theater theater = find.getTheater();
            for (int i = 1; i <= theater.numberOfSeats; i++) {
                if (!zoz.contains(i)){
                    System.out.print(i + " ");
                }
                else{
                    System.out.print("X" + " ");
                }

            }
            System.out.println();
        }catch (SQLException e){
            System.out.println("Nastal problem s databazou: " + e.getMessage());
        }
        catch (IOException e){
            System.out.println("zly vstup: " + e.getMessage());
        }


    }

    private void chooseSeats(){
        System.out.println("Choose seat: ");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter reservation id:");
            int id = Integer.parseInt(br.readLine());
            System.out.println("Enter seat number:");
            int seatnumber = Integer.parseInt(br.readLine());
            Seat seat = new Seat(seatnumber, id);
            ReservationDomain.cleanExpired();
            try {
                seat.insert();
                System.out.println("Sedadlo rezervovane");
            }catch (SQLException e){
                System.out.println("nepodarilo sa rezervovat sedadlo uz je zabrane");
            }
        }catch (SQLException e){
            System.out.println("Nastal problem s databazou: " + e.getMessage());
        }
        catch (IOException e){
            System.out.println("zly vstup: " + e.getMessage());
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }


    }

    private void createReservation(){
            System.out.println("Create reservation: ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter screening id:");
            try {
                int id = Integer.parseInt(br.readLine());
                System.out.println("Enter user id:");
                int userid = Integer.parseInt(br.readLine());
                try {
                    ReservationDomain.createReservation(id, userid);
                }
                catch (SQLException e){
                    System.out.println("nepodarilo sa potvrdit objednavku: " + e.getMessage());
                }
            }
            catch (IOException e){
                System.out.println("zly vstup");
            }

    }

    /**
     *Skontorluje ci momentalne neprebieha film v sale.
     */
    public boolean openingCheck(Timestamp opening, String theaterName, Movie movie) throws SQLException{
        String sql = "SELECT opening,movies.name,theater.name,duration FROM screening JOIN movies on movie_id = movies.id JOIN theater ON theater.id = theater_id WHERE theater.name = ?  AND (? BETWEEN  opening AND opening + (duration * interval '1 minute') OR ? BETWEEN  opening AND opening + (duration * interval '1 minute'))";
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1,theaterName);
            statement.setTimestamp(2, opening);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(opening);
            calendar.add(Calendar.MINUTE, movie.getDuration());
            Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());

            statement.setTimestamp(3, timestamp);
            try (ResultSet result = statement.executeQuery()){
                if (result.next()){
                    return false;
                }
                return true;
            }
        }
    }

    private void createScreening(){
        System.out.println("Create screening: ");
        //BigDecimal price, Timestamp opening, String MovieName, int MovieYear, String theaterName
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter price:");
            BigDecimal price = new BigDecimal(br.readLine());
            System.out.println("Enter movie name:");
            String name = br.readLine();
            System.out.println("Enter movie year:");
            int year = Integer.parseInt(br.readLine());
            System.out.println("Enter theater name:");
            String theater = br.readLine();
            System.out.println("Enter timestamp:");
            String time = br.readLine();
            Timestamp timestamp = Timestamp.valueOf(time);
            try {
                if (openingCheck(timestamp, theater, MovieFinder.getInstance().find(name, year))){
                    Screening screening = new Screening(price, timestamp, name, year, theater);
                    screening.insert();
                    System.out.println("screening created");
                }
                else{
                    System.out.println("Film momentalne prebieha. nie je mozne vytvorit screening");
                }
            }catch (SQLException e){
                System.out.println("Nastal problem s databazou: " + e.getMessage());
            }

        }catch (Exception e){
            System.out.println("zly vstup " + e.getMessage());
        }
    }


    /**
     * Filmy ktore zacnu do 40 min.
     */
    private void getUpcoming(){
        System.out.println("Get upcoming: ");
        try {
            List<Screening> screenings = new ScreeningFinder().getUpcoming();
            if (screenings.isEmpty()) {
                System.out.println("No upcoming");
            }
            else{
                for (Screening screening : screenings) {
                    System.out.println(screening);
                }
            }
        }catch (SQLException e){
            System.out.println("Nastal problem s databazou: " + e.getMessage());
        }
    }
}
