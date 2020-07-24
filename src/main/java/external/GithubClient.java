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
import java.util.HashSet;


public class GithubClient{
	private static final String URL_TEMPLATE = "https://jobs.github.com/positions/json?description=%s&lat=%s&long=%s";
	private static final String DEFAULT_KEYWORD = "developer";
	
	/**
	 * Return a list of items based on the search of lat, lon and keyword. If empty in the request body OR error status code, 
	 * search will return empty list.
	 * @param lat the latitude we want to search for
	 * @param lon the longitude we want to search for
	 * @param keyword the keyword we want to search for
	 * @return a list of items based on the search of lat, lon and keyword. If empty in the request body OR error status code, 
	 * search will return empty list.
	 */
	public List<Item> search(double lat, double lon, String keyword){
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
		
		String url = String.format(URL_TEMPLATE, keyword, lat, lon);
		
		CloseableHttpClient httpclient = HttpClients.createDefault(); 
		// HttpClients is the parent of CloseableHttpClient with methods named createDefault
		ResponseHandler<List<Item>> responseHandler = new ResponseHandler<List<Item>>(){

			@Override
			/**
			 * Get an empty ArrayList iff the response body is empty OR cannot get status. 
			 * ELse we get a list of items based on the body of response (JSONArray) 
			 */
			public List<Item> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				if(response.getStatusLine().getStatusCode() != 200 || response.getEntity() == null) {
					return new ArrayList<>();
				}
				HttpEntity entity = response.getEntity();
				String responseBody = EntityUtils.toString(entity);
				JSONArray itemJsonarray = new JSONArray(responseBody);
				return getItemList(itemJsonarray);
			}
			
		};
		
		try {
			// Excute return anything that is returned by ResponseHandler.handleResponse
			return httpclient.execute(new HttpGet(url), responseHandler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ArrayList<>();
	}
	
	
	/**
	 * According to the input itemJsonarray, we return a list of items with attributes. 
	 * @param itemJsonarray input JsonArray of items 
	 * @return a list of corresponding items 
	 */
	private List<Item> getItemList(JSONArray itemJsonarray){
		List<Item> itemList = new ArrayList<>();
		List<String> descriptions = new ArrayList<>();
		
		for(int i = 0; i<itemJsonarray.length(); i++) {
			String description = getStringFieldOrEmpty(itemJsonarray.getJSONObject(i), "description");
			if(description.equals("") || description.equals("\n")) {
				descriptions.add(getStringFieldOrEmpty(itemJsonarray.getJSONObject(i), "title"));
			}else {
				descriptions.add(description);
			}
			
			List<List<String>> keywords = MonkeyLearnClient.extractKeywords(descriptions.toArray(new String[descriptions.size()]));
			JSONObject obj = itemJsonarray.getJSONObject(i);
			Item newItem = Item.builder()
					.itemId(getStringFieldOrEmpty(obj, "id"))
					.name(getStringFieldOrEmpty(obj, "title")).
					address(getStringFieldOrEmpty(obj, "location")).
					url(getStringFieldOrEmpty(obj, "url")).
					imageUrl(getStringFieldOrEmpty(obj, "company_logo")).
					keywords(new HashSet<String>(keywords.get(i))).
					build();
			
			
			itemList.add(newItem);					
		}
		return itemList;
	}
	
	/**
	 * 
	 * @param obj
	 * @param field
	 * @return an empty str if the object is null OR a string describing the object if not null. 
	 */
	private String getStringFieldOrEmpty(JSONObject obj, String field) {
		return obj.isNull(field)? "" : obj.getString(field);
	}

}
