import java.sql.Connection;
import java.sql.SQLException;

import connection.DbContext;
import org.postgresql.ds.PGSimpleDataSource;
import ui.MainMenu;

public class Main {
    /**Main je prebrane zo vzoroveho projektu.
     **/

    public static void main(String[] args) throws SQLException {
        PGSimpleDataSource ds = new PGSimpleDataSource();

        ds.setServerName("localhost");
        ds.setPortNumber(5432);
        ds.setDatabaseName("postgres");
        ds.setUser("postgres");
        ds.setPassword("Data11");
        try(Connection c = ds.getConnection()){
            DbContext.setConnection(c);
            //new MesacneTrzby();
            MainMenu mainMenu = new MainMenu();
            mainMenu.run();
        } catch (Exception exception) {
            System.out.println("stalo sa nieco velmi zle");
            exception.printStackTrace();
        }

    }
    }
