package dao;
import entity.Code;
import dao.base.ParameterSetter;
import dao.base.EntityDao;
import lombok.SneakyThrows;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CodeDao extends EntityDao<Code> {
	//region singleton
	private CodeDao() {
	}
	
	private static CodeDao _instance;
	
	public static CodeDao getInstance() {
		if (_instance == null)
			_instance = new CodeDao();
	
		return _instance;
	}
	//endregion

	@Override
	protected String setTableName() {
		return "Code";
	}

	@SneakyThrows
	@Override
	protected Code readEntity(ResultSet result) {
		Code entity = new Code();

		entity.setCodeId(result.getInt(1));
		entity.setCodeName(result.getString(2));
		entity.setCodeCategoryId(result.getInt(3));

	return entity;
}

	@Override
	public int update(Code entity) {

		String query = updateQuery();

		return execute(query, new ParameterSetter() {

			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {

				statement.setString(1, entity.getCodeName());
				statement.setInt(2, entity.getCodeCategoryId());
				statement.setInt(3, entity.getCodeId());
			}
		});
	}

	public int insert(Code entity) {

		String query = insertQuery();

		return execute(query, new ParameterSetter() {

			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {

				statement.setInt(1, entity.getCodeId());
				statement.setString(2, entity.getCodeName());
				statement.setInt(3, entity.getCodeCategoryId());
			}
		});
	}

	protected Code getByKey(int codeId) {

		String query = getByKeyQuery();

		return getOne(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, codeId);
			}
		 });
	}

	protected int deleteByKey(int codeId) {

		String query = deleteByKeyQuery();

		return execute(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, codeId);
			}
		 });
	}


}