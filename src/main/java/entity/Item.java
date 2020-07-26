package entity;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class Item {
	
	private String itemId;
	private String name;
	private String address;
	private Set<String> keywords;
	private String imageUrl;
	private String url;
	
	public JSONObject toJSONObject() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("item_id", itemId);
		jsonObject.put("name", name);
		jsonObject.put("address", address);
		jsonObject.put("keywords", new JSONArray(keywords));
		jsonObject.put("imageUrl", imageUrl);
		jsonObject.put("url", url);
		return jsonObject;
	}
}
