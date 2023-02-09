package com.Onesoft.functioncall;

public class UseAtm {
	public static void main(String[] args) {
		Atm atm = new Atm();
		atm.bankName = "IOB";
		atm.mainBalance = 89999;
		atm.depositeAmount = 50000;
		System.out.print("Bank Name is " + atm.bankName +": My Total Balance is "
				+ atm.findcredit(atm.mainBalance, atm.depositeAmount) + ": Debit amount is "
				+ atm.findDebit(atm.mainBalance, atm.depositeAmount));

	}

}

class Atm {
	String bankName;
	int mainBalance;
	int depositeAmount;

	public int findcredit(int a, int b) {
		return a + b;
	}

	public int findDebit(int a, int b) {
		return a - b;
	}
}
