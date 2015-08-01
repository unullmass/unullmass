package me.ullas.manu.java.stuff;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
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
	static int primesFoundLimit = 100;
	
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
			if(ThreadedPrimes.primesSoFar.contains(i))
				continue;
			int isPrime = -1;
			synchronized(ThreadedPrimes.primesSoFar){
			for(BigInteger j : ThreadedPrimes.primesSoFar.headSet(i.divide(new BigInteger("2")))){
				if(i.mod(j).equals(BigInteger.ZERO)){
					isPrime = 0;
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
}