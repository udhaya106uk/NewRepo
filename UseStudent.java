package com.Onesoft.functioncall;
public class UseStudent {
	public static void main(String[] args) {
		Student student=new Student();
		student.displayMarks(100, 98, 95);
		student.findTotal(100, 98, 95);
	}
	
}


class Student {
	public void  displayMarks(int tamil,int english,int maths) {
		
		System.out.println("Tamil Mark "+tamil+" : English Mark "+english+" : Maths Mark "+maths);
	}
	
	public void findTotal(int tamil, int english, int maths) {
		System.out.println("Total Mark is "+(tamil+english+maths));
	}

}
