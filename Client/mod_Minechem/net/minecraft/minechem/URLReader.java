package net.minecraft.minechem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.minecraft.src.mod_Minechem;

public class URLReader {
	
	private static String githubChemicalDictionary = "https://raw.github.com/perky/Minechem/master/Client/minechem/Chemical%20Dictionary.txt";
	private static String githubReleaseVersion = "https://raw.github.com/perky/Minechem/master/Client/mod_Minechem/releaseversion";
	
	public URLReader() {
		
	}
	
	public void getChemicalDictionary() {
		String chemicalDictionary = null;
		
		try {
			chemicalDictionary = readURL(githubChemicalDictionary);
		} catch(Exception e) {
			System.out.println("Could not read Chemical Dictionary URL");
		}
		
		if(chemicalDictionary != null) {
			try {
				writeStringToChemicalDictionary(chemicalDictionary);
			} catch(Exception e) {
				System.out.println("Could not write to Chemical Dictionary.txt");
			}
		}
	}
	
	public String getLatestReleaseVersion() {
		String lastestReleaseVersion = null;
		try {
			lastestReleaseVersion = readURL(githubReleaseVersion);
		} catch(Exception e) {
			System.out.println("Could not read latest release version URL");
		}
		
		return lastestReleaseVersion;
	}
	
	private void writeStringToChemicalDictionary(String str) throws Exception {
		FileWriter fileWriter = new FileWriter(mod_Minechem.fileChemicalDictionary);
		PrintWriter printOut = new PrintWriter(fileWriter);
		printOut.print(str);
		printOut.close();
	}
	
	private String readURL(String urlString) throws Exception {
		URL url = new URL(urlString);
		HttpURLConnection huc = (HttpURLConnection) url.openConnection();
	    HttpURLConnection.setFollowRedirects(false);
	    huc.setConnectTimeout(10 * 1000);
	    huc.setRequestMethod("GET");
	    huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
	    huc.connect();
		
		InputStream inStream = huc.getInputStream();
		InputStreamReader inStreamReader = new InputStreamReader(inStream);
		BufferedReader in = new BufferedReader(inStreamReader);
		
		String inputLine;
		String stringBuilder = "";
		while((inputLine = in.readLine()) != null) {
			stringBuilder += inputLine + "\n";
		}
		
		in.close();
		
		return stringBuilder;
	}

}
