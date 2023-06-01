package rdg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import connection.DbContext;

public class Customer {
    Integer id;
    String name;
    String password;

    public Customer(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Customer() {

    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }


    /**
     * Vlozy Customera do databazy
     */
    public void insert() throws SQLException {
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("INSERT INTO customer(name, password) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, name);
            statement.setString(2,password);
            statement.executeUpdate();
            try(ResultSet resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                id = resultSet.getInt(1);
            }
        }
    }

    /**
     * zmaze Customera do databazy
     */
    public void delete() throws SQLException {

        try (PreparedStatement statement = DbContext.getConnection().prepareStatement("DELETE FROM customer WHERE name = ?")) {
            statement.setString(1, name);
            statement.executeUpdate();
        }
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
