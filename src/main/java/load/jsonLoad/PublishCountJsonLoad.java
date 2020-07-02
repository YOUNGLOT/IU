package load.jsonLoad;
import entity.PublishCount;
import dao.PublishCountDao;
import helper.runnable.ParallelInsert;
import load.base.JsonLoad;;
import org.json.simple.JSONArray;import org.json.simple.JSONObject;
import java.util.ArrayList;
import lombok.SneakyThrows;

public class PublishCountJsonLoad extends JsonLoad<PublishCount> {

	//region singleton
	private PublishCountJsonLoad() {
	}
	
	private static PublishCountJsonLoad _instance;
	
	public static PublishCountJsonLoad getInstance() {
		if (_instance == null)
			_instance = new PublishCountJsonLoad();
	
		return _instance;
	}
	//endregion

	@Override
	protected PublishCount setEntity(JSONObject object) {
		PublishCount entity = new PublishCount();

		entity.setDate((String) object.get(""));
		entity.setLocationCode(nameToCode((String) object.get("")));
		entity.setCardCount(stringToInt((String) object.get("")));
		entity.setMobileCount(stringToInt((String) object.get("")));

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
		try { return PublishCountDao.getInstance().getAll(); } catch (Exception e){ return entities; }
	}

	@Override
	 protected boolean checkCondition(PublishCount entity) {
		/*// 해당 메소드를 사용하려면 Entity Class에 Equals && HashCode 메소드를 재구현 해주세요
		for (int i = 0; i < entities.size(); i++) {
			if(entities.get(i).equals(entity)){
				return false;
			}
		}*/
		return true;
	}

	@Override
	protected int insert(PublishCount entity){
		return PublishCountDao.getInstance().insert(entity);
	}

	@Override
	protected String makeExceptionsTextFileName() {
		return "PublishCountJsonLoadExceptions";
	}


}