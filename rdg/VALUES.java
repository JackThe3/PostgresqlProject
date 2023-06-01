package rdg;

import connection.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class VALUES {

    public static int DESAT = 600000;

    /*
    Hodnota pouzita v projekte.
     */
    public static Timestamp NOW() throws SQLException{
        //
        //"SELECT CURRENT_TIMESTAMP"
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT date_trunc('milliseconds', current_timestamp)")){
            try (ResultSet result = statement.executeQuery()){
                result.next();
                return result.getTimestamp(1);
            }
        }
    }


}
