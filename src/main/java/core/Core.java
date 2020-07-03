package core;

import classMaker.DaoClassMaker;
import classMaker.EntityClassMaker;
import classMaker.JsonLoadClassMaker;
import classMaker.TxtLoadClassMaker;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Core {

    //region singleton
    private Core() {
    }

    private static Core _instance;

    public static Core getInstance() {
        if (_instance == null)
            _instance = new Core();

        return _instance;
    }

    //endregion

    //커넥션은 조건없이 여기로, 다른커넥션 쓰면 꼬인다   중요한 Method라 맨위
    public final Connection getConnection() throws SQLException {

        String connString = "jdbc:sqlserver://14.32.18.226:1433;database=YL1;user=as;password=1234";

        return DriverManager.getConnection(connString);
    }

    @SneakyThrows
    public void makeEntityAndDaoClass(){
        ArrayList<String> tableNames = getTableNames_MSSQL();
        for (int i = 0; i < tableNames.size(); i++) {
            EntityClassMaker.getInstance().makeClass(tableNames.get(i));
            DaoClassMaker.getInstance().makeClass(tableNames.get(i));
        }
    }

    @SneakyThrows
    public void makeLoadClass(){
        ArrayList<String> tableNames = getTableNames_MSSQL();
        for (int i = 0; i < tableNames.size(); i++) {
            JsonLoadClassMaker.getInstance().makeClass(tableNames.get(i));
            TxtLoadClassMaker.getInstance().makeClass(tableNames.get(i));
        }
    }

    public void foo() throws IOException {

        String directory = "./city_mark_info";

            String[] fileNames = getfileNames(directory);

            for (int i = 0; i < fileNames.length; i++) {
                System.out.println(String.format(" 시작합니당 : %s 파일 시작!", fileNames[i]));
                //ShopJsonLoad.getInstance().load(String.format("%s/%s", directory, fileNames[i]));
                //identity = ShopTypeCodeLoad.getInstance().load(String.format("%s/%s", directory, fileNames[i]), identity);
            }
    }

    public String[] getfileNames(String directory){
        File path = new File(directory);

        final String pattern = "";

        String[] fileList = path.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(pattern);
            }
        });
        if (fileList.length > 0) {
            for (int i = 0; i < fileList.length; i++) {
                System.out.println(fileList[i]);
            }
        }
        return fileList;
    }

    public boolean makeFile(String directory, String name, String formatType, String txt) {

        String filedirectory = String.format("%s/%s.%s", directory, name, formatType);

        try {
            File file = new File(filedirectory);
            FileWriter fw = new FileWriter(file, true);

            fw.write(txt);
            fw.flush();
            fw.close();

            return true;
        } catch (
                Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList getTableNames_MSSQL() throws SQLException {

        Connection con = getConnection();
        ArrayList<String> arrayList = new ArrayList<>();

        try {
            DatabaseMetaData dbmd = con.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rs = dbmd.getTables(null, null, "%", types);
            while (rs.next()) {
                if (!rs.getString("TABLE_NAME").contains("trace") && !rs.getString("TABLE_NAME").contains("diagram")) {
                    arrayList.add(rs.getString("TABLE_NAME"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        arrayList.remove("Code");
        arrayList.remove("CodeCategory");
        return arrayList;
    }
}
