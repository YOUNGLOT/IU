package entity;

import lombok.Data;

@Data
public class TotalPaymentAccount{
	private int totalPaymentAccountId;
	private String date;
	private int genderCode;
	private int locationCode;
	private int businessCode;
	private int totalPayment;

}