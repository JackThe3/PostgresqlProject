package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import Statistika.FilmMesiaca;
import rdg.*;

public class MovieMenu extends Menu {
    /**ui.Menu je prebrane zo vzoroveho projektu.
     **/
    public void print() {
        System.out.println("*********************************");
        System.out.println("* 1. getAllMovies               *");
        System.out.println("* 2. find Movie                 *");
        System.out.println("*********************************");
        System.out.println("* 3. create new Movie           *");
        System.out.println("* 4. update Movie               *");
        System.out.println("* 5. delete Movie               *");
        System.out.println("*********************************");
        System.out.println("* 6. get Movie genres           *");
        System.out.println("* 7. set Movie genres           *");
        System.out.println("* 8. delete Movie genres        *");
        System.out.println("*********************************");
        System.out.println("* exit                          *");
        System.out.println("*********************************");
    }

    @Override
    public void handle(String option){
        switch (option) {
            case "1" -> getAll();
            case "2" -> findMovie();
            case "3" -> newMovie();
            case "4" -> updateMovie();
            case "5" -> deleteMovie();
            case "6" -> getMovieGenre();
            case "7" -> setMovieGenre();
            case "8" -> deleteMovieGenre();
            case "exit" -> exit();
            default -> System.out.println("Unknown option");
        }
    }

    /**
     * Vstup z klavesnice pre Film menu.
     * @return Movie, ak nie je v databaze tak null.
     */
    public Movie input(){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter a movie name:");
            String name = br.readLine();
            System.out.println("Enter a year:");
            Integer year = Integer.parseInt(br.readLine());
            return MovieFinder.getInstance().find(name, year);
        }
        catch (SQLException e){
            System.out.println("Nastal problem s databazou: " + e.getMessage());
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void deleteMovieGenre(){
        System.out.println("delete genres from movie: ");
        Movie movie = input();
        if (movie == null) {
            System.out.println("Film neexistuje");
        }
        else{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter a genre:");
            try {
                String genre = br.readLine();
                try {
                    boolean b = movie.deleteGenre(genre);
                    if (b) System.out.println("zanre boli zmazane pre film: " + movie);
                }
                catch (SQLException e){
                    System.out.println("Nastal problem s databazou: " + e.getMessage());
                }
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }

        }
    }

    private void setMovieGenre(){
        System.out.println("Set movie genres: ");
        Movie movie = input();
        if (movie == null) {
            System.out.println("Film neexistuje");
        }
        else{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter a genre:");
            try {
                String genre = br.readLine();
                try {
                    boolean s = movie.setGenres(genre);
                    if (s) System.out.println("zanre boli nastavene pre film: " + movie + ":"+ movie.getGenres());
                }
                catch (SQLException e){
                    System.out.println("Nastal problem s databazou: " + e.getMessage());
                }
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }

        }
    }

    private void getMovieGenre(){
        System.out.println("Get movie genres: ");
        Movie movie = input();
        if (movie == null){
            System.out.println("No such movie exists");
            return;
        }
        try {
            System.out.println(movie + " Genres:" + movie.getGenres());
        }
        catch (SQLException e){
            System.out.println("Nastal problem s databazou: " + e.getMessage());
        }
    }

    private void deleteMovie(){
        System.out.println("Delete movie: ");
        Movie movie = input();
        if (movie == null){
            System.out.println("No such movie exists");
        }
        else {
            try {
                movie.delete();
                System.out.println("The movie has been successfully deleted");
            }
            catch (SQLException e){
                System.out.println("Nastal problem s databazou: " + e.getMessage());
            }
        }
    }

    private void updateMovie(){
        System.out.println("Update movie: ");
        Movie movie = input();
        if (movie == null){
            System.out.println("No such movie exists");
        }
        else {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                System.out.println("Enter new name:");
                String name = br.readLine();
                movie.setName(name);
                System.out.println("Enter new year:");
                int year = Integer.parseInt(br.readLine());
                movie.setYear(year);
                System.out.println("Enter new duration:");
                Integer duration = Integer.parseInt(br.readLine());
                movie.setDuration(duration);
                try {
                    movie.update();
                    System.out.println("The movie has been successfully updated");
                }
                catch (SQLException e){
                    System.out.println("Nastal problem s databazou: " + e.getMessage());
                }
            }
            catch (IOException e){
                System.out.println("zly vstup");
            }
        }
    }

    private void newMovie(){
        System.out.println("Create movie: ");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            Movie movie = new Movie();
            System.out.println("Enter name:");
            movie.setName(br.readLine());
            System.out.println("Enter a year:");
            Integer year = Integer.parseInt(br.readLine());
            movie.setYear(year);
            System.out.println("Enter a duration:");
            Integer duration = Integer.parseInt(br.readLine());
            movie.setDuration(duration);
            try {
                movie.insert();
                System.out.println("The movie has been sucessfully added");
                System.out.print("The movie's id is: ");
                System.out.println(movie.getId());
            }
            catch (SQLException e){
                System.out.println("Nastal problem s databazou: " + e.getMessage());
            }

        }catch (IOException e){
            System.out.println("zly vstup");
        }

    }

    private void findMovie(){
        System.out.println("Find movie: ");
        Movie movie = input();
        if (movie == null){
            System.out.println("No such movie exists");
        }
        else{
            System.out.println(movie);
        }


    }

    private void getAll(){
        System.out.println("Get all movies: ");
        try {
            for (Movie movie : MovieFinder.getInstance().getAll()) {
                System.out.println(movie);
            }
        }
        catch (SQLException e){
            System.out.println("Nastal roblem s databazu: " + e.getMessage());
        }

    }
}
