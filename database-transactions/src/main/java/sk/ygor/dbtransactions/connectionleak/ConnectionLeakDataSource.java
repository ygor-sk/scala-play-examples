package sk.ygor.dbtransactions.connectionleak;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.SQLException;

public class ConnectionLeakDataSource extends BasicDataSource {

    public ConnectionLeakDataSource() throws SQLException {
        setUrl("jdbc:h2:mem:play;TRACE_LEVEL_SYSTEM_OUT=2");
        setUsername("sa");
        setPassword("sa");
        setMaxTotal(1);
        setRollbackOnReturn(false);
        setEnableAutoCommitOnReturn(false);
    }

}