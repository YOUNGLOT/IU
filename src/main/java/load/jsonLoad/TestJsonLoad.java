package load.jsonLoad;
import entity.Test;
import dao.TestDao;
import helper.runnable.ParallelInsert;
import load.base.JsonLoad;;
import org.json.simple.JSONArray;import org.json.simple.JSONObject;
import java.util.ArrayList;
import lombok.SneakyThrows;

public class TestJsonLoad extends JsonLoad<Test> {

	//region singleton
	private TestJsonLoad() {
	}
	
	private static TestJsonLoad _instance;
	
	public static TestJsonLoad getInstance() {
		if (_instance == null)
			_instance = new TestJsonLoad();
	
		return _instance;
	}
	//endregion

	@Override
	protected Test setEntity(JSONObject object) {
		Test entity = new Test();

		entity.setTestName((String) object.get(""));
		entity.setTestpk1(stringToInt((String) object.get("")));
		entity.setTestpk2(stringToInt((String) object.get("")));

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
		try { return TestDao.getInstance().getAll(); } catch (Exception e){ return entities; }
	}

	@Override
	 protected boolean checkCondition(Test entity) {
		/*// 해당 메소드를 사용하려면 Entity Class에 Equals && HashCode 메소드를 재구현 해주세요
		for (int i = 0; i < entities.size(); i++) {
			if(entities.get(i).equals(entity)){
				return false;
			}
		}*/
		return true;
	}

	@Override
	protected int insert(Test entity){
		return TestDao.getInstance().insert(entity);
	}

	@Override
	protected String makeExceptionsTextFileName() {
		return "TestJsonLoadExceptions";
	}


}