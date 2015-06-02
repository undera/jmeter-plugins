/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kg.apc.jmeter.dbmon;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

class TestConnection implements Connection {

    private final TestConnectionDataProvider dataProvider;

    public TestConnection(TestConnectionDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return new TestStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void commit() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void rollback() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isClosed() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getCatalog() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clearWarnings() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getHoldability() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Clob createClob() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Blob createBlob() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public NClob createNClob() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSchema(String s) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSchema() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setNetworkTimeout(Executor executor, int i) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    static ResultSet resultSet(double d) {
        return new TestResultSet(d);
    }

    public interface TestConnectionDataProvider {
        public ResultSet getQueryResult(String sql);
    }

    private class TestStatement implements Statement {
        public TestStatement() {
        }

        @Override
        public ResultSet executeQuery(String sql) throws SQLException {
            return dataProvider.getQueryResult(sql);
        }

        @Override
        public int executeUpdate(String sql) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void close() throws SQLException {
        }

        @Override
        public int getMaxFieldSize() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setMaxFieldSize(int max) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getMaxRows() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setMaxRows(int max) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setEscapeProcessing(boolean enable) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getQueryTimeout() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setQueryTimeout(int seconds) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void cancel() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public SQLWarning getWarnings() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void clearWarnings() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setCursorName(String name) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean execute(String sql) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public ResultSet getResultSet() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getUpdateCount() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean getMoreResults() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setFetchDirection(int direction) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getFetchDirection() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setFetchSize(int rows) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getFetchSize() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getResultSetConcurrency() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getResultSetType() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void addBatch(String sql) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void clearBatch() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int[] executeBatch() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Connection getConnection() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean getMoreResults(int current) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public ResultSet getGeneratedKeys() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int executeUpdate(String sql, String[] columnNames) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean execute(String sql, int[] columnIndexes) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean execute(String sql, String[] columnNames) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getResultSetHoldability() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isClosed() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setPoolable(boolean poolable) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isPoolable() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void closeOnCompletion() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isCloseOnCompletion() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private static class TestResultSet implements ResultSet {
        private final double value;
        private int row = 0;

        public TestResultSet(double value) {
            this.value = value;
        }

        @Override
        public boolean next() throws SQLException {
            return row++ < 1;
        }

        @Override
        public void close() throws SQLException {
        }

        @Override
        public boolean wasNull() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getString(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean getBoolean(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public byte getByte(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public short getShort(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getInt(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public long getLong(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public float getFloat(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double getDouble(int columnIndex) throws SQLException {
            return value;
        }

        @Override
        public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public byte[] getBytes(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Date getDate(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Time getTime(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Timestamp getTimestamp(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public InputStream getAsciiStream(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public InputStream getUnicodeStream(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public InputStream getBinaryStream(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getString(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean getBoolean(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public byte getByte(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public short getShort(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getInt(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public long getLong(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public float getFloat(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double getDouble(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public byte[] getBytes(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Date getDate(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Time getTime(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Timestamp getTimestamp(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public InputStream getAsciiStream(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public InputStream getUnicodeStream(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public InputStream getBinaryStream(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public SQLWarning getWarnings() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void clearWarnings() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getCursorName() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public ResultSetMetaData getMetaData() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Object getObject(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Object getObject(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int findColumn(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Reader getCharacterStream(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Reader getCharacterStream(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isBeforeFirst() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isAfterLast() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isFirst() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isLast() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void beforeFirst() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void afterLast() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean first() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean last() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getRow() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean absolute(int row) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean relative(int rows) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean previous() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setFetchDirection(int direction) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getFetchDirection() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setFetchSize(int rows) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getFetchSize() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getType() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getConcurrency() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean rowUpdated() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean rowInserted() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean rowDeleted() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateNull(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBoolean(int columnIndex, boolean x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateByte(int columnIndex, byte x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateShort(int columnIndex, short x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateInt(int columnIndex, int x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateLong(int columnIndex, long x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateFloat(int columnIndex, float x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateDouble(int columnIndex, double x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateString(int columnIndex, String x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBytes(int columnIndex, byte[] x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateDate(int columnIndex, Date x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateTime(int columnIndex, Time x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateObject(int columnIndex, Object x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateNull(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBoolean(String columnLabel, boolean x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateByte(String columnLabel, byte x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateShort(String columnLabel, short x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateInt(String columnLabel, int x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateLong(String columnLabel, long x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateFloat(String columnLabel, float x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateDouble(String columnLabel, double x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateString(String columnLabel, String x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBytes(String columnLabel, byte[] x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateDate(String columnLabel, Date x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateTime(String columnLabel, Time x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateObject(String columnLabel, Object x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void insertRow() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateRow() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void deleteRow() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void refreshRow() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void cancelRowUpdates() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void moveToInsertRow() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void moveToCurrentRow() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Statement getStatement() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Ref getRef(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Blob getBlob(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Clob getClob(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Array getArray(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Ref getRef(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Blob getBlob(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Clob getClob(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Array getArray(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Date getDate(int columnIndex, Calendar cal) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Date getDate(String columnLabel, Calendar cal) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Time getTime(int columnIndex, Calendar cal) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Time getTime(String columnLabel, Calendar cal) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public URL getURL(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public URL getURL(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateRef(int columnIndex, Ref x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateRef(String columnLabel, Ref x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBlob(int columnIndex, Blob x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBlob(String columnLabel, Blob x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateClob(int columnIndex, Clob x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateClob(String columnLabel, Clob x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateArray(int columnIndex, Array x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateArray(String columnLabel, Array x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public RowId getRowId(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public RowId getRowId(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateRowId(int columnIndex, RowId x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateRowId(String columnLabel, RowId x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getHoldability() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isClosed() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateNString(int columnIndex, String nString) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateNString(String columnLabel, String nString) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public NClob getNClob(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public NClob getNClob(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public SQLXML getSQLXML(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public SQLXML getSQLXML(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getNString(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getNString(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Reader getNCharacterStream(int columnIndex) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Reader getNCharacterStream(String columnLabel) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateClob(int columnIndex, Reader reader) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateClob(String columnLabel, Reader reader) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateNClob(int columnIndex, Reader reader) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void updateNClob(String columnLabel, Reader reader) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T> T getObject(int i, Class<T> aClass) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T> T getObject(String s, Class<T> aClass) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }


}
