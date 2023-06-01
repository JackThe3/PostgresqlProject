package rdg;

import java.math.BigDecimal;
import java.sql.*;

import connection.DbContext;

public class Screening {
    public int id;
    BigDecimal price;
    public Timestamp opening;
    Movie movie;
    Theater theater;

    public BigDecimal getPrice() {
        return price;
    }

    public Timestamp getOpening() {
        return opening;
    }

    public Movie getMovie() { return movie;}

    public Theater getTheater() {
        return theater;
    }

    public Screening() {}

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setOpening(Timestamp opening) {
        this.opening = opening;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    @Override
    public String toString() {
        return "Screening{" +
                "id=" + id +
                ", price=" + price +
                ", opening=" + opening +
                ", movie=" + movie +
                ", theater=" + theater +
                '}';
    }

    public Screening(BigDecimal price, Timestamp opening, String MovieName, int MovieYear , String theaterName) throws SQLException {
        this.movie = MovieFinder.getInstance().find(MovieName, MovieYear);
        this.theater = TheaterFinder.getInstance().find(theaterName);
        this.opening = opening;
        this.price = price;
    }

    public void insert() throws SQLException{
        try(PreparedStatement statement = DbContext.getConnection().prepareStatement("INSERT INTO screening(price, opening, movie_id, theater_id) VALUES(?,?,?,?)",Statement.RETURN_GENERATED_KEYS)){
            statement.setBigDecimal(1, price);
            statement.setTimestamp(2, opening);
            statement.setInt(3, movie.getId());
            statement.setInt(4, theater.getId());

            statement.executeUpdate();
            try(ResultSet resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                id = resultSet.getInt(1);
            }
        }
    }
}
