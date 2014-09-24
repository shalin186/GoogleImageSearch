package com.codepath.googleimagesearch.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.AndroidCharacter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.codepath.googleimagesearch.R;
import com.codepath.googleimagesearch.models.ImageSearchSettingsModel;

public class SettingsDialog extends DialogFragment {

	private Spinner spinImageSize;
	private Spinner spinColorFilter;
	private Spinner spinImageType;
	private EditText etSiteFilter;
	private Button btnSave;
	private Button btnCancel;
	
	public interface SettingsDialogListener {
		void onSaveSettingsDialog(ImageSearchSettingsModel settingsModel);
	}


	public SettingsDialog() {
		// Empty constructor required for DialogFragment
	}

	public static SettingsDialog newInstance(String title) {
		SettingsDialog frag = new SettingsDialog();
		Bundle args = new Bundle();
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_settings, container);
		String title = getArguments().getString("title", "Advanced Filters");
		getDialog().setTitle(title);
		getViews(view);
		setOnClickListeners();
		GetSetDialogFields();

		return view;
	}

	private void GetSetDialogFields() {
		ImageSearchSettingsModel settingsModel = (ImageSearchSettingsModel) getArguments()
				.getSerializable("settings");
		
		if(!settingsModel.imageSize.isEmpty()){
			setSpinnerToValue(spinImageSize, settingsModel.imageSize);
			setSpinnerToValue(spinColorFilter, settingsModel.colorFilter);
			setSpinnerToValue(spinImageType, settingsModel.imageType);
			etSiteFilter.setText(settingsModel.siteFilter);
		}
	}

	public void setSpinnerToValue(Spinner spinner, String value) {
		int index = 0;
		SpinnerAdapter adapter = spinner.getAdapter();
		for (int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getItem(i).equals(value)) {
				index = i;
				break; // terminate loop
			}
		}
		spinner.setSelection(index);
	}
	
	private void setOnClickListeners() {
		btnSave.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				ImageSearchSettingsModel settingsModel = new ImageSearchSettingsModel(
						spinImageSize.getSelectedItem().toString(),
						spinColorFilter.getSelectedItem().toString(),
						spinImageType.getSelectedItem().toString(),
						etSiteFilter.getText().toString());

				SettingsDialogListener listner = (SettingsDialogListener) getActivity();
				listner.onSaveSettingsDialog(settingsModel);
				
				 dismiss();
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	private void getViews(View view) {
		btnSave = (Button) view.findViewById(R.id.btnSave);
		btnCancel = (Button) view.findViewById(R.id.btnCancel);
		spinImageSize = (Spinner) view.findViewById(R.id.spinImageSize);
		spinColorFilter = (Spinner) view.findViewById(R.id.spinColorFilter);
		spinImageType = (Spinner) view.findViewById(R.id.spinImageType);
		etSiteFilter = (EditText) view.findViewById(R.id.etSiteFilter);
		
		// Create an ArrayAdapter using the string array and a custom spinner layout
		ArrayAdapter<CharSequence> imageSizeAdapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.image_size_array, R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		imageSizeAdapter.setDropDownViewResource(R.layout.simple_spinner_list);
		// Apply the adapter to the spinner
		spinImageSize.setAdapter(imageSizeAdapter);
		
		// Create an ArrayAdapter using the string array and a custom spinner layout
		ArrayAdapter<CharSequence> colorFilterAdapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.color_filter_array, R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		colorFilterAdapter.setDropDownViewResource(R.layout.simple_spinner_list);
		// Apply the adapter to the spinner
		spinColorFilter.setAdapter(colorFilterAdapter);
		
		// Create an ArrayAdapter using the string array and a custom spinner layout
		ArrayAdapter<CharSequence> imageTypeAdapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.image_type_array, R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		imageTypeAdapter.setDropDownViewResource(R.layout.simple_spinner_list);
		// Apply the adapter to the spinner
		spinImageType.setAdapter(imageTypeAdapter);		
	}
}
