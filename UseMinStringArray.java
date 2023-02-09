package com.Onesoft.functioncall;

public class UseMinStringArray {
	public static void main(String[] args) {
		MinStringArray minar=new MinStringArray();
		String []names= {"raja","ram","vetri"};
		minar.minarray(names);
	}

}
class  MinStringArray {
	public void minarray(String[]a) {
		String min=a[0];
		for(int i=0; i<a.length; i++) {
			if(a[i].length()<=min.length()) {
				min=a[i];				
			}
		}
		System.out.println(min);
	}
}