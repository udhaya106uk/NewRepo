package com.Onesoft.functioncall;

public class UseCalculator {
	public static void main(String[] args) {
		Calculator cals=new Calculator();
		System.out.println(cals.add(5, 5, 5));
		System.out.println(cals.sub());
		cals.multi(5, 5);
		cals.div();
		}
	}
