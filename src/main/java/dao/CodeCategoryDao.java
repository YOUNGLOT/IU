package dao;
import entity.CodeCategory;
import dao.base.ParameterSetter;
import dao.base.EntityDao;
import lombok.SneakyThrows;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CodeCategoryDao extends EntityDao<CodeCategory> {
	//region singleton
	private CodeCategoryDao() {
	}
	
	private static CodeCategoryDao _instance;
	
	public static CodeCategoryDao getInstance() {
		if (_instance == null)
			_instance = new CodeCategoryDao();
	
		return _instance;
	}
	//endregion

	@Override
	protected String setTableName() {
		return "CodeCategory";
	}

	@SneakyThrows
	@Override
	protected CodeCategory readEntity(ResultSet result) {
		CodeCategory entity = new CodeCategory();

		entity.setCodeCategoryId(result.getInt(1));
		entity.setCodeCategoryName(result.getString(2));

	return entity;
}

	@Override
	public int update(CodeCategory entity) {

		String query = updateQuery();

		return execute(query, new ParameterSetter() {

			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {

				statement.setString(1, entity.getCodeCategoryName());
				statement.setInt(2, entity.getCodeCategoryId());
			}
		});
	}

	public int insert(CodeCategory entity) {

		String query = insertQuery();

		return execute(query, new ParameterSetter() {

			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {

				statement.setInt(1, entity.getCodeCategoryId());
				statement.setString(2, entity.getCodeCategoryName());
			}
		});
	}

	@SneakyThrows
	protected CodeCategory getByKey(int codeCategoryId) {

		String query = getByKeyQuery();

		return getOne(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, codeCategoryId);
			}
		 });
	}

	@SneakyThrows
	protected int deleteByKey(int codeCategoryId) {

		String query = deleteByKeyQuery();

		return execute(query, new ParameterSetter() {
			@SneakyThrows
			@Override
			public void setValue(PreparedStatement statement) {
				statement.setInt(1, codeCategoryId);
			}
		 });
	}


}