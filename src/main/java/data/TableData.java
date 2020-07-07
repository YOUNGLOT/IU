package data;

import core.Core;
import lombok.Data;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

@Data
public class TableData {
    protected String[] columns;
    protected String[] columnTypes;
    protected String[] primaryKeyColumns;
    protected String[] notPrimaryKeyColumns;
    protected String[] identityColumns;
    protected String[] notIdentityColumns;

    @SneakyThrows
    public TableData(String tableName) {

        Connection conn = Core.getInstance().getConnection();
        ResultSetMetaData resultSetMetaData = conn.prepareStatement(String.format("select * from %s", tableName)).executeQuery().getMetaData();

        String[] columns = new String[resultSetMetaData.getColumnCount()];
        String[] columnTypes = new String[resultSetMetaData.getColumnCount()];

        for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
            columns[i] = resultSetMetaData.getColumnName(i + 1);
            columnTypes[i] = refineType(resultSetMetaData.getColumnTypeName(i + 1));
        }
         //Method 이름 마지막에 "_" 붙인이유 : getter Method 재정의 방지
        this.columns = columns;
        this.columnTypes = columnTypes;
        this.primaryKeyColumns = getPkColumns(conn, tableName);
        this.notPrimaryKeyColumns = getNotPrimaryKeyColumns_();
        this.identityColumns = getIdentityColumns(conn, tableName);
        this.notIdentityColumns = getNotIdentityColumns_();

        conn.close();
    }

    @SneakyThrows
    private String[] getPkColumns(Connection conn, String tableName) {
        String query = String.format("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME='%s' AND CONSTRAINT_NAME = 'PK_%s'", tableName, tableName);

        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet result = statement.executeQuery();

        ArrayList<String> pkNames = new ArrayList<>();

        while (result.next()) {
            String pkName = result.getString(1);
            pkNames.add(pkName);
        }

        String[] array = new String[pkNames.size()];

        for (int i = 0; i < pkNames.size(); i++) {
            array[i] = pkNames.get(i);
        }

        return array;
    }

    private String[] getNotPrimaryKeyColumns_(){
        String[] notPrimaryKeyColumns = new String[columns.length - primaryKeyColumns.length];
        int count = 0;
        for (int i = 0; i < columns.length; i++) {
            if(!Arrays.stream(primaryKeyColumns).anyMatch(columns[i]::equals)){
                notPrimaryKeyColumns[count] = columns[i];
                count++;
            }
        }
        return notPrimaryKeyColumns;
    }

    @SneakyThrows
    private String[] getIdentityColumns(Connection conn, String tableName) {
        String query = "SELECT B.name AS [Table], A.name AS [Colum] FROM syscolumns A JOIN sysobjects B ON B.id = A.id WHERE A.status = 128 AND B.name NOT LIKE 'queue_messages_%'";

        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet result = statement.executeQuery();

        ArrayList<String> arrayList = new ArrayList<>();
        while (result.next()) {
            if (result.getString(1).equals(tableName)) {
                arrayList.add(result.getString(2));
            }
        }

        result.close();
        statement.close();

        String[] identityColumns = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            identityColumns[i] = arrayList.get(i);
        }

        return identityColumns;
    }

    private String[] getNotIdentityColumns_(){
        String[] notIdentityColumns = new String[columns.length - identityColumns.length];
        int count = 0;
        for (int i = 0; i < columns.length; i++) {
            if(!Arrays.stream(identityColumns).anyMatch(columns[i]::equals)){
                notIdentityColumns[count] = columns[i];
                count++;
            }
        }
        return notIdentityColumns;
    }

    //MSSQL ResultSetMetaData 에서 불러온 types 를 Java와 매칭되는 types로 변경(타임 다듬기!!)
    protected String refineType(String columnType) {
        if (columnType == "NUMBER"||columnType == "int") {
            return "Int";
        } else if (columnType == "CHAR" || columnType == "VARCHAR2"||columnType == "nvarchar"||columnType == "varchar"||columnType == "date") {
            return "String";
        } else if (columnType == "DECIMAL") {
            return "BigDecimal";
        } else {
            System.out.println(columnType + "지정되지 않아 String으로 처리했습니다. 확인해 주세요");
            return "String";
        }
    }

}
