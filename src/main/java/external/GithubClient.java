package external;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

// Any changes after pom.xml must click Sava button and then Maven install 
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import entity.Item;

import java.util.*;


public class GithubClient{
	private static final String URL_TEMPLATE = "https://www.themuse.com/api/public/jobs?page=1&api_key=e300a5a75f3402f92cab016ff68a36ae5b81ffa1adaa30f334f96ce4c7115b0c";
	private static final String DEFAULT_KEYWORD = "developer";
	

	/**
	 * Return a list of items based on the search of lat, lon and keyword. If empty in the request body OR error status code, 
	 * search will return empty list.
	 * @param keyword the keyword we want to search for
	 * @return a list of items based on the search of lat, lon and keyword. If empty in the request body OR error status code, 
	 * search will return empty list.
	 */
	public List<Item> search(String keyword) {
		
		//Assign a value for keyword
		if(keyword == null) {
			keyword = DEFAULT_KEYWORD;
		}
		
		try {
			keyword = URLEncoder.encode(keyword, "UTF-8");
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//Update the url
//		String url = String.format(URL_TEMPLATE, keyword, lat, lon);
		String url = URL_TEMPLATE;
		//IMPOSTANT 
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		//create a custom response handler to handle the response 
		ResponseHandler<List<Item>> responseHandler = new ResponseHandler<List<Item>>() {
			
			@Override
			/**\
			 * if we cannot get the response (status: not 200) or the entity is empty, we will return a new list of items,
			 * ELse, we will transfer response.getEntity to string and then write it into an JSONArray
			 * final HttpResponse response: 
			 */
			public List<Item> handleResponse(final HttpResponse response) throws IOException{
				if (response.getStatusLine().getStatusCode() != 200) {
					return new ArrayList<>();
				}
				HttpEntity entity = response.getEntity();
				if(entity == null) {
					return new ArrayList<>();
				}
				String responseBody = EntityUtils.toString(entity);
				ObjectMapper mapper = new ObjectMapper();
				try{
					Map<String, Object> jsonMap = mapper.readValue(responseBody, Map.class);
					ArrayList<LinkedHashMap<String, Object>> map = (ArrayList<LinkedHashMap<String, Object>>)jsonMap.get("results");
					// ArrayList<LinkedHashMap<String, String>>
					return getItemList(map);
				}catch (Exception e){
					e.printStackTrace();

				}
//				JSONArray array = new JSONArray(responseBody);
				return getItemList(new ArrayList<>());  //Update
			}
		};
		
		try {
			return httpclient.execute(new HttpGet(url), responseHandler); // Why not change?
		}catch(ClientProtocolException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		// create file

		
		return new ArrayList<>(); // Update 
	}
	
	// Why it matters? 
	
	/**
	 * Get a list of items objects from JSONArray 
	 * @param array the JSONArray contains json objects from 
	 * @return a list of item objects stored in the JSONArray 
	 */
	private List<Item> getItemList(ArrayList<LinkedHashMap<String, Object>> array){
		List<Item> itemList = new ArrayList<>();
		List<String> descriptionList = new ArrayList<>();
		
		for(int j = 0; j < array.size(); j++) {
			String description = (String)array.get(j).get("contents");
			if(description.equals("") || description.equals("\n")) {
				// If empty in description, we use title to do extraction
				descriptionList.add((String)array.get(j).get("name"));
			}else {
				descriptionList.add(description);
			}
		}
		
		List<List<String>> keywords = MonkeyLearnClient.extractKeywords(descriptionList.toArray(new String[descriptionList.size()]));
		
		
		for(int i = 0; i<array.size(); ++i) {
			
			LinkedHashMap<String, Object> object = array.get(i);   // Iterate and get the JSONObject from jsonarray
			
			Item item = Item.builder()
							.itemId(object.get("id").toString())
							.name(object.get("name") + ", " + ((LinkedHashMap<String, Object>)object.get("company")).get("name"))
							.address( ((List<LinkedHashMap<String, String>>)object.get("locations")).get(0).get("name"))
							.url(((LinkedHashMap<String, String>)object.get("refs")).get("landing_page"))
							.build();
			itemList.add(item);
		}
		return itemList;
		
	}
	
	private String getStringFieldOrEmpty(JSONObject obj, String field) {
		return obj.isNull(field)? "": obj.getString(field);
	}

}
