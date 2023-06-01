package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import rdg.*;
import ts.ReservationDomain;

public class KuponMenu extends Menu {
    /**Menu je prebrane zo vzoroveho projektu.
     **/
    public void print() {
        System.out.println("*********************************");
        System.out.println("* 1. Find kupon                 *");
        System.out.println("* 2. Use Kupon                 *");
        System.out.println("*********************************");
        System.out.println("* 3. create new Kupon           *");
        System.out.println("* 4. delete Kupon               *");
        System.out.println("* exit                          *");
        System.out.println("*********************************");
    }

    @Override
    public void handle(String option){
        switch (option) {
            case "1" -> findKupon();
            case "2" -> useKupon();
            case "3" -> newKupon();
            case "4" -> deleteKupon();
            case "exit" -> exit();
            default -> System.out.println("Unknown option");
        }
    }

    private void useKupon(){
        System.out.println("Use kupon: ");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter kupon code: ");
            String code = br.readLine();
            System.out.println("Enter user id: ");
            int userId = Integer.parseInt(br.readLine());
            System.out.println("Enter reservation id: ");
            int reservationId = Integer.parseInt(br.readLine());
            try {
                ReservationDomain.useKupon(code, userId, reservationId);
            }
            catch (SQLException e){
                System.out.println("Nepodarilo sa uplatnit kupon: " + e.getMessage());
            }
        }catch (IOException e){
            System.out.println("zly vstup " + e.getMessage());
        }


    }

    private void findKupon(){
        System.out.println("Find kupon: ");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter kupon code: ");
            String code = br.readLine();
            try {
                Kupon k = KuponFinder.getInstance().findByCode(code);
                System.out.println(k);
            }catch (SQLException e){
                System.out.println("Nepodarilo sa najst kupon: " + e.getMessage());
            }

        }catch (IOException e){
            System.out.println("zly vstup " + e.getMessage());
        }
    }

    private void deleteKupon() {
        System.out.println("Delete kupon: ");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter kupon code: ");
            String code = br.readLine();
            try {
                Kupon k = KuponFinder.getInstance().findByCode(code);
                if (k == null) {
                    System.out.println("Nepodarilo sa najst kupon");
                }
                else {
                    k.delete();
                    System.out.println("Kupon bol odstraneny");
                }
            }catch (SQLException e){
                System.out.println("Nepodarilo sa zmazat kupon: " + e.getMessage());
            }


        }catch (IOException e){
            System.out.println("zly vstup " + e.getMessage());
        }
    }

    private void newKupon(){
        System.out.println("New kupon: ");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter kupon code: ");
            String code = br.readLine();
            System.out.println("Enter user id: ");
            int userId = Integer.parseInt(br.readLine());
            Kupon kupon = new Kupon(userId, code, -1);
            try {
                kupon.insert();
                System.out.println("Kupon bol vytvoreny");
            }catch (SQLException e){
                System.out.println("Nepodarilo sa vytvorit kupon: " + e.getMessage());
            }
        }catch (IOException e){
            System.out.println("zly vstup " + e.getMessage());
        }
    }
}
