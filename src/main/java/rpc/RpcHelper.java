package rpc;


import java.io.IOException;
import java.util.List;
import external.GithubClient;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Item;


public class RpcHelper {
	
//	public static void writeJSONArray(HttpServletResponse response, List<Item> items) {
//		response.setContentType("application/json");
//		
//		JSONArray jsonArray = new JSONArray();
//		for(int i = 0; i < items.size(); i++) {
//			jsonArray.put(items.get(i).toJSONObject());
//		}
//		try {
//			response.getWriter().print(jsonArray);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void writeJSONArray(HttpServletResponse response, JSONArray array) throws IOException {
		response.setContentType("application/json");
		response.getWriter().print(array);
	}
	
	public static void writeJSONObject(HttpServletResponse response, JSONObject object) throws IOException {
		response.setContentType("application/json");
		response.getWriter().print(object);
	}
	
}
