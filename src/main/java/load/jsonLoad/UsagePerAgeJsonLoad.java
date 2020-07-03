package load.jsonLoad;
import entity.UsagePerAge;
import dao.UsagePerAgeEntityDao;
import helper.runnable.ParallelInsert;
import load.base.JsonLoad;;
import org.json.simple.JSONArray;import org.json.simple.JSONObject;
import java.util.ArrayList;
import lombok.SneakyThrows;

public class UsagePerAgeJsonLoad extends JsonLoad<UsagePerAge> {

	//region singleton
	private UsagePerAgeJsonLoad() {
	}
	
	private static UsagePerAgeJsonLoad _instance;
	
	public static UsagePerAgeJsonLoad getInstance() {
		if (_instance == null)
			_instance = new UsagePerAgeJsonLoad();
	
		return _instance;
	}
	//endregion

	@Override
	protected UsagePerAge setEntity(JSONObject object) {
		UsagePerAge entity = new UsagePerAge();

		entity.setDate((String) object.get(""));
		entity.setLocationCode(nameToCode((String) object.get("")));
		entity.setAgeCode(nameToCode((String) object.get("")));
		entity.setPaymentTypeCode(nameToCode((String) object.get("")));
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
		try { return UsagePerAgeEntityDao.getInstance().getAll(); } catch (Exception e){ return entities; }
	}

	@Override
	 protected boolean checkCondition(UsagePerAge entity) {
		/*// 해당 메소드를 사용하려면 Entity Class에 Equals && HashCode 메소드를 재구현 해주세요
		for (int i = 0; i < entities.size(); i++) {
			if(entities.get(i).equals(entity)){
				return false;
			}
		}*/
		return true;
	}

	@Override
	protected int insert(UsagePerAge entity){
		return UsagePerAgeEntityDao.getInstance().insert(entity);
	}

	@Override
	protected String makeExceptionsTextFileName() {
		return "UsagePerAgeJsonLoadExceptions";
	}


}