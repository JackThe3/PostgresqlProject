package rdg;
import connection.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Meno zanru je vzdy toUpperCase()
 */
public class Genre {
    Integer id;
    String name;

    public Integer getId(){
        return id;
    }
    public String getName(){
        return name;
    }

    public void setId(Integer id){
        this.id = id;
    }
    public void setName(String name){
        name = name.toUpperCase();
        this.name = name;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Genre(){
    }

    /**
     * Vlozy zaner do databazy.
     */
    public void insert() throws SQLException{
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("INSERT INTO genres(genre) VALUES(?)",Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, name);
            statement.executeUpdate();
            try(ResultSet resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                id = resultSet.getInt(1);
            }
        }
    }

    /**
     * Premenuje zaner v databaze.
     */
    public void update() throws SQLException{
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("UPDATE genres SET genre=? WHERE id = ?")) {
            statement.setString(1, name);
            statement.setInt(2, id);
            statement.executeUpdate();
        }
    }

    /**
     * Zmaze zaner z databazy.
     */
    public void delete() throws SQLException {
        try (PreparedStatement statement = DbContext.getConnection().prepareStatement("DELETE FROM genres WHERE genre = ?")) {
            statement.setString(1, name);
            statement.executeUpdate();
        }
    }
}
