package classMaker;

import classMaker.base.LoadClassMaker;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Arrays;

public class TxtLoadClassMaker extends LoadClassMaker {

    //region singleton
        private TxtLoadClassMaker() {
        }

        private static TxtLoadClassMaker _instance;

        public static TxtLoadClassMaker getInstance(){
            if (_instance == null)
                _instance = new TxtLoadClassMaker();

            return _instance;
        }

        //endregion

    @Override
    protected String writeImportCode(String code) {
        code += String.format(
                "package load.txtLoad;\n" +
                        "import entity.%s;\n" +
                        "import dao.%sDao;\n" +
                        "import helper.runnable.ParallelInsert;\n" +
                        "import java.util.ArrayList;\n" +
                        "import lombok.SneakyThrows;\n" +
                        "import load.base.TxtLoad;\n" +
                        "import java.io.BufferedReader;\n"
                , tableName, tableName);
        return code += "\n";
    }

    @Override
    protected String writeClassCode(String code) {
        code += String.format(
                "public class %sTxtLoad extends TxtLoad<%s> {\n" +
                        "private String characterSet;\n"
                , tableName, tableName);
        return code += "\n";
    }

    @Override
    protected String writeMethodCode(String code) throws SQLException {
        code = writeSetEntityCode(code);
        code = writeSetCharacterSetCode(code);
        code = writeEtlCode(code);
        code = writeSetFirstIdentityCode(code);
        code = writeGetEntitiesCode(code);
        code = writeCheckConditionCode(code);
        code = writeInsertCode(code);
        code = writeMakeExceptionsTextFileNameCode(code);

        return code;
    }

    @Override
    protected String getMakePath() {
        return "./src/main/java/load/txtLoad";
    }

    @Override
    protected String getClassType() {
        return "TxtLoad";
    }

    //region Write Code 구현
    private String writeSetEntityCode(String code) throws SQLException {
        code += String.format(
                "\t@Override\n" +
                        "\tprotected %s setEntity(String[] array) {\n" +
                        "\n\t\t%s %s = new %s();\n" +
                        "\t\t//todo array와 매칭 시키세요~~" +
                        "\n", tableName, tableName, makeStartLetterSmall(tableName), tableName);
        for (int i = 0; i < tableData.getColumns().length; i++) {
            code += makeSetCode(tableData.getColumns()[i], tableData.getColumnTypes()[i], i);
        }

        code += String.format(
                "\n\t\treturn %s;" +
                        "\n\t}\n", makeStartLetterSmall(tableName));
        return code +="\n";
    }
    private String writeSetCharacterSetCode(String code){
        code += String.format(
                "\t@Override\n" +
                        "\tprotected String setCharacterSet() {\n" +
                        "\t\treturn characterSet;\n" +
                        "\t}\n");
        return code += "\n";
    }
    private String makeSetCode(String colmnName, String columnType, int i) throws SQLException {
        if (Arrays.stream(tableData.getIdentityColumns()).anyMatch(colmnName::equals)) {
            return "";
        }else{
            if(colmnName.equals("CodeId")||colmnName.equals("CodeCategoryId")){
                return String.format("\t\t%s.set%s(stringToInt(array[%d]));\n"
                        ,makeStartLetterSmall(tableName), makeStartLetterUpper(colmnName), i);
            }else if (colmnName.equals("CodeName")||colmnName.equals("CodeCategoryName")) {
                return String.format("\t\t%s.set%s(array[%d]);\n"
                        ,makeStartLetterSmall(tableName), makeStartLetterUpper(colmnName), i);
            }else if(colmnName.contains("Code")){
                return String.format("\t\t%s.set%s(nameToCode(array[%d]));\n"
                        ,makeStartLetterSmall(tableName), makeStartLetterUpper(colmnName), i);
            }else if (columnType.equals("int")){
                return String.format("\t\t%s.set%s(stringToInt(array[%d]));\n"
                        ,makeStartLetterSmall(tableName), makeStartLetterUpper(colmnName), i);
            }else if (columnType.equals("nvarchar")){
                return String.format("\t\t%s.set%s(array[%d]);\n"
                        ,makeStartLetterSmall(tableName), makeStartLetterUpper(colmnName), i);
            }else{
                return String.format("\t\t%s.set%s(array[%d]);\n" +
                                "\t\t//todo 맞는 타입을 수동으로 변환\n"
                        ,makeStartLetterSmall(tableName), makeStartLetterUpper(colmnName), i);
            }
        }
    }
    private String writeEtlCode(String code){
        code += String.format(
                "\t//runnable 미사용시 해당 함수 비활성화\n" +
                        "\t@SneakyThrows\n" +
                        "\t@Override\n" +
                        "\tprotected void etl(BufferedReader object) {\n" +
                        "\t\tParallelInsert.getInstance().parallelInsert(new ParallelInsert.Run.RunningMethod() {\n" +
                        "\t\t\t@Override\n" +
                        "\t\t\tpublic void runningMethod() {\n" +
                        "\t\t\t\tsynchronized (ParallelInsert.class) {\n" +
                        "\t\t\t\t\tloading(object);\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t}\n" +
                        "\t\t});\n" +
                        "\t}\n"
                , tableName);
        return code+="\n";
    }
    private String writeSetFirstIdentityCode(String code){
        code += String.format(
                "\t@Override\n" +
                        "\tprotected int setFirstIdentity() {\n" +
                        "\t\treturn 0;\n" +
                        "\t\t//todo Identity 시작값을 Set하는 Method\n" +
                        "\t}\n");
        return code+="\n";
    }
    private String writeGetEntitiesCode(String code){
        code += String.format(
                "\t@Override\n" +
                        "\tprotected ArrayList getEntities() {\n" +
                        "\t\ttry { return %sDao.getInstance().getAll(); } catch (Exception e){ return entities; }\n" +
                        "\t}\n",tableName);
        return code+="\n";
    }
    private String writeInsertCode(String code){
        code += String.format(
                "\t@Override\n" +
                        "\tprotected int insert(%s entity){\n" +
                        "\t\treturn %sDao.getInstance().insert(entity);\n" +
                        "\t}\n"
                , tableName, tableName);
        return code += "\n";
    }
    private String writeCheckConditionCode(String code){
        code += String.format(
                "\t@Override\n" +
                        "\t protected boolean checkCondition(%s entity) {\n" +
                        "\t\t/*// 해당 메소드를 사용하려면 Entity Class에 Equals && HashCode 메소드를 재구현 해주세요\n" +
                        "\t\tfor (int i = 0; i < entities.size(); i++) {\n" +
                        "\t\t\tif(entities.get(i).equals(entity)){\n" +
                        "\t\t\t\treturn false;\n" +
                        "\t\t\t}\n" +
                        "\t\t}*/\n" +
                        "\t\treturn true;\n" +
                        "\t}\n", tableName);
        return code+="\n";
    }
    private String writeMakeExceptionsTextFileNameCode(String code){
        code += String.format(
                "\t@Override\n" +
                        "\tprotected String makeExceptionsTextFileName() {\n" +
                        "\t\treturn \"%sTxtLoadExceptions\";\n" +
                        "\t}\n", tableName);
        return code += "\n";
    }
    //endregion
}
