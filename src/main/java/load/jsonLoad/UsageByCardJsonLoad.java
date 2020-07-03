package load.jsonLoad;
import entity.UsageByCard;
import dao.UsageByCardEntityDao;
import helper.runnable.ParallelInsert;
import load.base.JsonLoad;;
import org.json.simple.JSONArray;import org.json.simple.JSONObject;
import java.util.ArrayList;
import lombok.SneakyThrows;

public class UsageByCardJsonLoad extends JsonLoad<UsageByCard> {

	//region singleton
	private UsageByCardJsonLoad() {
	}
	
	private static UsageByCardJsonLoad _instance;
	
	public static UsageByCardJsonLoad getInstance() {
		if (_instance == null)
			_instance = new UsageByCardJsonLoad();
	
		return _instance;
	}
	//endregion

	@Override
	protected UsageByCard setEntity(JSONObject object) {
		UsageByCard entity = new UsageByCard();

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
		try { return UsageByCardEntityDao.getInstance().getAll(); } catch (Exception e){ return entities; }
	}

	@Override
	 protected boolean checkCondition(UsageByCard entity) {
		/*// 해당 메소드를 사용하려면 Entity Class에 Equals && HashCode 메소드를 재구현 해주세요
		for (int i = 0; i < entities.size(); i++) {
			if(entities.get(i).equals(entity)){
				return false;
			}
		}*/
		return true;
	}

	@Override
	protected int insert(UsageByCard entity){
		return UsageByCardEntityDao.getInstance().insert(entity);
	}

	@Override
	protected String makeExceptionsTextFileName() {
		return "UsageByCardJsonLoadExceptions";
	}


}