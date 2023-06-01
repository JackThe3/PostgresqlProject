package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public abstract class Menu {
    /**ui.Menu je prebrane zo vzoroveho projektu.
     **/
    private boolean exit;

    public void run() throws Exception {
        exit = false;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (!exit) {
            System.out.println();
            print();
            System.out.println();

            String line = br.readLine();
            if (line == null) {
                return;
            }

            System.out.println();

            handle(line);
        }
    }

    public void exit() {
        exit = true;
    }

    public abstract void print();

    public abstract void handle(String option) throws Exception;
}