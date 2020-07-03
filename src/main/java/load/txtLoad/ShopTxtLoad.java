package load.txtLoad;
import entity.Shop;
import dao.ShopEntityDao;
import helper.runnable.ParallelInsert;
import java.util.ArrayList;
import lombok.SneakyThrows;
import load.base.TxtLoad;
import java.io.BufferedReader;

public class ShopTxtLoad extends TxtLoad<Shop> {
private String characterSet;

	//region singleton
	private ShopTxtLoad() {
	}
	
	private static ShopTxtLoad _instance;
	
	public static ShopTxtLoad getInstance() {
		if (_instance == null)
			_instance = new ShopTxtLoad();
	
		return _instance;
	}
	//endregion

	@Override
	protected Shop setEntity(String[] array) {

		Shop shop = new Shop();
		//todo array와 매칭 시키세요~~
		shop.setName(array[1]);
		shop.setBusinessCode(nameToCode(array[2]));
		shop.setAddress(array[3]);
		shop.setStreetNameAddress(array[4]);
		shop.setZipcode(stringToInt(array[5]));
		shop.setLatitude(array[6]);
		shop.setLongitude(array[7]);
		shop.setLocationCode(nameToCode(array[8]));

		return shop;
	}

	@Override
	protected String setCharacterSet() {
		return characterSet;
	}

	//runnable 미사용시 해당 함수 비활성화
	@SneakyThrows
	@Override
	protected void etl(BufferedReader object) {
		ParallelInsert.getInstance().parallelInsert(new ParallelInsert.Run.RunningMethod() {
			@Override
			public void runningMethod() {
				synchronized (ParallelInsert.class) {
					loading(object);
				}
			}
		});
	}

	@Override
	protected int setFirstIdentity() {
		return 0;
		//todo Identity 시작값을 Set하는 Method
	}

	@Override
	protected ArrayList getEntities() {
		try { return ShopEntityDao.getInstance().getAll(); } catch (Exception e){ return entities; }
	}

	@Override
	 protected boolean checkCondition(Shop entity) {
		/*// 해당 메소드를 사용하려면 Entity Class에 Equals && HashCode 메소드를 재구현 해주세요
		for (int i = 0; i < entities.size(); i++) {
			if(entities.get(i).equals(entity)){
				return false;
			}
		}*/
		return true;
	}

	@Override
	protected int insert(Shop entity){
		return ShopEntityDao.getInstance().insert(entity);
	}

	@Override
	protected String makeExceptionsTextFileName() {
		return "ShopTxtLoadExceptions";
	}


}