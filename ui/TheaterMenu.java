package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import rdg.*;

public class TheaterMenu extends Menu {

    /** Menu je prebrane zo vzoroveho projektu.
     **/

    public void print() {
        System.out.println("*********************************");
        System.out.println("* 1. get All theaters           *");
        System.out.println("* 2. find theater               *");
        System.out.println("* exit                          *");
        System.out.println("*********************************");
    }

    @Override
    public void handle(String option){
            switch (option) {
                case "1" -> getAll();
                case "2" -> findTheater();
                case "exit" -> exit();
                default -> System.out.println("Unknown option");
            }
    }

    private void findTheater(){
        System.out.println("Find theater: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter a theater name:");
        try {
            String name = br.readLine();
            Theater theater = TheaterFinder.getInstance().find(name);
            if (theater == null){
                System.out.println("No such theater exists");
            }
            else{
                System.out.println(theater);
            }
        }
        catch (SQLException e){
            System.out.println("Nastal problem s databazou: " + e.getMessage());
        }
        catch (IOException e ){
            System.out.println("zly vstup");
        }
    }

    private void getAll(){
        System.out.println("Get all theaters: ");
        try {
            for (Theater theater : TheaterFinder.getInstance().getAll()) {
                System.out.println(theater);
            }
        }
        catch (SQLException e){
            System.out.println("Nastal problem s databazou: " + e.getMessage());
        }
    }
}