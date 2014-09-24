package com.codepath.googleimagesearch.models;

import java.io.Serializable;

public class ImageSearchSettingsModel implements Serializable{
	private static final long serialVersionUID = -5001885086362337060L;
	public String imageSize;
	public String colorFilter;
	public String imageType;
	public String siteFilter;

	public ImageSearchSettingsModel(String imageSize, String colorFilter,
			String imageType, String siteFilter) {
		this.imageSize = imageSize;
		this.colorFilter = colorFilter;
		this.imageType = imageType;
		this.siteFilter = siteFilter;
	}
}
