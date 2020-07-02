package load.jsonLoad;
import entity.UsageByGiftCard;
import dao.UsageByGiftCardDao;
import helper.runnable.ParallelInsert;
import load.base.JsonLoad;;
import org.json.simple.JSONArray;import org.json.simple.JSONObject;
import java.util.ArrayList;
import lombok.SneakyThrows;

public class UsageByGiftCardJsonLoad extends JsonLoad<UsageByGiftCard> {

	//region singleton
	private UsageByGiftCardJsonLoad() {
	}
	
	private static UsageByGiftCardJsonLoad _instance;
	
	public static UsageByGiftCardJsonLoad getInstance() {
		if (_instance == null)
			_instance = new UsageByGiftCardJsonLoad();
	
		return _instance;
	}
	//endregion

	@Override
	protected UsageByGiftCard setEntity(JSONObject object) {
		UsageByGiftCard entity = new UsageByGiftCard();

		entity.setLocationCode(nameToCode((String) object.get("")));
		entity.setDate((String) object.get(""));
		entity.setCharge(stringToInt((String) object.get("")));
		entity.setSpend(stringToInt((String) object.get("")));

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
		try { return UsageByGiftCardDao.getInstance().getAll(); } catch (Exception e){ return entities; }
	}

	@Override
	 protected boolean checkCondition(UsageByGiftCard entity) {
		/*// 해당 메소드를 사용하려면 Entity Class에 Equals && HashCode 메소드를 재구현 해주세요
		for (int i = 0; i < entities.size(); i++) {
			if(entities.get(i).equals(entity)){
				return false;
			}
		}*/
		return true;
	}

	@Override
	protected int insert(UsageByGiftCard entity){
		return UsageByGiftCardDao.getInstance().insert(entity);
	}

	@Override
	protected String makeExceptionsTextFileName() {
		return "UsageByGiftCardJsonLoadExceptions";
	}


}