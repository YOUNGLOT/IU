package load.txtLoad;
import entity.TotalPaymentAccount;
import dao.TotalPaymentAccountEntityDao;
import helper.runnable.ParallelInsert;
import java.util.ArrayList;
import lombok.SneakyThrows;
import load.base.TxtLoad;
import java.io.BufferedReader;

public class TotalPaymentAccountTxtLoad extends TxtLoad<TotalPaymentAccount> {
private String characterSet;

	//region singleton
	private TotalPaymentAccountTxtLoad() {
	}
	
	private static TotalPaymentAccountTxtLoad _instance;
	
	public static TotalPaymentAccountTxtLoad getInstance() {
		if (_instance == null)
			_instance = new TotalPaymentAccountTxtLoad();
	
		return _instance;
	}
	//endregion

	@Override
	protected TotalPaymentAccount setEntity(String[] array) {

		TotalPaymentAccount totalPaymentAccount = new TotalPaymentAccount();
		//todo array와 매칭 시키세요~~
		totalPaymentAccount.setDate(array[1]);
		totalPaymentAccount.setGenderCode(nameToCode(array[2]));
		totalPaymentAccount.setLocationCode(nameToCode(array[3]));
		totalPaymentAccount.setBusinessCode(nameToCode(array[4]));
		totalPaymentAccount.setTotalPayment(stringToInt(array[5]));

		return totalPaymentAccount;
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
		try { return TotalPaymentAccountEntityDao.getInstance().getAll(); } catch (Exception e){ return entities; }
	}

	@Override
	 protected boolean checkCondition(TotalPaymentAccount entity) {
		/*// 해당 메소드를 사용하려면 Entity Class에 Equals && HashCode 메소드를 재구현 해주세요
		for (int i = 0; i < entities.size(); i++) {
			if(entities.get(i).equals(entity)){
				return false;
			}
		}*/
		return true;
	}

	@Override
	protected int insert(TotalPaymentAccount entity){
		return TotalPaymentAccountEntityDao.getInstance().insert(entity);
	}

	@Override
	protected String makeExceptionsTextFileName() {
		return "TotalPaymentAccountTxtLoadExceptions";
	}


}