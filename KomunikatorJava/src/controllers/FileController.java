package controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class FileController {
	
	public static void saveToFile(String msg)
	{
		FileWriter file;
		try {
			file = new FileWriter("history.txt", true);
			BufferedWriter out = new BufferedWriter(file);
			out.write(msg);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static String readFile()
	{
		File file = new File("history.txt");
	      Scanner in;
	      String msg="";
		try {
			in = new Scanner(file);
		      while(in.hasNext())
		    	  msg += in.nextLine()+"\n";
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  
	


		return msg;
	}
	
	public static void cleanHistory()
	{
		PrintWriter pw;
		try {
			pw = new PrintWriter("history.txt");
			pw.println("");
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
