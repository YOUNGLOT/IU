package dao.base;

import data.TableData;
import lombok.SneakyThrows;
import core.Core;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public abstract class EntityDao<E> {
    protected String tableName = setTableName();
    protected TableData tableData = new TableData(tableName);

    protected abstract String setTableName();

    //helper에서 가져온 커넥션 함수
    @SneakyThrows
    protected final Connection getConnection() {
        return Core.getInstance().getConnection();
    }

    //region abstract methods

    protected abstract E readEntity(ResultSet result);
    public abstract int update(E entity);
    public abstract int insert(E entity);
    //endregion

    //region base Methods

    @SneakyThrows
    protected final E getOne(String query, ParameterSetter parameterSetter){
        Connection connection = getConnection();

        PreparedStatement statement = connection.prepareStatement(query);

        if (parameterSetter != null)
            parameterSetter.setValue(statement);

        ResultSet result = statement.executeQuery();

        ArrayList<E> entities = new ArrayList<>();
        while (result.next()){
            E entity = readEntity(result);
            entities.add(entity);
        }

        result.close();
        statement.getConnection().close();
        statement.close();
        connection.close();

        return entities.size() == 0 ? null : entities.get(0);
    }

    @SneakyThrows
    protected final ArrayList<E> getMany(String query, ParameterSetter parameterSetter) {
        Connection connection = getConnection();

        PreparedStatement statement = connection.prepareStatement(query);

        if (parameterSetter != null)
            parameterSetter.setValue(statement);

        ResultSet result = statement.executeQuery();

        ArrayList<E> entities = new ArrayList<>();
        while (result.next()){
            E entity = readEntity(result);
            entities.add(entity);
        }

        result.close();
        statement.getConnection().close();
        statement.close();
        connection.close();

        return entities;
    }

    @SneakyThrows
    protected final int getInt(String query, ParameterSetter parameterSetter){
        Connection connection = getConnection();

        PreparedStatement statement = connection.prepareStatement(query);

        if (parameterSetter != null)
            parameterSetter.setValue(statement);

        ResultSet result = statement.executeQuery();

        int count = 0;
        while (result.next()){
            count = result.getInt(1);
        }

        result.close();
        statement.getConnection().close();
        statement.close();
        connection.close();

        return count;
    }

    @SneakyThrows
    protected final int execute(String query, ParameterSetter parameterSetter){
        Connection connection = getConnection();

        PreparedStatement statement = connection.prepareStatement(query);
        if (parameterSetter != null)
            parameterSetter.setValue(statement);

        int rowCount = statement.executeUpdate();

        statement.getConnection().close();
        statement.close();
        connection.close();

        return (rowCount == 1) ? 0 : 1 ;
    }

    @SneakyThrows
    public final int getCount(){
        String query = getCountQuery();

        return getInt(query, null);
    }

    @SneakyThrows
    public final ArrayList<E> getAll() {
        //language=TSQL
        String query = getAllQuery();

        return getMany(query, null);
    }

    //endregion

    //region queryMethods

    protected String getByKeyQuery() {
        //language=TSQL
        return  String.format("select * from %s where " + keyQuery(), tableName);
    }

    protected String deleteByKeyQuery() {
        //language=TSQL
        return String.format("Delete %s Where " + keyQuery(), tableName);
    }

    protected String updateQuery() {
        //language=TSQL
        return String.format("update %s set " + notKeyQuery() + " where " + keyQuery(), tableName);
    }

    protected String insertQuery() {
        //language=TSQL
        String query = String.format("insert into %s values (", tableName);
        for (int i = 0; i < tableData.getColumns().length - tableData.getIdentityColumns().length; i++) {
            if( i == 0 ){
                query += " ?";
            }else{
                query += ", ?";
            }
        }
        if(tableData.getIdentityColumns().length == 0){
            return query +=" )";
        }else{
            return query += " ) SELECT @@IDENTITY AS SEQ ";
        }

    }

    protected String getCountQuery() {
        //language=TSQL
        return String.format("select count(*) from %s", tableName);
    }

    protected String getAllQuery() {
        //language=TSQL
        return String.format("select * from %s", tableName);
    }

    private String keyQuery() {
        String query = "";
        for (int i = 0; i < tableData.getPrimaryKeyColumns().length; i++) {
            if( i == 0 ){
                query += String.format(" %s = ? ", tableData.getPrimaryKeyColumns()[i]);
            }else{
                query += String.format(", %s = ? ", tableData.getPrimaryKeyColumns()[i]);
            }
        }
        return query;
    }

    private String notKeyQuery() {
        String query = "";
        for (int i = 0; i < tableData.getNotPrimaryKeyColumns().length; i++) {
            if( i == 0 ){
                query += String.format(" %s = ? ", tableData.getColumns()[i]);
            }else{
                query += String.format(", %s = ? ", tableData.getColumns()[i]);
            }
        }
        return query;
    }
    //endregion




}
