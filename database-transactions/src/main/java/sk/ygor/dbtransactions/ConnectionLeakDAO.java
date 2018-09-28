package sk.ygor.dbtransactions;

import org.apache.commons.dbcp2.BasicDataSource;
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
        this.dataSource = createDataSource();
    }

    public void insertWithForgottenCommit() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false); // begin transaction
            try (PreparedStatement statement = connection.prepareStatement("insert into users (id) values (null)")) {
                statement.executeUpdate();
            }
            // connection.setAutoCommit(true); // developer forgot to commit
        }
    }

    public List<Integer> readRowsWithNoTransaction() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            List<Integer> result = new ArrayList<>();
            try (PreparedStatement statement = connection.prepareStatement("select * from users")) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        result.add(rs.getInt("id"));
                    }
                    return result;
                }
            }
        }
    }

    public void insertWithProperHandling(boolean insertDuplicate) throws Exception {
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
                connection.rollback();
            }
            throw ex;
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    private DataSource createDataSource() throws SQLException {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:h2:mem:play;TRACE_LEVEL_SYSTEM_OUT=2");
        dataSource.setUsername("sa");
        dataSource.setPassword("sa");
        dataSource.setMaxTotal(1);
        dataSource.setRollbackOnReturn(false);
        dataSource.setEnableAutoCommitOnReturn(false);

        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("create table users (id int auto_increment not null primary key)").execute();
            connection.prepareStatement("insert into users (id) values (1)").execute();
        }

        return dataSource;
    }

}