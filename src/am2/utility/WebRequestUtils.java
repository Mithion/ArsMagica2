package am2.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class WebRequestUtils {
	private static final String charset = "UTF-8";
	private static final String USER_AGENT = "Mozilla/5.0";

	public static String sendPost(String webURL, HashMap<String, String> postOptions) throws Exception {
		URL obj = new URL(webURL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "";
		for (String s : postOptions.keySet()){
			urlParameters += String.format("%s=%s&", s, URLEncoder.encode(postOptions.get(s), charset));
		}
		if (urlParameters.contains("&"))
			urlParameters = urlParameters.substring(0, urlParameters.lastIndexOf('&'));

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'POST' request to URL : " + webURL);
		//System.out.println("Post parameters : " + urlParameters);
		//System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine + "\r\n");
		}
		in.close();

		//print result
		return response.toString();

	}
}
