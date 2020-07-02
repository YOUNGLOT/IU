package classMaker;

import classMaker.base.ClassMaker;

import java.sql.SQLException;
import java.util.Arrays;

public class DaoClassMaker extends ClassMaker {

    //region singleton
    private DaoClassMaker() {
    }

    private static DaoClassMaker _instance;

    public static DaoClassMaker getInstance() {
        if (_instance == null)
            _instance = new DaoClassMaker();

        return _instance;
    }

    //endregion

    @Override
    protected String writeImportCode(String code) {
        code += String.format(
                "package dao;\n" +
                        "import entity.%s;\n" +
                        "import dao.base.ParameterSetter;\n" +
                        "import dao.base.EntityDao;\n" +
                        "import lombok.SneakyThrows;\n" +
                        "import java.sql.PreparedStatement;\n" +
                        "import java.sql.ResultSet;\n"
                , tableName);
        return code += "\n";
    }

    @Override
    protected String writeClassCode(String code) {
        code += String.format(
                "public class %sDao extends EntityDao<%s> {\n"
                , tableName, tableName);
        return code;
    }

    @Override
    protected String writeMethodCode(String code) throws SQLException {

        code = writeSetTableName(code);
        code = writeReadEntity(code);
        code = writeUpdate(code);
        code = writeInsert(code);
        code = writeGetByKey(code);
        code = writeDeleteByKey(code);

        return code;
    }

    @Override
    protected String getMakePath() {
        return "./src/main/java/dao";
    }

    @Override
    protected String getClassType() {
        return "Dao";
    }

    private String writeSetTableName(String code) {
        code += String.format(
                "\t@Override\n" +
                        "\tprotected String setTableName() {\n" +
                        "\t\treturn \"%s\";\n" +
                        "\t}\n"
                , tableName);
        return code += "\n";
    }

    private String writeReadEntity(String code) {
        code += String.format(
                "\t@SneakyThrows\n" +
                        "\t@Override\n" +
                        "\tprotected %s readEntity(ResultSet result) {\n" +
                        "\t\t%s entity = new %s();\n\n"
                , tableName, tableName, tableName);

        for (int i = 0; i < tableData.getColumns().length; i++) {
            code += String.format(
                    "\t\tentity.set%s(result.get%s(%d));\n"
                    , makeStartLetterUpper(tableData.getColumns()[i]), refineType(tableData.getColumnTypes()[i]), i + 1);
        }

        code += String.format(
                "\n\treturn entity;\n\t}\n");
        return code += "\n";
    }

    private String writeUpdate(String code) {
        code += String.format(
                "\t@Override\n" +
                        "\tpublic int update(%s entity) {\n\n" +
                        "\t\tString query = updateQuery();\n\n"
                , tableName, tableName);
        code += String.format(
                "\t\treturn execute(query, new ParameterSetter() {\n\n" +
                        "\t\t\t@SneakyThrows\n" +
                        "\t\t\t@Override\n" +
                        "\t\t\tpublic void setValue(PreparedStatement statement) {\n\n");
        for (int i = 0; i < tableData.getColumns().length - 1; i++) {
            code += String.format(
                    "\t\t\t\tstatement.set%s(%d, entity.get%s());\n", refineType(tableData.getColumnTypes()[i + 1]), i + 1, makeStartLetterUpper(tableData.getColumns()[i + 1]));
            if (i == tableData.getColumns().length - 2) {
                code += String.format(
                        "\t\t\t\tstatement.set%s(%d, entity.get%s());\n", refineType(tableData.getColumnTypes()[0]), i + 2, tableData.getColumns()[0]);
            }
        }
        return code += String.format(
                "\t\t\t}\n" +
                        "\t\t});\n" +
                        "\t}\n\n");
    }

    private String writeInsert(String code) {
        code += String.format(
                "\tpublic int insert(%s entity) {\n\n" +
                        "\t\tString query = insertQuery();\n\n"
                , tableName);
        if (tableData.getIdentityColumns().length == 0) {
            code += String.format(
                    "\t\treturn execute(query, new ParameterSetter() {\n\n"
            );
        } else {
            code += String.format(
                    "\t\treturn getInt(query, new ParameterSetter() {\n\n"
            );
        }
        code += String.format(
                "\t\t\t@SneakyThrows\n" +
                        "\t\t\t@Override\n" +
                        "\t\t\tpublic void setValue(PreparedStatement statement) {\n\n"
        );
        int count = 1;
        for (int i = 0; i < tableData.getColumns().length; i++) {
            if (!Arrays.stream(tableData.getIdentityColumns()).anyMatch(tableData.getColumns()[i]::equals)) {
                code += String.format(
                        "\t\t\t\tstatement.set%s(%d, entity.get%s());\n"
                        , refineType(tableData.getColumnTypes()[i]), count, makeStartLetterUpper(tableData.getColumns()[i]));
                count++;
            }
        }
        return code += String.format(
                "\t\t\t}\n" +
                        "\t\t});\n" +
                        "\t}\n\n");
    }

    private String writeGetByKey(String code) {
        code += String.format(
                "\tpublic %s getByKey("
                , tableName);
        int count = 0;
        for (int i = 0; i < tableData.getColumns().length; i++) {
            if (Arrays.stream(tableData.getPrimaryKeyColumns()).anyMatch(tableData.getColumns()[i]::equals)) {
                if (count == 0) {
                    code += String.format(
                            "%s %s"
                            , makeStartLetterSmall(tableData.getColumnTypes()[i]), makeStartLetterSmall(tableData.getColumns()[i]));
                    count++;
                } else {
                    code += String.format(
                            ",%s %s"
                            , makeStartLetterSmall(tableData.getColumnTypes()[i]), makeStartLetterSmall(tableData.getColumns()[i]));
                }
            }
        }
        code += String.format(
                ") {\n" +
                        "\n" +
                        "\t\tString query = getByKeyQuery();\n" +
                        "\n" +
                        "\t\treturn getOne(query, new ParameterSetter() {\n" +
                        "\t\t\t@SneakyThrows\n" +
                        "\t\t\t@Override\n" +
                        "\t\t\tpublic void setValue(PreparedStatement statement) {\n"
        );
        count = 1;
        for (int i = 0; i < tableData.getColumns().length; i++) {
            if (Arrays.stream(tableData.getPrimaryKeyColumns()).anyMatch(tableData.getColumns()[i]::equals)) {
                code += String.format(
                        "\t\t\t\tstatement.set%s(%d, %s);\n"
                        , makeStartLetterUpper(tableData.getColumnTypes()[i]), count, makeStartLetterSmall(tableData.getColumns()[i]));
                count++;
            }
        }
        return code += String.format(
                "\t\t\t}\n" +
                        "\t\t });\n" +
                        "\t}\n\n"
        );
    }

    private String writeDeleteByKey(String code) {
        code += String.format(
                "\tpublic int deleteByKey("
                , tableName);
        int count = 0;
        for (int i = 0; i < tableData.getColumns().length; i++) {
            if (Arrays.stream(tableData.getPrimaryKeyColumns()).anyMatch(tableData.getColumns()[i]::equals)) {
                if (count == 0) {
                    code += String.format(
                            "%s %s"
                            , makeStartLetterSmall(tableData.getColumnTypes()[i]), makeStartLetterSmall(tableData.getColumns()[i]));
                    count++;
                } else {
                    code += String.format(
                            ",%s %s"
                            , makeStartLetterSmall(tableData.getColumnTypes()[i]), makeStartLetterSmall(tableData.getColumns()[i]));
                }
            }
        }
        code += String.format(
                ") {\n" +
                        "\n" +
                        "\t\tString query = deleteByKeyQuery();\n" +
                        "\n" +
                        "\t\treturn execute(query, new ParameterSetter() {\n" +
                        "\t\t\t@SneakyThrows\n" +
                        "\t\t\t@Override\n" +
                        "\t\t\tpublic void setValue(PreparedStatement statement) {\n"
        );
        count = 1;
        for (int i = 0; i < tableData.getColumns().length; i++) {
            if (Arrays.stream(tableData.getPrimaryKeyColumns()).anyMatch(tableData.getColumns()[i]::equals)) {
                code += String.format(
                        "\t\t\t\tstatement.set%s(%d, %s);\n"
                        , makeStartLetterUpper(tableData.getColumnTypes()[i]), count, makeStartLetterSmall(tableData.getColumns()[i]));
                count++;
            }
        }
        return code += String.format(
                "\t\t\t}\n" +
                        "\t\t });\n" +
                        "\t}\n\n"
        );
    }

    protected String refineType(String columnType) {
        if (columnType == "NUMBER" || columnType == "int") {
            return "Int";
        } else if (columnType == "CHAR" || columnType == "VARCHAR2" || columnType == "nvarchar") {
            return "String";
        } else if (columnType == "DECIMAL") {
            return "BigDecimal";
        } else {
            System.out.println(columnType + " : 지정되지 않아 String으로 처리했습니다. 확인해 주세요");
            return "String";
        }
    }

}
