package entity;

import lombok.Data;

@Data
public class Shop{
	private int shopId;
	private String name;
	private int businessCode;
	private String address;
	private String streetNameAddress;
	private int zipcode;
	private String latitude;
	private String longitude;
	private int locationCode;

}