package dao;
import entity.UsageByGiftCard;
import dao.base.ParameterSetter;
import dao.base.EntityDao;
import lombok.SneakyThrows;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsageByGiftCardDao extends EntityDao<UsageByGiftCard> {
	//region singleton
	private UsageByGiftCardDao() {
	}
	
	private static UsageByGiftCardDao _instance;
	
	public static UsageByGiftCardDao getInstance() {
		if (_instance == null)
			_instance = new UsageByGiftCardDao();
	
		return _instance;
	}
	//endregion

	@Override
	protected String setTableName() {
		return "UsageByGiftCard";
	}

	@SneakyThrows
	@Override
	protected UsageByGiftCard readEntity(ResultSet result) {
		UsageByGiftCard entity = new UsageByGiftCard();

		entity.setUsageByGiftCardId(result.getInt(1));
		entity.setLocationCode(result.getInt(2));
		entity.setDate(result.getString(3));
		entity.setCharge(result.getInt(4));
		entity.setSpend(result.getInt(5));

	return entity;
	}

	@Override
	public int update(UsageByGiftCard entity) {

		String query = updateQuery();

		return execute(query, new ParameterSetter() {

			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {

				statement.setInt(1, entity.getLocationCode());
				statement.setString(2, entity.getDate());
				statement.setInt(3, entity.getCharge());
				statement.setInt(4, entity.getSpend());
				statement.setInt(5, entity.getUsageByGiftCardId());
			}
		});
	}

	public int insert(UsageByGiftCard entity) {

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

	public UsageByGiftCard getByKey(int usageByGiftCardId) {

		String query = getByKeyQuery();

		return getOne(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, usageByGiftCardId);
			}
		 });
	}

	public int deleteByKey(int usageByGiftCardId) {

		String query = deleteByKeyQuery();

		return execute(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, usageByGiftCardId);
			}
		 });
	}


}