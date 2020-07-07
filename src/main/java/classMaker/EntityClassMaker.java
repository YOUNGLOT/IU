package classMaker;

import classMaker.base.ClassMaker;

import java.util.Arrays;

public class EntityClassMaker extends ClassMaker {

    //region singleton
        private EntityClassMaker() {
        }

        private static EntityClassMaker _instance;

        public static EntityClassMaker getInstance(){
            if (_instance == null)
                _instance = new EntityClassMaker();

            return _instance;
        }

        //endregion

    @Override
    protected String writeSingletonCode(String code){
            return code;
    }

    @Override
    protected String writeImportCode(String code) {
        code += String.format(
                "package entity;\n" +
                        "\n" +
                        "import lombok.Data;\n");
        if (Arrays.asList(tableData.getColumnTypes()).contains("decimal")){
            code += String.format(
                    "import java.sql.BigDecimal;\n");
        }
        return code += "\n";
    }

    @Override
    protected String writeClassCode(String code) {
        code += String.format(
                "@Data\n" +
                        "public class %s{\n" , tableName);
        return code;
    }

    @Override
    protected String writeMethodCode(String code) {
        for (int i = 0; i < tableData.getColumns().length; i++) {
            code += String.format(
                    "\tprivate %s %s;\n"
                    , makeStartLetterSmall(tableData.getColumnTypes()[i])
                    , makeStartLetterSmall(tableData.getColumns()[i]));
        }
        return code;
    }

    @Override
    protected String getMakePath() {
        return "./src/main/java/Entity";
    }

    @Override
    protected String getClassType() {
        return "";
    }
}
