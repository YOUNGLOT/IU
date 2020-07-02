package dao;
import entity.PublishCount;
import dao.base.ParameterSetter;
import dao.base.EntityDao;
import lombok.SneakyThrows;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PublishCountDao extends EntityDao<PublishCount> {
	//region singleton
	private PublishCountDao() {
	}
	
	private static PublishCountDao _instance;
	
	public static PublishCountDao getInstance() {
		if (_instance == null)
			_instance = new PublishCountDao();
	
		return _instance;
	}
	//endregion

	@Override
	protected String setTableName() {
		return "PublishCount";
	}

	@SneakyThrows
	@Override
	protected PublishCount readEntity(ResultSet result) {
		PublishCount entity = new PublishCount();

		entity.setPublishCountId(result.getInt(1));
		entity.setDate(result.getString(2));
		entity.setLocationCode(result.getInt(3));
		entity.setCardCount(result.getInt(4));
		entity.setMobileCount(result.getInt(5));

	return entity;
	}

	@Override
	public int update(PublishCount entity) {

		String query = updateQuery();

		return execute(query, new ParameterSetter() {

			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {

				statement.setString(1, entity.getDate());
				statement.setInt(2, entity.getLocationCode());
				statement.setInt(3, entity.getCardCount());
				statement.setInt(4, entity.getMobileCount());
				statement.setInt(5, entity.getPublishCountId());
			}
		});
	}

	public int insert(PublishCount entity) {

		String query = insertQuery();

		return getInt(query, new ParameterSetter() {

			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {

				statement.setString(1, entity.getDate());
				statement.setInt(2, entity.getLocationCode());
				statement.setInt(3, entity.getCardCount());
				statement.setInt(4, entity.getMobileCount());
			}
		});
	}

	public PublishCount getByKey(int publishCountId) {

		String query = getByKeyQuery();

		return getOne(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, publishCountId);
			}
		 });
	}

	public int deleteByKey(int publishCountId) {

		String query = deleteByKeyQuery();

		return execute(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, publishCountId);
			}
		 });
	}


}