package me.ullas.manu.java.stuff;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.logging.Logger;


public class ThreadedPrimesRunner {

	public static void main(String[] args) throws IOException, InterruptedException {
		Logger l = Logger.getLogger(Thread.currentThread().getName());
		try{
			if(!new File(args[1]).exists()){
				throw new FileNotFoundException("Prime file " + args[1] + " does not exist. Creating default.");
				
			}
		}catch(FileNotFoundException primeFileDoesNotExist){
			l.info("Prime list not found! Creating new list at " + args[1]);
			File primesList = new File(args[1]);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(primesList));
			bw.write("2\n3\n5\7");
			
			bw.close();
		}
		
		
		
		l.info("Initializing prime list.");
		
		Scanner s = new Scanner(new File(args[1]));
		while(s.hasNextBigInteger()){
			ThreadedPrimes.primesSoFar.add(s.nextBigInteger());
			s.nextLine();
		}
		s.close();
		
		try{
			ThreadedPrimes.primesFoundLimit = Integer.parseInt(args[2]);
		}catch(Exception e){
			ThreadedPrimes.primesFoundLimit = 100;
		}
		
		ThreadedPrimes[] t_array =  new ThreadedPrimes[Integer.parseInt(args[0])];
		for(int i = 0; i < Integer.parseInt(args[0]); i++){
			t_array[i] = new ThreadedPrimes();
			t_array[i].setName("PrimeRunner_"+(i+1));
			t_array[i].start();
		}
		
		for(int i = 0; i < Integer.parseInt(args[0]); i++){
			t_array[i].join();
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(args[1]));
		for(BigInteger prime : ThreadedPrimes.primesSoFar){
			bw.write(prime+"\n");
		}
		bw.close();
	}
}
