package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import Statistika.FilmMesiaca;
import Statistika.MesacneTrzby;

public class StatMenu extends Menu {
    /**Menu je prebrane zo vzoroveho projektu.
     **/
    public void print() {
        System.out.println("*********************************");
        System.out.println("* 1. Filmy mesiaca              *");
        System.out.println("* 2. Trzba                      *");
        System.out.println("*********************************");
        System.out.println("* exit                          *");
        System.out.println("*********************************");
    }

    @Override
    public void handle(String option){
        switch (option) {
            case "1" -> filmMesiaca();
            case "2" -> trzba();
            case "exit" -> exit();
            default -> System.out.println("Unknown option");
        }
    }

    private void trzba(){
        System.out.println("Trzba: ");
        try {
            new MesacneTrzby();
        }
        catch (Exception e) {
            System.out.println("nastala chyba: " + e.getMessage());
        }
    }

    private void filmMesiaca(){
        System.out.println("Filmy mesiaca: ");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter a pocet filmov:");
            int m = Integer.parseInt(br.readLine());
            new FilmMesiaca(m).get();

        } catch (Exception e) {
            System.out.println("nastala chyba: " + e.getMessage());
        }

    }
}
