import core.Core;
import dao.ShopDao;

public class Main {
    public static void main(String[] args) {

        //core.Core 에서 Conection 설정후
        //Core.getInstance().makeEntityAndDaoClass();
        Core.getInstance().makeLoadClass();


        //적제 해 볼까?

        /*String folderDirectory = "./data";

        String[] fileNames = Core.getInstance().getfileNames(folderDirectory);

        for (int i = 0; i < fileNames.length; i++) {

            System.out.println(String.format(" 시작합니당 : %s 파일 시작!", fileNames[i]));

            String fileDirectory = String.format("%s/%s", folderDirectory, fileNames[i]);

            //todo : load함수 넣어요

        }*/
    }
}
