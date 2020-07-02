package dao.base;

import java.sql.PreparedStatement;

public interface ParameterSetter {
    void setValue(PreparedStatement statement);
}
