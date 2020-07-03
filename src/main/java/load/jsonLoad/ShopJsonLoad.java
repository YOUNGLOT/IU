package load.jsonLoad;
import entity.Shop;
import dao.ShopEntityDao;
import helper.runnable.ParallelInsert;
import load.base.JsonLoad;;
import org.json.simple.JSONArray;import org.json.simple.JSONObject;
import java.util.ArrayList;
import lombok.SneakyThrows;

public class ShopJsonLoad extends JsonLoad<Shop> {

	//region singleton
	private ShopJsonLoad() {
	}
	
	private static ShopJsonLoad _instance;
	
	public static ShopJsonLoad getInstance() {
		if (_instance == null)
			_instance = new ShopJsonLoad();
	
		return _instance;
	}
	//endregion

	@Override
	protected Shop setEntity(JSONObject object) {
		Shop entity = new Shop();

		entity.setName((String) object.get(""));
		entity.setBusinessCode(nameToCode((String) object.get("")));
		entity.setAddress((String) object.get(""));
		entity.setStreetNameAddress((String) object.get(""));
		entity.setZipcode(stringToInt((String) object.get("")));
		entity.setLatitude((String) object.get(""));
		entity.setLongitude((String) object.get(""));
		entity.setLocationCode(nameToCode((String) object.get("")));

		return entity;
	}

	//runnable 미사용시 해당 함수 비활성화
	@SneakyThrows
	@Override
	protected void etl(JSONArray object) {
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
		return "ShopJsonLoadExceptions";
	}


}