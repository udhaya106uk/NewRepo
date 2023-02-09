package com.Onesoft.functioncall;

public class UseMarks {
	public static void main(String[] args) {
	Marks mark=new Marks();
	mark.findTotal(95, 89, 99, 78, 95);
	mark.findAverage(95, 89, 99, 78, 95);
	}

}




class Marks {
	public void findTotal(int tam, int eng, int mat, int sci, int soc) {
		System.out.println("Total is "+(tam+eng+mat+sci+soc));
	}
	public void findAverage(int tam, int eng, int mat, int sci, int soc) {
		System.out.println("Average Mark is "+(tam+eng+mat+sci+soc)/5);
	}
}