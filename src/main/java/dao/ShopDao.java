package dao;
import entity.Shop;
import dao.base.ParameterSetter;
import dao.base.EntityDao;
import lombok.SneakyThrows;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ShopDao extends EntityDao<Shop> {
	//region singleton
	private ShopDao() {
	}
	
	private static ShopDao _instance;
	
	public static ShopDao getInstance() {
		if (_instance == null)
			_instance = new ShopDao();
	
		return _instance;
	}
	//endregion

	@Override
	protected String setTableName() {
		return "Shop";
	}

	@SneakyThrows
	@Override
	protected Shop readEntity(ResultSet result) {
		Shop entity = new Shop();

		entity.setShopId(result.getInt(1));
		entity.setName(result.getString(2));
		entity.setBusinessCode(result.getInt(3));
		entity.setAddress(result.getString(4));
		entity.setStreetNameAddress(result.getString(5));
		entity.setZipcode(result.getInt(6));
		entity.setLatitude(result.getString(7));
		entity.setLongitude(result.getString(8));
		entity.setLocationCode(result.getInt(9));

	return entity;
	}

	@Override
	public int update(Shop entity) {

		String query = updateQuery();

		return execute(query, new ParameterSetter() {

			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {

				statement.setString(1, entity.getName());
				statement.setInt(2, entity.getBusinessCode());
				statement.setString(3, entity.getAddress());
				statement.setString(4, entity.getStreetNameAddress());
				statement.setInt(5, entity.getZipcode());
				statement.setString(6, entity.getLatitude());
				statement.setString(7, entity.getLongitude());
				statement.setInt(8, entity.getLocationCode());
				statement.setInt(9, entity.getShopId());
			}
		});
	}

	public int insert(Shop entity) {

		String query = insertQuery();

		return getInt(query, new ParameterSetter() {

			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {

				statement.setString(1, entity.getName());
				statement.setInt(2, entity.getBusinessCode());
				statement.setString(3, entity.getAddress());
				statement.setString(4, entity.getStreetNameAddress());
				statement.setInt(5, entity.getZipcode());
				statement.setString(6, entity.getLatitude());
				statement.setString(7, entity.getLongitude());
				statement.setInt(8, entity.getLocationCode());
			}
		});
	}

	public Shop getByKey(int shopId) {

		String query = getByKeyQuery();

		return getOne(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, shopId);
			}
		 });
	}

	public int deleteByKey(int shopId) {

		String query = deleteByKeyQuery();

		return execute(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, shopId);
			}
		 });
	}


}