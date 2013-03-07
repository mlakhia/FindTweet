package com.applabz.findtweet;
import android.content.SearchRecentSuggestionsProvider; 

public class SearchSuggProvider extends SearchRecentSuggestionsProvider { 
   
   public static final String AUTHORITY =  SearchSuggProvider.class.getName(); 

   public static final int MODE = DATABASE_MODE_QUERIES; 

   public SearchSuggProvider() { 
      setupSuggestions(AUTHORITY, MODE); 
   } 
}