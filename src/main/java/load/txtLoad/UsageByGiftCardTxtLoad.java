package load.txtLoad;
import entity.UsageByGiftCard;
import dao.UsageByGiftCardEntityDao;
import helper.runnable.ParallelInsert;
import java.util.ArrayList;
import lombok.SneakyThrows;
import load.base.TxtLoad;
import java.io.BufferedReader;

public class UsageByGiftCardTxtLoad extends TxtLoad<UsageByGiftCard> {
private String characterSet;

	//region singleton
	private UsageByGiftCardTxtLoad() {
	}
	
	private static UsageByGiftCardTxtLoad _instance;
	
	public static UsageByGiftCardTxtLoad getInstance() {
		if (_instance == null)
			_instance = new UsageByGiftCardTxtLoad();
	
		return _instance;
	}
	//endregion

	@Override
	protected UsageByGiftCard setEntity(String[] array) {

		UsageByGiftCard usageByGiftCard = new UsageByGiftCard();
		//todo array와 매칭 시키세요~~
		usageByGiftCard.setLocationCode(nameToCode(array[1]));
		usageByGiftCard.setDate(array[2]);
		usageByGiftCard.setCharge(stringToInt(array[3]));
		usageByGiftCard.setSpend(stringToInt(array[4]));

		return usageByGiftCard;
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
		try { return UsageByGiftCardEntityDao.getInstance().getAll(); } catch (Exception e){ return entities; }
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
		return UsageByGiftCardEntityDao.getInstance().insert(entity);
	}

	@Override
	protected String makeExceptionsTextFileName() {
		return "UsageByGiftCardTxtLoadExceptions";
	}


}