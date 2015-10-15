package me.ullas.manu.java.stuff;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;
import java.util.logging.Logger;

class ThreadedPrimes extends Thread{
	File primesList;
	BigInteger startingPrime;
	static BigInteger latestPrime;
	static TreeSet<BigInteger> primesSoFar = new TreeSet<BigInteger>();
	static boolean kickedOff = false;
	Logger l = Logger.getLogger(getName());
	int primesFound = 0;
	static int primesFoundLimit = 10000;
	
	public ThreadedPrimes() throws IOException{
		synchronized(ThreadedPrimes.primesSoFar){
			//check if this is the first thread
				startingPrime = primesSoFar.last();
			}
		l.info(this.getName() + ": starting prime is " + startingPrime);
	}
	
	static synchronized BigInteger getLatestPrime(){
		return ThreadedPrimes.primesSoFar.last();
	}
	
	public void run(){
		BigInteger i = getLatestPrime().add(new BigInteger("2"));
		for(; primesFound < primesFoundLimit; i = i.add(new BigInteger("2"))){
			if(ThreadedPrimes.primesSoFar.contains(i) || !quickCheckForPrime(i))
				continue;
			int isPrime = -1;
			synchronized(ThreadedPrimes.primesSoFar){
			for(BigInteger j : ThreadedPrimes.primesSoFar.headSet(i.divide(new BigInteger("2")))){
				if(i.mod(j).equals(BigInteger.ZERO)){
					l.info(this.getName() + " : " + i + " has a prime factor " + j);
					isPrime = 0;
					break;
				}
			}
			
			
			
				if(isPrime == -1 && !ThreadedPrimes.primesSoFar.contains(i)){
					ThreadedPrimes.primesSoFar.add(i);
					l.info(this.getName() + " : " + i + " is a PRIME!");
					primesFound++;
				}
			}
		}
		
		l.info(this.getName() + " found " + ThreadedPrimes.primesFoundLimit + " primes. Exiting.");
	}
	
	private static boolean quickCheckForPrime(BigInteger p){
		
		//if sum of all digits adds up to a multiple of 3
		if(getSumOfDigits(p).mod(new BigInteger("3")).equals(BigInteger.ZERO)){
			return false;
		}
		
		//if last digit is 0, 5 - check for div by 5
		if(p.mod(BigInteger.TEN).equals(new BigInteger("5")) || p.mod(BigInteger.TEN).equals(BigInteger.ZERO)){
			return false;
		}
		
		//check for 7 - as per https://www.math.hmc.edu/funfacts/ffiles/10005.5.shtml
		BigInteger[] checkDigitsFor7 = {BigInteger.ONE, new BigInteger("3"), new BigInteger("2"), new BigInteger("6"), new BigInteger("4"), new BigInteger("5")};
		
		ArrayList<BigInteger> reversedDigits = getDigits(p);
		//Collections.reverse(reversedDigits);
		int i = 0;
		BigInteger sumOfDigitsFor7 = BigInteger.ZERO;
		for(BigInteger digitFor7 : reversedDigits){
			sumOfDigitsFor7 = sumOfDigitsFor7.add(digitFor7.multiply(checkDigitsFor7[i%6]));
			i++;
		}
		if(sumOfDigitsFor7.mod(new BigInteger("7")).equals(0)){
			return false;
		}
		
		//alternating sum for 11
		//if sum of all digits adds up to a multiple of 3
		if(getAlternatingSumOfDigits(p).mod(new BigInteger("11")).equals(BigInteger.ZERO)){
			return false;
		}
		
		return true;
	}

	private static BigInteger getSumOfDigits(BigInteger p) {
		BigInteger sumOfDigits = BigInteger.ZERO;
		ArrayList<BigInteger> reversedDigits = getDigits(p);
		for(BigInteger digit : reversedDigits){
			sumOfDigits = sumOfDigits.add(sumOfDigits).add(digit);
		}
		return sumOfDigits;
	}
	
	private static ArrayList<BigInteger> getDigits(BigInteger p) {
		ArrayList<BigInteger> digitList = new ArrayList<BigInteger>();
		do{
			digitList.add(p.mod(BigInteger.TEN));
			p = p.divide(BigInteger.TEN);
		}while(p.compareTo(BigInteger.ZERO) > 1);
		return digitList;
	}
	
	private static BigInteger getAlternatingSumOfDigits(BigInteger p) {
		BigInteger sumOfDigits = BigInteger.ZERO;
		BigInteger addSwitch = BigInteger.ONE;
		ArrayList<BigInteger> reversedDigits = getDigits(p);
		Collections.reverse(reversedDigits);
		for(BigInteger digit : reversedDigits){
			sumOfDigits = sumOfDigits.add(sumOfDigits).add(addSwitch.multiply(digit));
			//flip the switch
			addSwitch = addSwitch.negate();
		}
		return sumOfDigits;
	}
}