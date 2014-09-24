package com.codepath.googleimagesearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codepath.googleimagesearch.R;
import com.codepath.googleimagesearch.models.ImageSearchSettingsModel;

public class ImageSearchSettingsActivity extends Activity {

	private Spinner spinImageSize;
	private Spinner spinColorFilter;
	private Spinner spinImageType;
	private EditText etSiteFilter;
	private Button btnSave;
	private Button btnCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		getViews();
		setOnClickListeners();
	}

	private void setOnClickListeners() {
		btnSave.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				ImageSearchSettingsModel settingsModel = new ImageSearchSettingsModel(
						spinImageSize.getSelectedItem().toString(),
						spinColorFilter.getSelectedItem().toString(),
						spinImageType.getSelectedItem().toString(),
						etSiteFilter.getText().toString());

				Intent imageSearch = new Intent();
				imageSearch.putExtra("settings", settingsModel);
				setResult(RESULT_OK, imageSearch); // set result code and bundle
													// data for response
				finish(); // closes the activity, pass data to parent
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void getViews() {
		btnSave = (Button) findViewById(R.id.btnSave);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		spinImageSize = (Spinner) findViewById(R.id.spinImageSize);
		spinColorFilter = (Spinner) findViewById(R.id.spinColorFilter);
		spinImageType = (Spinner) findViewById(R.id.spinImageType);
		etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
	}

}
