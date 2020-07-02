package entity;

import lombok.Data;

@Data
public class PublishCount{
	private int publishCountId;
	private String date;
	private int locationCode;
	private int cardCount;
	private int mobileCount;

}