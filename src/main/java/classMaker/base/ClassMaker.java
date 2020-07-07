package classMaker.base;

import core.Core;
import data.TableData;
import lombok.SneakyThrows;

import java.sql.*;

public abstract class ClassMaker {

    protected String tableName;
    protected static Connection conn;
    protected TableData tableData;

    @SneakyThrows
    public boolean makeClass(String tableName) {
        this.tableName = tableName;
        conn = Core.getInstance().getConnection(); //커넥션은 core의 것만 사용합니다.
        this.tableData = new TableData(tableName); //table 정보들을 담아보았어요

        String code = "";

        code = writeImportCode(code);
        code = writeClassCode(code);
        code = writeSingletonCode(code);
        code = writeMethodCode(code);
        code += "\n}";

        try {
            Core.getInstance().makeFile(getMakePath(), tableName + getClassType(), "java", code);
            return true;
        } catch (Exception e) {
            System.out.println("파일생성 실패 : " + e);
            return false;
        }
    }

    //wrtie Methods
    protected abstract String writeImportCode(String code);
    protected abstract String writeClassCode(String code);
    protected String writeSingletonCode(String code){
        code += String.format(
                "\t//region singleton\n" +
                        "\tprivate %s%s() {\n" +
                        "\t}\n" +
                        "\t\n" +
                        "\tprivate static %s%s _instance;\n" +
                        "\t\n" +
                        "\tpublic static %s%s getInstance() {\n" +
                        "\t\tif (_instance == null)\n" +
                        "\t\t\t_instance = new %s%s();\n" +
                        "\t\n" +
                        "\t\treturn _instance;\n" +
                        "\t}\n" +
                        "\t//endregion\n"
                , tableName, getClassType(), tableName, getClassType(), tableName, getClassType(), tableName, getClassType());
        return code += "\n";
    }
    protected abstract String writeMethodCode(String code) throws SQLException;

    //환경설정 메소드들
    protected abstract String getMakePath();
    protected abstract String getClassType();

    //region Helper Methods

    //camelCase 표기를 위해 첫글자 소문자로 변경
    protected String makeStartLetterSmall(String str) {
        if (str == "String" || str == "Date" || str == "BigDecimal") {
            return str;
        } else
            return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    protected String makeStartLetterUpper(String str) {
        if (str == "String" || str == "Date" || str == "BigDecimal") {
            return str;
        } else
            return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    //endregion


}
