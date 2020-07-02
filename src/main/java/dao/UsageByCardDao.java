package dao;
import entity.UsageByCard;
import dao.base.ParameterSetter;
import dao.base.EntityDao;
import lombok.SneakyThrows;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsageByCardDao extends EntityDao<UsageByCard> {
	//region singleton
	private UsageByCardDao() {
	}
	
	private static UsageByCardDao _instance;
	
	public static UsageByCardDao getInstance() {
		if (_instance == null)
			_instance = new UsageByCardDao();
	
		return _instance;
	}
	//endregion

	@Override
	protected String setTableName() {
		return "UsageByCard";
	}

	@SneakyThrows
	@Override
	protected UsageByCard readEntity(ResultSet result) {
		UsageByCard entity = new UsageByCard();

		entity.setUsageByCardId(result.getInt(1));
		entity.setLocationCode(result.getInt(2));
		entity.setDate(result.getString(3));
		entity.setCharge(result.getInt(4));
		entity.setSpend(result.getInt(5));

	return entity;
	}

	@Override
	public int update(UsageByCard entity) {

		String query = updateQuery();

		return execute(query, new ParameterSetter() {

			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {

				statement.setInt(1, entity.getLocationCode());
				statement.setString(2, entity.getDate());
				statement.setInt(3, entity.getCharge());
				statement.setInt(4, entity.getSpend());
				statement.setInt(5, entity.getUsageByCardId());
			}
		});
	}

	public int insert(UsageByCard entity) {

		String query = insertQuery();

		return getInt(query, new ParameterSetter() {

			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {

				statement.setInt(1, entity.getLocationCode());
				statement.setString(2, entity.getDate());
				statement.setInt(3, entity.getCharge());
				statement.setInt(4, entity.getSpend());
			}
		});
	}

	public UsageByCard getByKey(int usageByCardId) {

		String query = getByKeyQuery();

		return getOne(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, usageByCardId);
			}
		 });
	}

	public int deleteByKey(int usageByCardId) {

		String query = deleteByKeyQuery();

		return execute(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, usageByCardId);
			}
		 });
	}


}