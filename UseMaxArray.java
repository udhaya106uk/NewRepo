package com.Onesoft.functioncall;

public class UseMaxArray {
	public static void main(String[] args) {
		MaxArray maxi=new MaxArray();
		int[]numbers= {55,85,49,75,86,48};
		maxi.findmax(numbers);
	}

}






class MaxArray {
	public void findmax(int[] a) {
		int max=0;
		for(int i=0; i<a.length; i++) {
			if(a[i]>=max) {
				max=a[i];
			}
			}
		System.out.println(max);
	}
}