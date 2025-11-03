package com.example.musicFinder;

public class CacheDecorator implements SearchProvider {
    
    private final SearchProvider searchProvider;

    public CacheDecorator(SearchProvider searchProvider) {
        this.searchProvider = searchProvider;
    }

    @Override
    public String search(String artist, String song) {
        // Use the CacheService to cache the search results, and to check if the search results are already cached
        // Use "Cached Result:" as a prefix for the cached results to differentiate them from the direct fetch of uncached search results
        throw new UnsupportedOperationException("Unimplemented method 'search'");
    }
}
