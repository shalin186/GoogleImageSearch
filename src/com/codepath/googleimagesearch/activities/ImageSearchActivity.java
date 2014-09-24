package com.codepath.googleimagesearch.activities;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.codepath.googleimagesearch.R;
import com.codepath.googleimagesearch.activities.SettingsDialog.SettingsDialogListener;
import com.codepath.googleimagesearch.adapters.ImgSearchAdapter;
import com.codepath.googleimagesearch.models.ImageSearchModel;
import com.codepath.googleimagesearch.models.ImageSearchSettingsModel;
import com.codepath.googleimagesearch.utilities.EndlessScrollListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ImageSearchActivity extends SherlockFragmentActivity implements
		SettingsDialogListener {

	private static final String SEARCH_QUERY_START = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
	private static final String SEARCH_QUERY_PAGE = "&start=";
	private static final String SEARCH_QUERY_END = "&rsz=8";
	private final int REQUEST_CODE = 20;

	// Views
	GridView gvSearchResult;
	SearchView searchView;

	// Array Adapter
	ArrayList<ImageSearchModel> imgSearchList;
	ImgSearchAdapter imgSearchAdapter;

	String searchText="";
	String extraSearchParams="";
	String searchQueryUrl;
	int pageNumber = 0;

	ImageSearchSettingsModel settingsModel = new ImageSearchSettingsModel("",
			"", "", "");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_search);
		getViews();
		setArrayAdapter();
		setOnClickListeners();
	}

	private void setOnClickListeners() {
		// Open the image in full screen view when an item is clicked in grid
		// view
		gvSearchResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long id) {
				if (isNetworkAvailable() == false) {
					Toast.makeText(getBaseContext(),
							"Network Error! Please Try Again",
							Toast.LENGTH_LONG).show();
					return;
				}
				
				Intent fullScreenImageIntent = new Intent(
						ImageSearchActivity.this, FullScreenImageActivity.class);
				ImageSearchModel imageSearchModel = imgSearchList.get(pos);
				fullScreenImageIntent.putExtra("searchtext", searchText);
				fullScreenImageIntent.putExtra("title", imageSearchModel.title);
				fullScreenImageIntent.putExtra("imageurl",
						imageSearchModel.img.url);
				
				Log.i("ImageSearch", imageSearchModel.img.height+"");
				Log.i("ImageSearch", imageSearchModel.img.width+"");
				
				startActivity(fullScreenImageIntent);
			}
		});

		// Support for infinite scrolling
		gvSearchResult.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				pageNumber = page;
				// Google Image Search API supports only upto 8 pages of results
				if (pageNumber < 8) {
					buildSearchQuery(searchText);
					DisplayImages();
				}
			}
		});
	}

	public void DisplayImages() {
		PerformSearch();
	}

	private void PerformSearch() {

		AsyncHttpClient client = new AsyncHttpClient();

		if (isNetworkAvailable() == false) {
			Toast.makeText(getBaseContext(), "Network Error! Please Try Again",
					Toast.LENGTH_LONG).show();
			return;
		}

		Log.i("ImageSearch", "URL Query");
		client.get(searchQueryUrl, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {

				Log.i("ImageSearch", "URL Query Success");
				JSONArray photosJSON = null;
				try {
					photosJSON = response.getJSONObject("responseData")
							.getJSONArray("results");

					if (pageNumber == 0) {
						imgSearchList.clear();
						imgSearchAdapter.clear();
					}

					imgSearchAdapter.addAll(ImageSearchModel
							.getImgResultList(photosJSON));

				} catch (JSONException e) {
					Log.i("ImageSearch", "Exception");
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				Log.i("ImageSearch", "URL Query Failed");
				Toast.makeText(getBaseContext(),
						"Network Error! Please Try Again", Toast.LENGTH_LONG)
						.show();
			}
		});

	}

	private Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
	}

	private void buildSearchQuery(String searchText) {
		searchQueryUrl = SEARCH_QUERY_START + searchText + SEARCH_QUERY_PAGE
				+ pageNumber * 8 + SEARCH_QUERY_END + extraSearchParams;
	}

	private void setArrayAdapter() {
		imgSearchList = new ArrayList<ImageSearchModel>();
		imgSearchAdapter = new ImgSearchAdapter(this, imgSearchList);
		gvSearchResult.setAdapter(imgSearchAdapter);
	}

	private void getViews() {
		gvSearchResult = (GridView) findViewById(R.id.gvSearchResult);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_image_search, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) searchItem.getActionView();
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				// perform query here

				// Hide soft keybaord
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

				// Display images starting from the first image
				imgSearchAdapter.clear();
				pageNumber = 0;
				searchText = query;
				buildSearchQuery(searchText);
				DisplayImages();

				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

		MenuItem settingsItem = menu.findItem(R.id.action_settings);

		return super.onCreateOptionsMenu(menu);
	}

	public void imageSearchSettings(MenuItem miImageSearchSettings) {
		Log.i("ImageSearch", "Settings CLicked!");

		// LaunchSettingsActivity();

		LaunchSettingsDialog();

	}

	private void LaunchSettingsActivity() {
		// Settings Activity
		Intent settingsIntent = new Intent(ImageSearchActivity.this,
				ImageSearchSettingsActivity.class);
		startActivityForResult(settingsIntent, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageSearch) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			ImageSearchSettingsModel settingsModel = (ImageSearchSettingsModel) imageSearch
					.getSerializableExtra("settings");

			Toast.makeText(
					getBaseContext(),
					settingsModel.imageSize + settingsModel.colorFilter
							+ settingsModel.imageType
							+ settingsModel.siteFilter, Toast.LENGTH_LONG)
					.show();
		}

	}

	private void LaunchSettingsDialog() {
		// Settings Dialog
		FragmentManager fm = getSupportFragmentManager();
		SettingsDialog settingsDialog = SettingsDialog
				.newInstance("Advanced Filters");
		Bundle bundleSettings = new Bundle();
		bundleSettings.putSerializable("settings", settingsModel);
		settingsDialog.setArguments(bundleSettings);
		settingsDialog.show(fm, "activity_settings");
	}

	@Override
	public void onSaveSettingsDialog(ImageSearchSettingsModel settingsModel) {
		this.settingsModel = settingsModel;
		extraSearchParams = "";
		if(!(settingsModel.imageSize.equalsIgnoreCase("any"))){
			if(settingsModel.imageSize.equalsIgnoreCase("small")){
				extraSearchParams = "&&imgsz="+"icon";	
			}
			else if(settingsModel.imageSize.equalsIgnoreCase("medium")){
				extraSearchParams = "&&imgsz="+"medium";	
			}
			else if(settingsModel.imageSize.equalsIgnoreCase("large")){
				extraSearchParams = "&&imgsz="+"xxlarge";	
			}
			else if(settingsModel.imageSize.equalsIgnoreCase("extra-large")){
				extraSearchParams = "&&imgsz="+"huge";	
			}
		}
		if(!(settingsModel.colorFilter.equalsIgnoreCase("any"))){
			extraSearchParams = "&&imgcolor="+settingsModel.colorFilter;
		}
		if(!(settingsModel.imageType.equalsIgnoreCase("any"))){
			extraSearchParams = "&&imgtype="+settingsModel.imageType;
		}
		if(!(settingsModel.siteFilter.isEmpty())){
			extraSearchParams = "&&as_sitesearch="+settingsModel.siteFilter;
		}
		
		// Display images starting from the first image
		imgSearchAdapter.clear();
		pageNumber = 0;
		buildSearchQuery(searchText);
		DisplayImages();
	}
}
