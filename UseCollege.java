package com.Onesoft.functioncall;

public class UseCollege {
	public static void main(String[] args) {
		College college1=new College();
		college1.name="AVS";
		college1.fees=45000;
		college1.grade='A';
		college1.location="Hosur";
		
		
		
		College college2=new College();
		college2.name="JET";
		college2.fees=80000;
		college2.grade='B';
		college2.location="SALEM";
		
		
		College college3=new College();
		college3.name="VET";
		college3.fees=150000;
		college3.grade='S';
		college3.location="VELLOR";
		
	College [] clgs= {college1,college2,college3};
		
	System.out.println(college1.findMaxFeesClg(clgs));
	
	}

}
class College {
	String name;
	int fees;
	char grade;
	String location;
	public String findMaxFeesClg(College[]a) {
		int max=0;
		String temp="";
		for(int i=0; i<a.length; i++) {
			if(a[i].fees>max) {
		max=a[i].fees;
		temp=a[i].name;
		}
	}
		return temp;
	}
}
