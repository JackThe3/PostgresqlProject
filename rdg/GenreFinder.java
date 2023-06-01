package rdg;
import connection.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenreFinder{
    /**
     * finder Prebrany zo vzoroveho projektu
     */
    private static final GenreFinder INSTANCE = new GenreFinder();
    public static GenreFinder getInstance() {
        return INSTANCE;
    }
    public GenreFinder() {}

    /**
     * najde zaner v databaze.
     * @param name String meno zanru
     * @return zener z databaze. alebo null ak zaner neexistuje
     */
    public Genre findByName(String name) throws SQLException{
        name = name.toUpperCase();
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT * FROM genres WHERE genre = ?")){
            statement.setString(1,name);
            try (ResultSet result = statement.executeQuery()){
                if (result.next()){
                    Genre genre = new Genre();
                    genre.setId(result.getInt("id"));
                    genre.setName(result.getString("genre"));
                    return genre;
                }
                else {
                    return null;
                }
            }
        }
    }

    /**
     * najde vsetky zanre v databaze.
     * @return vsetky zanre z databazy., alebo empty list ak ziaden zaner neexistuje.
     */
    public List<Genre> getAll() throws SQLException{
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT * FROM genres")){
            try (ResultSet result = statement.executeQuery()){
                List<Genre> all= new ArrayList<>();
                while (result.next()){
                    Genre genre = new Genre();
                    genre.setId(result.getInt("id"));
                    genre.setName(result.getString("genre"));
                    all.add(genre);
                }
                return all;
            }
        }
    }


}
