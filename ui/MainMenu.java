package ui;

import ts.ReservationDomain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class MainMenu extends Menu {
    /**Menu je prebrane zo vzoroveho projektu.
     **/
    public void print() {
        System.out.println("*********************************");
        System.out.println("* 1. Genres                     *");
        System.out.println("* 2. Movie                      *");
        System.out.println("* 3. Theater                    *");
        System.out.println("* 4. Screening                  *");
        System.out.println("* 5. Kupon                      *");
        System.out.println("* 6. Kompenzacia                *");
        System.out.println("* 7. Statistika                 *");
        System.out.println("* exit                          *");
        System.out.println("*********************************");
    }

    @Override
    public void handle(String option) throws Exception {
        try {
            switch (option) {
                case "1" -> genres();
                case "2" -> movie();
                case "3" -> theater();
                case "4" -> screening();
                case "5" -> kupon();
                case "6" -> kompenz();
                case "7" -> stat();
                case "exit" -> exit();
                default -> System.out.println("Unknown option");
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void stat() throws Exception {
        StatMenu menu = new StatMenu();
        menu.run();
    }

    private void kompenz() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter screening id:");
        int id = Integer.parseInt(br.readLine());
        ReservationDomain.kompenzacia(id);
    }

    private void kupon() throws Exception {
        KuponMenu menu = new KuponMenu();
        menu.run();
    }

    private void screening() throws Exception {
        ScreeningMenu menu = new ScreeningMenu();
        menu.run();
    }

    private void theater() throws Exception{
        TheaterMenu menu = new TheaterMenu();
        menu.run();
    }

    private void movie() throws Exception {
        MovieMenu menu = new MovieMenu();
        menu.run();
    }

    private void genres() throws Exception {
        GenreMenu menu = new GenreMenu();
        menu.run();
    }
}
