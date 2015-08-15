package au.edu.sydney.Curracurrong.runtime.api.model;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class UpdateNotification {

	private List<UpdatedResource> updatedResources;
	
	public UpdateNotification(JSONArray json) {	
		List<UpdatedResource> newResources = new ArrayList<UpdatedResource>();

		try {
			for (int i=0; i<json.length(); i++) {
				newResources.add(new UpdatedResource(json.getJSONObject(i)));				
			}
			updatedResources = newResources;
		} catch (JSONException e) {
			throw new IllegalArgumentException("Given JSON object does not contain all required elements.", e);
		}
	}

	public List<UpdatedResource> getUpdatedResources() {
		return updatedResources;
	}

}
