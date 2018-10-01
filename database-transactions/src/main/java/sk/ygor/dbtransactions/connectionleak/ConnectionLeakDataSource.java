package sk.ygor.dbtransactions.connectionleak;

import org.apache.commons.dbcp2.BasicDataSource;

public class ConnectionLeakDataSource extends BasicDataSource {

    public ConnectionLeakDataSource() {
        setUrl("jdbc:h2:mem:ConnectionLeakDataSource;TRACE_LEVEL_SYSTEM_OUT=2");
        setUsername("sa");
        setPassword("sa");
        setMaxTotal(1);
        setRollbackOnReturn(false);
        setEnableAutoCommitOnReturn(false);
    }

}