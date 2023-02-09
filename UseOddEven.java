package com.Onesoft.functioncall;

public class UseOddEven {
	public static void main(String[] args) {
		OddEven oe=new OddEven();
		System.out.println(oe.findOddEven(5));
		
	}

}
class OddEven {
	public String findOddEven(int a) {
		if(a%2==0) {
			return "This is Even Number "+a;
		}
		else {
			return "This is Odd Number "+a;
		}
	}
	
}