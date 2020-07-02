package dao;
import entity.Test;
import dao.base.ParameterSetter;
import dao.base.EntityDao;
import lombok.SneakyThrows;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestDao extends EntityDao<Test> {
	//region singleton
	private TestDao() {
	}
	
	private static TestDao _instance;
	
	public static TestDao getInstance() {
		if (_instance == null)
			_instance = new TestDao();
	
		return _instance;
	}
	//endregion

	@Override
	protected String setTableName() {
		return "Test";
	}

	@SneakyThrows
	@Override
	protected Test readEntity(ResultSet result) {
		Test entity = new Test();

		entity.setTestId(result.getInt(1));
		entity.setTestName(result.getString(2));
		entity.setTestpk1(result.getInt(3));
		entity.setTestpk2(result.getInt(4));

	return entity;
	}

	@Override
	public int update(Test entity) {

		String query = updateQuery();

		return execute(query, new ParameterSetter() {

			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {

				statement.setString(1, entity.getTestName());
				statement.setInt(2, entity.getTestpk1());
				statement.setInt(3, entity.getTestpk2());
				statement.setInt(4, entity.getTestId());
			}
		});
	}

	public int insert(Test entity) {

		String query = insertQuery();

		return getInt(query, new ParameterSetter() {

			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {

				statement.setString(1, entity.getTestName());
				statement.setInt(2, entity.getTestpk1());
				statement.setInt(3, entity.getTestpk2());
			}
		});
	}

	public Test getByKey(int testId,int testpk1,int testpk2) {

		String query = getByKeyQuery();

		return getOne(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, testId);
				statement.setInt(2, testpk1);
				statement.setInt(3, testpk2);
			}
		 });
	}

	public int deleteByKey(int testId,int testpk1,int testpk2) {

		String query = deleteByKeyQuery();

		return execute(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, testId);
				statement.setInt(2, testpk1);
				statement.setInt(3, testpk2);
			}
		 });
	}


}