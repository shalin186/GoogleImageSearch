package com.codepath.googleimagesearch.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.codepath.googleimagesearch.R;
import com.codepath.googleimagesearch.models.ImageSearchModel;
import com.codepath.googleimagesearch.utilities.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class FullScreenImageActivity extends SherlockFragmentActivity {
	// Views
	TextView tvTitle;
	TouchImageView ivFullScreenImage;

	ImageSearchModel imageSearchModel = new ImageSearchModel();
	String title;
	String imageUrl;
	String searchText;
	ActionBar actionBar;

	boolean bFullScreen = false;
	boolean bImageLoaded = false;
	boolean bMenuCreated = false;
	private ShareActionProvider miShareAction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_fullscreen);
		getViews();
		getInentValues();
		setViews();
		setActionBarTitle();
		setOnClickListeners();
	}

	private void setOnClickListeners() {
		// Make the image full screen when clicked
		// ivFullScreenImage.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// if (bFullScreen == false) {
		// bFullScreen = true;
		// actionBar.hide();
		// tvTitle.setVisibility(View.INVISIBLE);
		// }
		// else{
		// bFullScreen = false;
		// actionBar.show();
		// tvTitle.setVisibility(View.VISIBLE);
		// }
		//
		// }
		// });

	}

	private void setActionBarTitle() {
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		String title = (String) actionBar.getTitle();
		actionBar.setTitle(searchText);
	}

	private void setViews() {
		tvTitle.setText(Html.fromHtml(title));
		Log.i("ImageSearch", title);
		Log.i("ImageSearch", imageUrl);
		Picasso.with(this).load(imageUrl).resize(300, 300)
				.into(ivFullScreenImage, new Callback() {
					@Override
					public void onSuccess() {
						// Setup share intent now that image has loaded
						bImageLoaded = true;
						if (bMenuCreated) {
							ShareImage();
						}
					}

					@Override
					public void onError() {
						// ...
					}
				});
	}

	private void getInentValues() {
		title = getIntent().getStringExtra("title");
		imageUrl = getIntent().getStringExtra("imageurl");
		searchText = getIntent().getStringExtra("searchtext");
	}

	private void getViews() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		ivFullScreenImage = (TouchImageView) findViewById(R.id.ivFullScreenImage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_image_fullscreen, menu);
		// Locate MenuItem with ShareActionProvider
		MenuItem item = menu.findItem(R.id.menu_item_share);
		// Fetch reference to the share action provider
		miShareAction = (ShareActionProvider) item.getActionProvider();
		// Return true to display menu
		bMenuCreated = true;
		if (bImageLoaded) {
			ShareImage();
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.miShare:
			ShareImage();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void ShareImage() {
		Log.i("ImageSearch", "Sharing Image");

		// Get access to the URI for the bitmap
		Uri bmpUri = getLocalBitmapUri(ivFullScreenImage);
		if (bmpUri != null) {
			// Construct a ShareIntent with link to image
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
			shareIntent.setType("image/*");
			// Launch sharing dialog for image
			// startActivity(Intent.createChooser(shareIntent, "Share Image"));
			// Attach share event to the menu item provider
			miShareAction.setShareIntent(shareIntent);
		} else {
			Log.i("ImageSearch", "Sharing Failed");
			Toast.makeText(this, "Can Not Share This Image", Toast.LENGTH_LONG)
					.show();
		}
	}

	// Returns the URI path to the Bitmap displayed in specified ImageView
	public Uri getLocalBitmapUri(TouchImageView imageView) {
		Drawable mDrawable = imageView.getDrawable();
		Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

		String path = Images.Media.insertImage(getContentResolver(), mBitmap,
				"Image Description", "test");

		Uri uri = Uri.parse(path);
		return uri;
	}

}
