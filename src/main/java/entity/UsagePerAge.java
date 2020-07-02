package entity;

import lombok.Data;

@Data
public class UsagePerAge{
	private int usagePerAgeId;
	private String date;
	private int locationCode;
	private int ageCode;
	private int paymentTypeCode;
	private int spend;

}