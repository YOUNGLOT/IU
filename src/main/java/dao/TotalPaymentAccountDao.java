package dao;
import entity.TotalPaymentAccount;
import dao.base.ParameterSetter;
import dao.base.EntityDao;
import lombok.SneakyThrows;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TotalPaymentAccountDao extends EntityDao<TotalPaymentAccount> {
	//region singleton
	private TotalPaymentAccountDao() {
	}
	
	private static TotalPaymentAccountDao _instance;
	
	public static TotalPaymentAccountDao getInstance() {
		if (_instance == null)
			_instance = new TotalPaymentAccountDao();
	
		return _instance;
	}
	//endregion

	@Override
	protected String setTableName() {
		return "TotalPaymentAccount";
	}

	@SneakyThrows
	@Override
	protected TotalPaymentAccount readEntity(ResultSet result) {
		TotalPaymentAccount entity = new TotalPaymentAccount();

		entity.setTotalPaymentAccountId(result.getInt(1));
		entity.setDate(result.getString(2));
		entity.setGenderCode(result.getInt(3));
		entity.setLocationCode(result.getInt(4));
		entity.setBusinessCode(result.getInt(5));
		entity.setTotalPayment(result.getInt(6));

	return entity;
	}

	@Override
	public int update(TotalPaymentAccount entity) {

		String query = updateQuery();

		return execute(query, new ParameterSetter() {

			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {

				statement.setString(1, entity.getDate());
				statement.setInt(2, entity.getGenderCode());
				statement.setInt(3, entity.getLocationCode());
				statement.setInt(4, entity.getBusinessCode());
				statement.setInt(5, entity.getTotalPayment());
				statement.setInt(6, entity.getTotalPaymentAccountId());
			}
		});
	}

	public int insert(TotalPaymentAccount entity) {

		String query = insertQuery();

		return getInt(query, new ParameterSetter() {

			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {

				statement.setString(1, entity.getDate());
				statement.setInt(2, entity.getGenderCode());
				statement.setInt(3, entity.getLocationCode());
				statement.setInt(4, entity.getBusinessCode());
				statement.setInt(5, entity.getTotalPayment());
			}
		});
	}

	public TotalPaymentAccount getByKey(int totalPaymentAccountId) {

		String query = getByKeyQuery();

		return getOne(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, totalPaymentAccountId);
			}
		 });
	}

	public int deleteByKey(int totalPaymentAccountId) {

		String query = deleteByKeyQuery();

		return execute(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, totalPaymentAccountId);
			}
		 });
	}


}