package leg.lion.mensawidget;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class Requester {


	public static final SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat printFormat = new SimpleDateFormat("EEEE, d. MMMM", Locale.GERMAN);



	public static StringBuilder requestWebsite(String url) throws IOException {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
        URL website = new URL(url);
        URLConnection yc = website.openConnection();
		yc.setConnectTimeout(5000);
        StringBuilder resultSb = new StringBuilder();
        try(BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()))){
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                resultSb.append(inputLine);
            }
        } catch(IOException io) {
			io.printStackTrace();
			if(io instanceof UnknownHostException)throw io;
        	return null;
        }
        return resultSb;
	}

	public static String header(){
		String result = "";
		try {
			String dateString = nextDate();
			Date date = parseFormat.parse(dateString);
			result = printFormat.format(date);
		} catch (IOException e) {
			e.printStackTrace();
			result = "Error while connecting to website";
		} catch (ParseException | JSONException e) {
			e.printStackTrace();
			result = "Error while parsing";
		}
		return result;
	}

	public static String fullString(){
		String result = "";
		try {

			for(String meal : meals(nextDate()))result += meal+"\n\n";
			result = result.substring(0,result.length()-1);
		} catch (IOException e) {
			e.printStackTrace();
			result = "Error while connecting to website";
		} catch (JSONException e) {
			e.printStackTrace();
			result = "Error while parsing";
		}
		return result;
	}

	public static String nextDate() throws IOException, JSONException {
		String resp = requestWebsite("https://openmensa.org/api/v2/canteens/62/days").toString();
		return ((JSONObject)new JSONArray(resp).get(0)).get("date").toString();
	}

	public static List<String> meals(String date) throws IOException, JSONException {
		String resp = requestWebsite("https://openmensa.org/api/v2/canteens/62/days/"+date+"/meals").toString();///2018-08-06/meals");
		JSONArray meals = new JSONArray(resp);
		List<String> list = new ArrayList<String>();
		for(int i = 0; i < meals.length(); i++){
			list.add(((JSONObject)meals.get(i)).getString("name").replace("\n",""));
		}
		return list;
	}

}
