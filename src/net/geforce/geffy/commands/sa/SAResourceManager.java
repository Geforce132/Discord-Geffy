package net.geforce.geffy.commands.sa;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Scanner;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

import net.geforce.geffy.misc.Passwords;

public class SAResourceManager {
	
	private static final int HTTP_REQUEST_TIMEOUT = 2000;
	private static final String SEARCH_ENGINE_ID = "004958339459507545485:8rbcigrntqo";
	
	public static int SEARCH_QUERIES_REMAINING = 79;
	
	public static String searchSA(String keyword){
	    Customsearch customsearch= null;

	    try {
	        customsearch = new Customsearch(new NetHttpTransport(),new JacksonFactory(), new HttpRequestInitializer() {
	            public void initialize(HttpRequest httpRequest) {
	                try {
	                    // set connect and read timeouts
	                    httpRequest.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
	                    httpRequest.setReadTimeout(HTTP_REQUEST_TIMEOUT);

	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                }
	            }
	        });
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    List<Result> resultList=null;
	    try {
	        Customsearch.Cse.List list=customsearch.cse().list(keyword);
	        list.setKey(Passwords.SA_GOOGLE_API_KEY);
	        list.setCx(SEARCH_ENGINE_ID);
	        Search results=list.execute();
	        resultList=results.getItems();
	    }
	    catch (  Exception e) {
	        e.printStackTrace();
	    }
	    
	    if(resultList != null) {
	    	SEARCH_QUERIES_REMAINING--;
	    	return resultList.get(0).getLink();
	    }
	    
		return null;	 
	}
	
//	public static String getSALink(String[] searchArgs) throws IOException {
//		String searchQuery = "steam analyst ";
//		
//		for(String term : searchArgs) {
//			searchQuery += (term + " ").replaceAll("null", "");
//		}
//		
//		searchQuery = searchQuery.replaceAll(" ", "+");
//		
//		String search = "https://www.google.com/search?q=" + searchQuery + "&num=1";
//		Document doc = Jsoup.connect(search).userAgent("Mozilla/5.0").get();
//
//		Elements results = doc.select("h3.r > a");
//
//		for (Element result : results) {
//			String linkHref = result.attr("href");
//			return linkHref.substring(7, linkHref.indexOf("&"));
//		}
//		
//		return null;
//	}
	
	public static void writeToJson(String url, String fnPrice, String mwPrice, String ftPrice, String wwPrice, String bsPrice, String imgLink) {
		File folder = new File("sa");
		
		if(!folder.exists())
			folder.mkdir();
		
		Scanner scanner = new Scanner(url);
		
		String itemId = url.replaceAll("\\D", "");
		scanner.close();
		
		File itemFile = new File("sa/" + itemId + ".txt");

		try {
			PrintWriter writer = new PrintWriter(itemFile, "UTF-8");

			writer.println("FN: " + fnPrice);
			writer.println("MW: " + mwPrice);
			writer.println("FT: " + ftPrice);
			writer.println("WW: " + wwPrice);
			writer.println("BS: " + bsPrice);
			writer.println("Image: " + imgLink);

			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static String getImageLink(String url) throws FileNotFoundException {
		File folder = new File("sa");
		
		if(!folder.exists())
			return null;
		
		Scanner scanner = new Scanner(url);
		
		String itemId = url.replaceAll("\\D", "");
		scanner.close();
		
		File itemFile = new File("sa/" + itemId + ".txt");
		
		if(!itemFile.exists()) {
			return null;
		}
		else {
			Scanner input = new Scanner(itemFile);

			while (input.hasNextLine()) {
				String line = input.nextLine();

				if(line.startsWith("Image: ")) {
					input.close();
					return line.replace("Image: ", "");
				}
			}
			
			input.close();
		}
		
		return null;
	}
	
	public static Color getEmbedColor(String grade) {
		if(grade.contains("\u2605")) {
			return new Color(255,223,0);
		}
		else if(grade.contains("Contraband")) {
			return new Color(255, 174, 57);
		}
		else if(grade.contains("Covert")) {
			return new Color(235, 75, 75);
		}
		else if(grade.contains("Classified")) {
			return new Color(211, 46, 230);
		}
		else if(grade.contains("Restricted")) {
			return new Color(136, 71, 255);
		}
		else if(grade.contains("Mil-Spec")) {
			return new Color(75, 105, 255);
		}
		else if(grade.contains("Industrial")) {
			return new Color(94, 152, 217);
		}
		else if(grade.contains("Consumer")) {
			return new Color(176, 195, 217);
		}

		
		return new Color(0, 0 ,0);
	}
}
