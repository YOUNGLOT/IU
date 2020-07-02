package dao.base;

import data.TableData;
import lombok.SneakyThrows;
import core.Core;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public abstract class Dao<E> {
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

    protected abstract String getCountQuery();

    protected abstract String getAllQuery();
    //endregion

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

    public abstract int update(E entity);
    public abstract int insert(E entity);
}
