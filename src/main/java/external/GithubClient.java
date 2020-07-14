package external;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

// Any changes after pom.xml must click Sava button and then Maven install 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import entity.Item;

import java.util.List;
import java.util.ArrayList;


public class GithubClient{
	private static final String URL_TEMPLATE = "https://jobs.github.com/positions/json?description=%s&lat=%s&long=%s";
	private static final String DEFAULT_KEYWORD = "developer";
	
	public void search(double lat, double lon, String keyword){
		if(keyword == null) {
			keyword = DEFAULT_KEYWORD;
		}

		try {
			// URLEncoder is needed for keyword
			keyword = URLEncoder.encode(keyword, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String.format(URL_TEMPLATE, keyword, lat, lon);
		
		CloseableHttpClient httpclient = HttpClients.createDefault(); 
		// HttpClients is the parent of CloseableHttpClient with methods named createDefault
		ResponseHandler<List<Item>> responseHandler = new ResponseHandler<List<Item>>(){
			@Override
			public List<Item> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				if(response.getStatusLine().getStatusCode() != 200 || response.getEntity() == null) {
					return new ArrayList<>();
				}
				HttpEntity entity = response.getEntity();
				String responseBody = EntityUtils.toString(entity);
				JSONArray itemJsonarray = new JSONArray(responseBody);
				List<Item> itemList = new ArrayList<>();
				for(int i = 0; i<itemJsonarray.length(); i++) {
					JSONObject obj = itemJsonarray.getJSONObject(i);
					Item newItem = Item.builder()
							.itemId(getStringFieldOrEmpty(obj, "id"))
							.name(getStringFieldOrEmpty(obj, "title")).
							address(getStringFieldOrEmpty(obj, "location")).
							url(getStringFieldOrEmpty(obj, "url")).
							imageUrl(getStringFieldOrEmpty(obj, "company_logo")).
							build();
					itemList.add(newItem);					
				}
				return itemList;
			}
			
		};
		
		
		

	}
	
	private String getStringFieldOrEmpty(JSONObject obj, String field) {
		return obj.isNull(field)? "" : obj.getString(field);
	}

}
