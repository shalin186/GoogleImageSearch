package com.codepath.googleimagesearch.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.googleimagesearch.R;
import com.codepath.googleimagesearch.models.ImageSearchModel;
import com.squareup.picasso.Picasso;

public class ImgSearchAdapter extends ArrayAdapter<ImageSearchModel> {

	private static class ViewHolder {
		ImageView ivImage;
		TextView tvImageTitle;
	}

	public ImgSearchAdapter(Context context,
			List<ImageSearchModel> imgSearchList) {
		super(context, android.R.layout.simple_list_item_1, imgSearchList);
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		ImageSearchModel imageSearchModel = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_image_search, parent, false);
			
			viewHolder = new ViewHolder();
			viewHolder.ivImage = (ImageView)convertView.findViewById(R.id.ivImage);
			viewHolder.tvImageTitle = (TextView)convertView.findViewById(R.id.tvImageTitle);
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder = (ViewHolder)convertView.getTag();
		}

		viewHolder.ivImage.setImageResource(0);
		Picasso.with(getContext()).load(imageSearchModel.tbImg.url)
		.fit().into(viewHolder.ivImage);
		viewHolder.tvImageTitle.setText(Html.fromHtml(imageSearchModel.title));
		viewHolder.tvImageTitle.setVisibility(View.GONE);
		return convertView;
	}

}
