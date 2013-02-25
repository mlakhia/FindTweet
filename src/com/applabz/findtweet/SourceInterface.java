package com.applabz.findtweet;

import java.util.ArrayList;

public interface SourceInterface {
	int size(); 				// int size()
	Tweet getTweet(long id); 	// Tweet getTweet(int long)	
	ArrayList<Tweet> getAllTweets();	
}
