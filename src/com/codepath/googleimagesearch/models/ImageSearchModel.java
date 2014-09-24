package com.codepath.googleimagesearch.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ImageSearchModel {

	public String title;
	public TbImage tbImg;
	public Image img;

	public class TbImage {
		public String url;
		public int width;
		public int height;

		public TbImage(String url, int width, int height) {
			this.url = url;
			this.width = width;
			this.height = height;
		}
	}

	public class Image{
		public String url;
		public int width;
		public int height;

		public Image(String url, int width, int height) {
			this.url = url;
			this.width = width;
			this.height = height;
		}
	}

	public ImageSearchModel(){
		
	}
	public ImageSearchModel(JSONObject photoJSON) {
		try {
			this.title = photoJSON.getString("title");

			this.img = new Image(photoJSON.getString("url"),
					photoJSON.getInt("width"), photoJSON.getInt("height"));

			this.tbImg = new TbImage(photoJSON.getString("tbUrl"),
					photoJSON.getInt("tbWidth"), photoJSON.getInt("tbHeight"));

		} catch (JSONException e) {
			Log.i("ImageSearch", "Exception in ImageSearchModel Constructor");
			e.printStackTrace();
		}
	}

	public static ArrayList<ImageSearchModel> getImgResultList(
			JSONArray photosJSON) {
		JSONObject photoJSON;
		ArrayList<ImageSearchModel> ImgSearchList = new ArrayList<ImageSearchModel>();

		for (int i = 0; i < photosJSON.length(); i++) {
			try {
				photoJSON = photosJSON.getJSONObject(i);
				ImgSearchList.add(new ImageSearchModel(photoJSON));
			} catch (JSONException e) {
				Log.i("ImageSearch", "Exception in getImgResultList");
				e.printStackTrace();
			}
		}
		return ImgSearchList;
	}

	@Override
	public String toString() {
		
		return this.title;
	}
	
	
}
