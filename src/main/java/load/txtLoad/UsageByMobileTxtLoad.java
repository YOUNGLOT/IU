package load.txtLoad;
import entity.UsageByMobile;
import dao.UsageByMobileEntityDao;
import helper.runnable.ParallelInsert;
import java.util.ArrayList;
import lombok.SneakyThrows;
import load.base.TxtLoad;
import java.io.BufferedReader;

public class UsageByMobileTxtLoad extends TxtLoad<UsageByMobile> {
private String characterSet;

	//region singleton
	private UsageByMobileTxtLoad() {
	}
	
	private static UsageByMobileTxtLoad _instance;
	
	public static UsageByMobileTxtLoad getInstance() {
		if (_instance == null)
			_instance = new UsageByMobileTxtLoad();
	
		return _instance;
	}
	//endregion

	@Override
	protected UsageByMobile setEntity(String[] array) {

		UsageByMobile usageByMobile = new UsageByMobile();
		//todo array와 매칭 시키세요~~
		usageByMobile.setLocationCode(nameToCode(array[1]));
		usageByMobile.setDate(array[2]);
		usageByMobile.setCharge(stringToInt(array[3]));
		usageByMobile.setSpend(stringToInt(array[4]));

		return usageByMobile;
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
		try { return UsageByMobileEntityDao.getInstance().getAll(); } catch (Exception e){ return entities; }
	}

	@Override
	 protected boolean checkCondition(UsageByMobile entity) {
		/*// 해당 메소드를 사용하려면 Entity Class에 Equals && HashCode 메소드를 재구현 해주세요
		for (int i = 0; i < entities.size(); i++) {
			if(entities.get(i).equals(entity)){
				return false;
			}
		}*/
		return true;
	}

	@Override
	protected int insert(UsageByMobile entity){
		return UsageByMobileEntityDao.getInstance().insert(entity);
	}

	@Override
	protected String makeExceptionsTextFileName() {
		return "UsageByMobileTxtLoadExceptions";
	}


}