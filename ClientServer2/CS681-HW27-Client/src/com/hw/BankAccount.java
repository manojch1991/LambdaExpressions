package com.hw;

import java.util.concurrent.locks.ReentrantLock;

public class BankAccount{
	private double balance = 0;
	private ReentrantLock lock = new ReentrantLock();
	
	public double getBalance(){
		lock.lock();
		try{
			return balance;
		}finally{
			lock.unlock();
		}
		
	}
	
	public double deposit(double amount){
		lock.lock();
		try{
			balance += amount;
			return balance;
		}finally{
			lock.unlock();
		}
	}
	
	public double withdraw(double amount){
		lock.lock();
		try{
			balance -= amount;
			return balance;
		}finally{
			lock.unlock();
		}
	}
	
}
