package dao.base;

public abstract class EntityDao<E> extends Dao<E> {

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

    @Override
    protected String getCountQuery() {
        //language=TSQL
        return String.format("select count(*) from %s", tableName);
    }

    @Override
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

}