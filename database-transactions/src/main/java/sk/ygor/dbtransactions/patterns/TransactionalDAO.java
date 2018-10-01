package sk.ygor.dbtransactions.patterns;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class TransactionalDAO {

    private final DataSource dataSource = createDataSource();

    private static ThreadLocal<Integer> executeInTransactionLevel = ThreadLocal.withInitial(() -> 0);
    private static ThreadLocal<Connection> threadConnection = ThreadLocal.withInitial(() -> null);

    public TransactionalDAO() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("create table foo (id int auto_increment not null primary key)").execute();
            connection.prepareStatement("create table bar (id int auto_increment not null primary key)").execute();
        }
    }

    private DataSource createDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:h2:mem:TransactionalDAO;TRACE_LEVEL_SYSTEM_OUT=2");
        dataSource.setUsername("sa");
        dataSource.setPassword("sa");
        return dataSource;
    }

    private interface DatabaseOperations {
        int update(String sql);
    }

    private <T> T executeInTransaction(Function<DatabaseOperations, T> callback) {
        final Connection[] connection = new Connection[1];
        final PreparedStatement[] statement = new PreparedStatement[1];
        int level = executeInTransactionLevel.get();
        try {
            if (level == 0) {
                assert threadConnection.get() == null;
                connection[0] = dataSource.getConnection();
                threadConnection.set(connection[0]);
                connection[0].setAutoCommit(false); // begin transaction
            } else {
                connection[0] = threadConnection.get();
            }

            T result = callback.apply(sql -> {
                executeInTransactionLevel.set(level + 1);
                try {
                    statement[0] = connection[0].prepareStatement(sql);
                    return statement[0].executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    executeInTransactionLevel.set(level);
                }
            });

            if (level == 0) {
                connection[0].setAutoCommit(true); // commit
            }
            return result;
        } catch (Exception ex) {
            if (level == 0 && connection[0] != null) {
                try {
                    connection[0].rollback();
                } catch (SQLException e) {
                    // no handling
                }
            }
            throw new RuntimeException(ex);
        } finally {
            if (statement[0] != null) {
                try {
                    statement[0].close();
                } catch (SQLException e) {
                    // no handling
                }
            }
            if (level == 0 && connection[0] != null) {
                try {
                    connection[0].close();
                } catch (SQLException e) {
                    // no handling
                }
            }
        }
    }

    public String test() {
        return executeInTransaction(databaseOperations -> {
            testInner();
            int rowsAffected = databaseOperations.update("insert into foo (id) values (null)");
            return "Insert new entry, rows affected: " + rowsAffected;
        });
    }

    private void testInner() {
        executeInTransaction(databaseOperations ->
                databaseOperations.update("insert into bar (id) values (null)")
        );
    }

    public List<Integer> list() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from foo left join bar on foo.id = bar.id")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Integer> result = new ArrayList<>();
                    while (resultSet.next()) {
                        result.add(resultSet.getInt(1));
                        result.add(resultSet.getInt(2));
                    }
                    return result;
                }
            }
        }
    }


}
