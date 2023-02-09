package com.Onesoft.functioncall;

public class UseWeekDays {
	public static void main(String[] args) {
		WeekDays d=new WeekDays();
		d.findDay(5);
	}

}
class WeekDays {
	public void findDay(int day) {
		switch (day) {
		case 1:System.out.println("SUNDAY");break;
		case 2:System.out.println("MONDAY");break;
		case 3:System.out.println("TUESDAY");break;
		case 4:System.out.println("WEDNESDAY");break;
		case 5:System.out.println("THURSDAY");break;
		case 6:System.out.println("FRIDAY");break;
		case 7:System.out.println("SATERDAY");break;
		default:System.out.println("in valid");break;
		}
		
	
}
	
}