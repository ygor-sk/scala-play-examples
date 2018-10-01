package sk.ygor.dbtransactions.connectionleak;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ConnectionLeakDAO {

    private final DataSource dataSource;

    public ConnectionLeakDAO() throws SQLException {
        this.dataSource = new ConnectionLeakDataSource();
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("create table users (id int auto_increment not null primary key)").execute();
            connection.prepareStatement("insert into users (id) values (1)").execute();
        }
    }

    public void insertWithForgottenCommit() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false); // begin transaction
            try (PreparedStatement statement = connection.prepareStatement("insert into users (id) values (null)")) {
                statement.executeUpdate();
            }
            // connection.setAutoCommit(true); // developer forgot to commit or unexpected exception was thrown
        }
    }

    public List<Integer> readRowsWithNoTransaction() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from users")) {
                try (ResultSet rs = statement.executeQuery()) {
                    List<Integer> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(rs.getInt("id"));
                    }
                    return result;
                }
            }
        }
    }

    public void insertWithProperHandling(boolean insertDuplicate) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false); // begin transaction
            String sql = insertDuplicate ?
                    "insert into users (id) values (1)" :
                    "insert into users (id) values (null)";
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            connection.setAutoCommit(true); // commit
        } catch (Exception ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    // do nothing
                }
            }
            throw ex;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // do nothing
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // do nothing
                }
            }
        }
    }

}
