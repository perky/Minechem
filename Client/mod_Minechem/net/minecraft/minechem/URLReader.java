package net.minecraft.minechem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import net.minecraft.src.mod_Minechem;

public class URLReader {
	
	private static String githubChemicalDictionary = "https://raw.github.com/perky/Minechem/master/Client/minechem/Chemical%20Dictionary.txt";
	
	
	public URLReader() {
		
	}
	
	public void getChemicalDictionary() {
		String chemicalDictionary = null;
		
		try {
			chemicalDictionary = readChemicalDictionaryURL();
		} catch(Exception e) {
			System.out.println("Could not read Chemical Dictionary URL");
			e.printStackTrace();
		}
		
		if(chemicalDictionary != null) {
			try {
				writeStringToChemicalDictionary(chemicalDictionary);
			} catch(Exception e) {
				System.out.println("Could not write to Chemical Dictionary.txt");
				e.printStackTrace();
			}
		}
	}
	
	private void writeStringToChemicalDictionary(String str) throws Exception {
		FileWriter fileWriter = new FileWriter(mod_Minechem.fileChemicalDictionary);
		PrintWriter printOut = new PrintWriter(fileWriter);
		printOut.print(str);
		printOut.close();
	}
	
	private String readChemicalDictionaryURL() throws Exception {
		URL url = new URL(githubChemicalDictionary);
		InputStreamReader inStream = new InputStreamReader( url.openStream() );
		BufferedReader in = new BufferedReader( inStream );
		
		String inputLine;
		String stringBuilder = "";
		while((inputLine = in.readLine()) != null) {
			stringBuilder += inputLine + "\n";
			System.out.println(inputLine);
		}
		
		in.close();
		
		return stringBuilder;
	}

}
