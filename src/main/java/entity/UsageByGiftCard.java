package entity;

import lombok.Data;

@Data
public class UsageByGiftCard{
	private int usageByGiftCardId;
	private int locationCode;
	private String date;
	private int charge;
	private int spend;

}