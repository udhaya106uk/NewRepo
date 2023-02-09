package com.Onesoft.functioncall;

public class UseFindMaximum {
	public static void main(String[] args) {
		FindMaximam max=new FindMaximam();
		max.findMax(85, 60, 98);
				}

}

class FindMaximam {
	public void findMax(int a, int b, int c) {
		if(a>b && a>c) {
			System.out.println(a+" IS MAXIMUM NUMBER");
		}
		else if(b>a && b>c) {
			System.out.println(b+" IS MAXIMUM NUMBER");
		}
		else {
			System.out.println(c+" IS MAXIMUM NUMBER");
		}
	}
}