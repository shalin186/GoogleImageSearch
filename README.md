# Instagram Popular Photoviewer

This is an Android application which can be used to search google images using google image search apis

Time spent: 12-15 hours

Completed user stories:

* [x] User can enter a search query that will display a grid of image results from the Google Image API
* [x] User can click on "settings" which allows selection of advanced search options to filter results
* [x] User can configure advanced search filters such as
		* [x] Size (small, medium, large, extra-large)
		* [x] Color filter (black, blue, brown, gray, green, etc...)
		* [x] Type (faces, photo, clip art, line art)
		* [x] Site (espn.com)
* [x] Subsequent searches will have any filters applied to the search results
* [x] User can tap on any image in results to see the image full-screen
* [x] User can scroll down “infinitely” to continue loading more image results (up to 8 pages)
* [x] Robust error handling, check if internet is available, handle error cases, network failures
* [x] Use the ActionBar SearchView or custom layout as the query box instead of an EditText
* [x] User can share an image to their friends or email it to themselves
* [x] Replace Filter Settings Activity with a lightweight modal overlay
* [x] User can zoom or pan images displayed in full-screen detail view

Libraries Used:
* [x] TouchImageView: https://github.com/Pkmmte/CircularImageView
* [x] android-async-http: https://www.dropbox.com/s/zqggkqv60zggyrt/android-async-http-1.4.5.jar?dl=1
* [x] Picasso Image: https://www.dropbox.com/s/25py1bmjr45936v/picasso-2.3.4.jar?dl=1
 
Walkthrough of all user stories:

![Video Walkthrough](GoogleImageSearch.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).
