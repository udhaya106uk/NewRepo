package com.Onesoft.functioncall;

public class UseBike {
	public static void main(String[] args) {
	Bike bike=new Bike();
	System.out.println(bike.findNetPrice());
	}

}


class Bike {
	String brand="Honda";
	int price=150000;
	String color="black";
	int taxPercentage=18;
	int taxAmount=(price*taxPercentage)/100;
public int findNetPrice() {
	return price+taxAmount;
}	
}
