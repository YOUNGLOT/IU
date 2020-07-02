package entity;

import lombok.Data;

@Data
public class UsageByMobile{
	private int usageByMobileId;
	private int locationCode;
	private String date;
	private int charge;
	private int spend;

}