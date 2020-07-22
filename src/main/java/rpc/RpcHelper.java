package rpc;


import java.io.IOException;
import java.util.List;
import external.GithubClient;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import entity.Item;


public class RpcHelper {
	
	public void writeJSONArray(HttpServletResponse response, List<Item> items) {
		JSONArray jsonArray = new JSONArray();
		for(int i = 0; i < items.size(); i++) {
			jsonArray.put(items.get(i).toJSONObject());
		}
		try {
			response.getWriter().print(jsonArray);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
