package load.txtLoad;
import entity.UsageByCard;
import dao.UsageByCardDao;
import helper.runnable.ParallelInsert;
import java.util.ArrayList;
import lombok.SneakyThrows;
import load.base.TxtLoad;
import java.io.BufferedReader;

public class UsageByCardTxtLoad extends TxtLoad<UsageByCard> {
private String characterSet;

	//region singleton
	private UsageByCardTxtLoad() {
	}
	
	private static UsageByCardTxtLoad _instance;
	
	public static UsageByCardTxtLoad getInstance() {
		if (_instance == null)
			_instance = new UsageByCardTxtLoad();
	
		return _instance;
	}
	//endregion

	@Override
	protected UsageByCard setEntity(String[] array) {

		UsageByCard usageByCard = new UsageByCard();
		//todo array와 매칭 시키세요~~
		usageByCard.setLocationCode(nameToCode(array[1]));
		usageByCard.setDate(array[2]);
		usageByCard.setCharge(stringToInt(array[3]));
		usageByCard.setSpend(stringToInt(array[4]));

		return usageByCard;
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
		try { return UsageByCardDao.getInstance().getAll(); } catch (Exception e){ return entities; }
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
		return UsageByCardDao.getInstance().insert(entity);
	}

	@Override
	protected String makeExceptionsTextFileName() {
		return "UsageByCardTxtLoadExceptions";
	}


}