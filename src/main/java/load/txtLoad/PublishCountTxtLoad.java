package load.txtLoad;
import entity.PublishCount;
import dao.PublishCountDao;
import helper.runnable.ParallelInsert;
import java.util.ArrayList;
import lombok.SneakyThrows;
import load.base.TxtLoad;
import java.io.BufferedReader;

public class PublishCountTxtLoad extends TxtLoad<PublishCount> {
private String characterSet;

	//region singleton
	private PublishCountTxtLoad() {
	}
	
	private static PublishCountTxtLoad _instance;
	
	public static PublishCountTxtLoad getInstance() {
		if (_instance == null)
			_instance = new PublishCountTxtLoad();
	
		return _instance;
	}
	//endregion

	@Override
	protected PublishCount setEntity(String[] array) {

		PublishCount publishCount = new PublishCount();
		//todo array와 매칭 시키세요~~
		publishCount.setDate(array[1]);
		publishCount.setLocationCode(nameToCode(array[2]));
		publishCount.setCardCount(stringToInt(array[3]));
		publishCount.setMobileCount(stringToInt(array[4]));

		return publishCount;
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
		return "PublishCountTxtLoadExceptions";
	}


}