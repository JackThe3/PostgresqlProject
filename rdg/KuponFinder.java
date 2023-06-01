package rdg;
import connection.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KuponFinder{
    /**
     * finder Prebrany zo vzoroveho projektu
     */
    private static final KuponFinder INSTANCE = new KuponFinder();
    public static KuponFinder getInstance() {
        return INSTANCE;
    }
    public KuponFinder() {}


    public Kupon findByCode(String code) throws SQLException{ ;
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("SELECT * FROM kupon WHERE code = ?")){
            statement.setString(1,code);
            try (ResultSet result = statement.executeQuery()){
                if (result.next()){
                    Kupon kupon = new Kupon();
                    kupon.setId(result.getInt("id"));
                    kupon.setCustomerId(result.getInt("customer_id"));
                    kupon.setCode(result.getString("code"));
                    int res = result.getInt("reservation_id");
                    if (result.wasNull()){
                        kupon.setReservationId(-1);
                    }
                    else{
                        kupon.setReservationId(res);
                    }

                    return kupon;
                }
                else {
                    return null;
                }
            }
        }
    }
}
