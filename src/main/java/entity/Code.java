package entity;

import lombok.Data;

@Data
public class Code{
	private int codeId;
	private String codeName;
	private int codeCategoryId;

	public boolean equals(Code entity){	return (this.codeName.equals(entity.getCodeName())&&this.codeCategoryId == entity.getCodeCategoryId())? true : false; }

	@Override
	public int hashCode(){
		return 31 + this.getCodeName().hashCode() + getCodeCategoryId();
	}
}