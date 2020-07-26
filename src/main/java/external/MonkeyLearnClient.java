package external;

import java.util.ArrayList;
import java.util.List;

//Please remember that in MonkeyLearn API, you should import simple.JSONObject instead of JSONObject. Same as JSONArray
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import com.monkeylearn.ExtraParam;
import com.monkeylearn.MonkeyLearn;
import com.monkeylearn.MonkeyLearnException;
import com.monkeylearn.MonkeyLearnResponse;

public class MonkeyLearnClient {
	private static final String MODEL_ID = "Your_Model_Key";  // Replace
	private static final String API_KEY = "Your_Api_Key"; // Replace
	
	
	
	/**
	 * Remember you comment the last main method before you run this main method. 
	 * This is a test main function, you should change the value of MODEL_ID and API_KEY above and then run it to check of your 
	 * API works. The return value is shown below. Yeah! An array of JSONArrays. 
	 * 
	 * [
	 *  [
	 *   {"count":1,"positions_in_text":[11],"keyword":"movies","relevance":"0.909"},
	 *   {"count":1,"positions_in_text":[25],"keyword":"books","relevance":"0.909"}
	 *  ],
	 *  [
	 *   {"count":1,"positions_in_text":[11],"keyword":"movies","relevance":"0.909"},
	 *   {"count":1,"positions_in_text":[22],"keyword":"books","relevance":"0.909"},
	 *   {"count":1,"positions_in_text":[35],"keyword":"sandwich","relevance":"0.909"}
	 *  ]
	 *   
	 * ]
	 * 
	 * @param args
	 */
	
//	public static void main(String[] args) {
//		
//		String[] textList = {"I love the movie and the book", "I hate the movie, the book and the sandwich"};
//		ExtraParam[] extraParams = {new ExtraParam("max_keywords", "3")};
//		MonkeyLearnResponse res = null;
//		try {
//			res = ml.extractors.extract(MODEL_ID, textList, extraParams);
//		} catch (MonkeyLearnException e) {
//			e.printStackTrace();
//		}
//		System.out.println(res.arrayResult);
//	}
	
	/**
	 * Extract the max three keywords for each document in the textList
	 * @param textList a list containing certain number of documents
	 * @return a list of list that contains the max three keywords of the document. OR empty arrayList if nothing found.
	 */
	public static List<List<String>> extractKeywords(String[] textList){
		MonkeyLearn ml = new MonkeyLearn(API_KEY);
		ExtraParam[] extraParams = {new ExtraParam("max_keywords", "3")};
		try {
			MonkeyLearnResponse monkeyLearnResponse = ml.extractors.extract(MODEL_ID, textList, extraParams);
			JSONArray result = monkeyLearnResponse.arrayResult;  // This is a simple JSONArray
			return getKeywordsFromJSONArray(result);
		} catch (MonkeyLearnException e) {
			System.out.println("Sorry, we cannot find your monkeyLearn address");
			e.printStackTrace();
		}
		return new ArrayList<>();
	}


	/**
	 * Extract all keywords from the single document from the input JSONArray
	 * @param result input JSONArray
	 * @return  a list of list that contains the max three keywords of the document. OR empty arrayList if nothing found.
	 */
	private static List<List<String>> getKeywordsFromJSONArray(JSONArray result) {
		List<List<String>> keywordsList = new ArrayList<>();
		
		for(int i = 0; i < result.size(); i++) {
			ArrayList<String> eleList = new ArrayList<>();
			JSONArray keywordsArray = (JSONArray) result.get(i);
			for(int j = 0; j < keywordsArray.size(); j++) {
				JSONObject ele = (JSONObject) keywordsArray.get(j);
				eleList.add((String) ele.get("keyword"));
			}
			keywordsList.add(eleList);
		}
		return keywordsList;
	}
	
	
	/*
	 * 
	 * Remember to comment last main method before you run this. This method is to check whether extractKeywords works. 
	 * The return value should be:
	 * 
	 *  computer science students
	 *  hearted person
     *  second day
	 *
	 */
	public static void main(String[] args) {
		String[] textList = {"Locky is the greatest computer science student with "
				+ "GPA4.0. He is also a very kind-hearted person who is willing to offer help. However, an Indian cheated him"
				+ "about $500. On the second day, Indian soldiers invaded his country, killing the innocent people with tanks and riffles."};
		
		List<List<String>> wordsList = extractKeywords(textList);
		for(List<String> words: wordsList) {
			for(String word: words) {
				System.out.println(word);
			}
			System.out.println();
		}
	}
	
}
