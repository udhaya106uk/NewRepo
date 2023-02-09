package com.Onesoft.functioncall;

public class UseSquareCube {
	public static void main(String[] args) {
		SquareCube fs=new SquareCube();
		System.out.println(fs.findSquare(5));
		System.out.println(fs.findCube(5));
		
	}
	

}

class SquareCube {
	public String findSquare(int a) {
	return "Square is "+(a*a);
	}
	public String findCube(int a) {
		return "Cube is "+(a*a*a);
	}
}