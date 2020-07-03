package load.txtLoad;
import entity.Test;
import dao.TestEntityDao;
import helper.runnable.ParallelInsert;
import java.util.ArrayList;
import lombok.SneakyThrows;
import load.base.TxtLoad;

import java.io.BufferedReader;

public class TestTxtLoad extends TxtLoad<Test> {
private String characterSet;

	//region singleton
	private TestTxtLoad() {
	}
	
	private static TestTxtLoad _instance;
	
	public static TestTxtLoad getInstance() {
		if (_instance == null)
			_instance = new TestTxtLoad();
	
		return _instance;
	}
	//endregion

	@Override
	protected Test setEntity(String[] array) {

		Test test = new Test();
		//todo array와 매칭 시키세요~~
		test.setTestName(array[1]);
		test.setTestpk1(stringToInt(array[2]));
		test.setTestpk2(stringToInt(array[3]));

		return test;
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
		try { return TestEntityDao.getInstance().getAll(); } catch (Exception e){ return entities; }
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
		return TestEntityDao.getInstance().insert(entity);
	}

	@Override
	protected String makeExceptionsTextFileName() {
		return "TestTxtLoadExceptions";
	}


}