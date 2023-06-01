package rdg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import connection.DbContext;

public class CustomerFinder{

    public static final CustomerFinder INSTANCE = new CustomerFinder();

    static CustomerFinder getInstance(){
        return INSTANCE;
    }

    /**
     * najde Customera podla mena
     */
    private Customer findByName(String name) throws SQLException {
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT * FROM customer WHERE name = ?")){
            statement.setString(1,name);
            try (ResultSet result = statement.executeQuery()){
                if (result.next()){
                    Customer customer = new Customer();
                    customer.setId(result.getInt("id"));
                    customer.setName(result.getString("name"));
                    customer.setPassword(result.getString("password"));
                    if (result.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    return customer;
                }
                else {
                    return null;
                }
            }
        }
    }

}
