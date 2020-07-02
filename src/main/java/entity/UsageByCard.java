package entity;

import lombok.Data;

@Data
public class UsageByCard{
	private int usageByCardId;
	private int locationCode;
	private String date;
	private int charge;
	private int spend;

}