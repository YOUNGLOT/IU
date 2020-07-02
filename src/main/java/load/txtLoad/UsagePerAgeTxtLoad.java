package load.txtLoad;
import entity.UsagePerAge;
import dao.UsagePerAgeDao;
import helper.runnable.ParallelInsert;
import java.util.ArrayList;
import lombok.SneakyThrows;
import load.base.TxtLoad;
import java.io.BufferedReader;

public class UsagePerAgeTxtLoad extends TxtLoad<UsagePerAge> {
private String characterSet;

	//region singleton
	private UsagePerAgeTxtLoad() {
	}
	
	private static UsagePerAgeTxtLoad _instance;
	
	public static UsagePerAgeTxtLoad getInstance() {
		if (_instance == null)
			_instance = new UsagePerAgeTxtLoad();
	
		return _instance;
	}
	//endregion

	@Override
	protected UsagePerAge setEntity(String[] array) {

		UsagePerAge usagePerAge = new UsagePerAge();
		//todo array와 매칭 시키세요~~
		usagePerAge.setDate(array[1]);
		usagePerAge.setLocationCode(nameToCode(array[2]));
		usagePerAge.setAgeCode(nameToCode(array[3]));
		usagePerAge.setPaymentTypeCode(nameToCode(array[4]));
		usagePerAge.setSpend(stringToInt(array[5]));

		return usagePerAge;
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
		try { return UsagePerAgeDao.getInstance().getAll(); } catch (Exception e){ return entities; }
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
		return UsagePerAgeDao.getInstance().insert(entity);
	}

	@Override
	protected String makeExceptionsTextFileName() {
		return "UsagePerAgeTxtLoadExceptions";
	}


}