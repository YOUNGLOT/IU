package dao;
import entity.UsagePerAge;
import dao.base.ParameterSetter;
import dao.base.EntityDao;
import lombok.SneakyThrows;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsagePerAgeDao extends EntityDao<UsagePerAge> {
	//region singleton
	private UsagePerAgeDao() {
	}
	
	private static UsagePerAgeDao _instance;
	
	public static UsagePerAgeDao getInstance() {
		if (_instance == null)
			_instance = new UsagePerAgeDao();
	
		return _instance;
	}
	//endregion

	@Override
	protected String setTableName() {
		return "UsagePerAge";
	}

	@SneakyThrows
	@Override
	protected UsagePerAge readEntity(ResultSet result) {
		UsagePerAge entity = new UsagePerAge();

		entity.setUsagePerAgeId(result.getInt(1));
		entity.setDate(result.getString(2));
		entity.setLocationCode(result.getInt(3));
		entity.setAgeCode(result.getInt(4));
		entity.setPaymentTypeCode(result.getInt(5));
		entity.setSpend(result.getInt(6));

	return entity;
	}

	@Override
	public int update(UsagePerAge entity) {

		String query = updateQuery();

		return execute(query, new ParameterSetter() {

			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {

				statement.setString(1, entity.getDate());
				statement.setInt(2, entity.getLocationCode());
				statement.setInt(3, entity.getAgeCode());
				statement.setInt(4, entity.getPaymentTypeCode());
				statement.setInt(5, entity.getSpend());
				statement.setInt(6, entity.getUsagePerAgeId());
			}
		});
	}

	public int insert(UsagePerAge entity) {

		String query = insertQuery();

		return getInt(query, new ParameterSetter() {

			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {

				statement.setString(1, entity.getDate());
				statement.setInt(2, entity.getLocationCode());
				statement.setInt(3, entity.getAgeCode());
				statement.setInt(4, entity.getPaymentTypeCode());
				statement.setInt(5, entity.getSpend());
			}
		});
	}

	public UsagePerAge getByKey(int usagePerAgeId) {

		String query = getByKeyQuery();

		return getOne(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, usagePerAgeId);
			}
		 });
	}

	public int deleteByKey(int usagePerAgeId) {

		String query = deleteByKeyQuery();

		return execute(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, usagePerAgeId);
			}
		 });
	}


}