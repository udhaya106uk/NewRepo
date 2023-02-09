package com.Onesoft.functioncall;

public class UseMobile {
	public static void main(String[] args) {
		Mobile mobile1=new Mobile();
		mobile1.brand="SAMSUNG";
		mobile1.price=85000;
		mobile1.color="BLACK";
		mobile1.isTouchScreen=true;
		
		Mobile mobile2=new Mobile();
		mobile2.brand="VIVO";
		mobile2.price=78000;
		mobile2.color="White";
		mobile2.isTouchScreen=true;
		System.out.println(mobile2.findMaxPriceMobile(mobile1.price, mobile2.price));
		
	}

}
class Mobile {
	String brand;
	int price;
	String color;
	boolean isTouchScreen;
	public String findMaxPriceMobile(int a, int b) {
		if(a>b) {
			return a+" is costly mobile";
	}
		else {
			return b+" is costly mobile";
		}
	}
		}
	