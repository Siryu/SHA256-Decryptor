package Host;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class WordDownload {
	
	// taken from http://www.mkyong.com/webservices/jax-rs/restfull-java-client-with-java-net-url/
	public static List<String> getTopWords(int count) {
		ArrayList<String> words = new ArrayList<String>();
		try {
			 URL url = new URL("https://code.neumont.edu/words/words/index?start=0&end=" + count);
			 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			 conn.setRequestMethod("GET");
			 conn.setRequestProperty("Accept", "application/json");
	
			 if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			 }
			 BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			 String output;
			 String newOutput = null;
			 while ((output = br.readLine()) != null) {
				 try {
					 System.out.println("finished downloading the words");
					JSONArray array = new JSONArray(output);
					System.out.println("finished converting all json strings to json objects");
					for(int i = 0; i < array.length(); i++) {
						newOutput = array.getJSONObject(i).getString("word");
						 words.add(newOutput);
					}
					System.out.println("finished adding all words into list");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		 conn.disconnect();
		 }
		 catch (MalformedURLException e) {
		     e.printStackTrace();
		 } 
		 catch (IOException e) {
			 e.printStackTrace();
		 }
		return words;
	}
}
