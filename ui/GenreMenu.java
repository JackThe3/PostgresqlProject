package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import rdg.*;

public class GenreMenu extends Menu {
    /**Menu je prebrane zo vzoroveho projektu.
     **/
    public void print() {
        System.out.println("*********************************");
        System.out.println("* 1. get all Genres             *");
        System.out.println("* 2. find Genre                 *");
        System.out.println("*********************************");
        System.out.println("* 3. create new Genre           *");
        System.out.println("* 4. update Genre               *");
        System.out.println("* 5. delete Genre               *");
        System.out.println("* exit                          *");
        System.out.println("*********************************");
    }

    @Override
    public void handle(String option){
        switch (option) {
            case "1" -> getAll();
            case "2" -> findGenre();
            case "3" -> newGenre();
            case "4" -> updateGenre();
            case "5" -> deleteGenre();
            case "exit" -> exit();
            default -> System.out.println("Unknown option");
        }
    }

    /**
     * Nahra meno zanru z klavesnice.
     * @return input v tomto pripade String name
     */
    public String input(){
        String name = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter name:");
        try {
            name = br.readLine();
            if (name.equals("")){
                System.out.println("zly vstup");
                name = input();
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        return name;
    }

    private void deleteGenre(){
        System.out.println("Delete existing Genre: ");
        String name = input();
        try {
            Genre genre = GenreFinder.getInstance().findByName(name);
            if (genre == null){
                System.out.println("No such genre exists");
            }
            else {
                genre.delete();
                System.out.println("The genre has been successfully deleted");
            }
        }
        catch (SQLException e){
            System.out.println("Nastal problem s databazou: " + e.getMessage());
        }
    }

    private void updateGenre(){
        System.out.println("Update existing genre: ");
        String name = input();
        try {
            Genre genre = GenreFinder.getInstance().findByName(name);
            if (genre == null){
                System.out.println("No such genre exists");
            }
            else {
                try {
                    System.out.println("Enter new name:");
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    genre.setName(br.readLine());
                    genre.update();
                    System.out.println("The genre has been successfully updated");
                } catch (IOException e) {
                    System.out.println("Zly vstup");
                }
            }
        }
        catch (SQLException e){
            System.out.println("Nastal problem s databazou: " + e.getMessage());
        }


    }

    private void newGenre(){
        System.out.println("Create new Genre: ");
        String name = input();
        try {
            Genre check = GenreFinder.getInstance().findByName(name);
            if (check != null) {
                System.out.println("genre uz existuje");
                return;
            }
            Genre genre = new Genre();
            genre.setName(name);
            genre.insert();
            System.out.println("The genre has been sucessfully added");
            System.out.print("The genre's id is: ");
            System.out.println(genre.getId());
        }
        catch (SQLException e){
            System.out.println("Nastal problem s databazou: " + e.getMessage());
        }
    }

    private void findGenre(){
        System.out.println("Find Genre: ");
        String name = input();
        try {
            Genre genre = GenreFinder.getInstance().findByName(name);
            if (genre == null){
                System.out.println("No such genre exists");
            }
            else{
                System.out.println(genre);
            }
        }
        catch (SQLException e){
            System.out.println("Nastal problem s databazou: " + e.getMessage());
        }
    }

    private void getAll(){
        System.out.println("All Genres: ");
        try {
            for (Genre genre : new GenreFinder().getAll()) {
                System.out.println(genre);
            }
        }
        catch (SQLException e){
            System.out.println("Nastal problem s databazou: " + e.getMessage());
        }
    }
}
