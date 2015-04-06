package com.example.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ��װ��Ƭ�ļ���
 * @author MV
 *
 */
public class Photo {

	private static final String JSON_FILENAME = "filename";
	/**
	 * ��Ƭ�ļ���
	 */
	private String mFileName;
	
	public void setmFileName(String mFileName) {
		this.mFileName = mFileName;
	}
	/*public Photo(JSONObject json) throws JSONException {
		
		mFileName = json.getString(JSON_FILENAME);
	}*/
	
	public JSONObject toJSON() throws JSONException{
		JSONObject json = new JSONObject();
		json.put(JSON_FILENAME, mFileName);
		
		return json;
	}
	
	public String getmFileName() {
		
		return mFileName;
	}
}
