package dao;
import dao.base.EntityDao;
import entity.UsageByMobile;
import dao.base.ParameterSetter;

import lombok.SneakyThrows;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsageByMobileEntityDao extends EntityDao<UsageByMobile> {
	//region singleton
	private UsageByMobileEntityDao() {
	}
	
	private static UsageByMobileEntityDao _instance;
	
	public static UsageByMobileEntityDao getInstance() {
		if (_instance == null)
			_instance = new UsageByMobileEntityDao();
	
		return _instance;
	}
	//endregion

	@Override
	protected String setTableName() {
		return "UsageByMobile";
	}

	@SneakyThrows
	@Override
	protected UsageByMobile readEntity(ResultSet result) {
		UsageByMobile entity = new UsageByMobile();

		entity.setUsageByMobileId(result.getInt(1));
		entity.setLocationCode(result.getInt(2));
		entity.setDate(result.getString(3));
		entity.setCharge(result.getInt(4));
		entity.setSpend(result.getInt(5));

	return entity;
	}

	@Override
	public int update(UsageByMobile entity) {

		String query = updateQuery();

		return execute(query, new ParameterSetter() {

			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {

				statement.setInt(1, entity.getLocationCode());
				statement.setString(2, entity.getDate());
				statement.setInt(3, entity.getCharge());
				statement.setInt(4, entity.getSpend());
				statement.setInt(5, entity.getUsageByMobileId());
			}
		});
	}

	public int insert(UsageByMobile entity) {

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

	public UsageByMobile getByKey(int usageByMobileId) {

		String query = getByKeyQuery();

		return getOne(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, usageByMobileId);
			}
		 });
	}

	public int deleteByKey(int usageByMobileId) {

		String query = deleteByKeyQuery();

		return execute(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, usageByMobileId);
			}
		 });
	}


}