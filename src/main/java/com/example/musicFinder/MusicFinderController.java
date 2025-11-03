package com.example.musicFinder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.regex.Pattern;

@RestController
public class MusicFinderController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String LYRICS_BASE = "https://api.lyrics.ovh/v1";
    private static final Pattern SAFE_PART = Pattern.compile("^[\\p{L}0-9 .,'()\\-]{1,100}$");

private String sanitizePart(String value, String fieldName) {
    if (value == null) throw new IllegalArgumentException(fieldName + " is required");
    String s = java.text.Normalizer.normalize(value.trim(), java.text.Normalizer.Form.NFKC);

    if (s.isEmpty() || s.length() > 200) {
        throw new IllegalArgumentException("Invalid " + fieldName);
    }

    for (int i = 0; i < s.length(); i++) {
        if (Character.isISOControl(s.charAt(i))) {
            throw new IllegalArgumentException("Invalid " + fieldName);
        }
    }
    return s;
}


    @GetMapping("/status")
    public String getStatus() {
        return "{\"status\":\"Application is running\"}";
    }

    private String getFormattedLyrics(String artist, String song) {
        String safeArtist = sanitizePart(artist, "artist");
        String safeSong = sanitizePart(song, "song");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(LYRICS_BASE)
                .pathSegment(safeArtist, safeSong)  
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        try {
            String rawJson = restTemplate.getForObject(uri, String.class);

            
            JsonNode jsonNode = objectMapper.readTree(rawJson);
            String rawLyrics = jsonNode.get("lyrics").asText();

    
            String formattedLyrics = rawLyrics.replaceAll("\\r", "");
            formattedLyrics = formattedLyrics.replaceAll("\\n+", "<br>");
            return formattedLyrics.trim();
        } catch (Exception e) {
            return "{\"error\":\"Lyrics not found\"}";
        }
    }


    private String getYouTubeSearchUrl(String artist, String song) {
        String safeArtist = sanitizePart(artist, "artist");
        String safeSong = sanitizePart(song, "song");

        URI uri = UriComponentsBuilder
                .fromHttpUrl("https://www.youtube.com/results")
                .queryParam("search_query", safeArtist + " " + safeSong)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        return uri.toString();
    }


    @GetMapping("/song/{artist}/{name}")
    public ObjectNode getSongDetails(@PathVariable String artist, @PathVariable String name) {
        String youtubeSearchUrl = getYouTubeSearchUrl(artist, name);
        String lyrics = getFormattedLyrics(artist, name);

        ObjectNode response = objectMapper.createObjectNode();
        response.put("song", name);
        response.put("artist", artist);
        response.put("youtubeSearch", youtubeSearchUrl);
        response.put("lyrics", lyrics);
        return response;
    }



    private final Logger logger;
    private final SearchProvider youtubeProvider;
    private final SearchProvider lyricsProvider;
    private final SearchStrategy exactSearchStrategy;
    private final SearchStrategy fuzzySearchStrategy;

    @Autowired
    public MusicFinderController(Logger logger) {
        this.logger = logger;
        this.youtubeProvider = new CacheDecorator(new YouTubeSearchProviderFactory().createProvider());
        this.lyricsProvider = new CacheDecorator(new LyricsSearchProviderFactory().createProvider());
        this.exactSearchStrategy = new CacheDecoratorStrategy(new ExactSearchStrategy());
        this.fuzzySearchStrategy = new CacheDecoratorStrategy(new FuzzySearchStrategy());
    }

    @GetMapping("/findMusic")
public String findMusic(@RequestParam String artist, @RequestParam String song) {
    String safeArtist = sanitizePart(artist, "artist");
    String safeSong = sanitizePart(song, "song");

    logger.logMessage("Searching for: " + safeArtist + " - " + safeSong);
    return "Results for " + safeArtist + " - " + safeSong;
}


    @GetMapping("/findMusic/factory")
    public String findMusicFactory(@RequestParam String artist, @RequestParam String song, @RequestParam String provider) {
        SearchProviderFactory factory;

        if ("youtube".equalsIgnoreCase(provider)) {
            factory = new YouTubeSearchProviderFactory();
        } else if ("lyrics".equalsIgnoreCase(provider)) {
            factory = new LyricsSearchProviderFactory();
        } else {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }

        SearchProvider searchProvider = factory.createProvider();
        return searchProvider.search(artist, song);
    }

    @GetMapping("/findMusic/decorator")
    public String findMusicDecorator(@RequestParam String artist, @RequestParam String song, @RequestParam String provider) {
        SearchProvider searchProvider;

        if ("youtube".equalsIgnoreCase(provider)) {
            searchProvider = youtubeProvider;
        } else if ("lyrics".equalsIgnoreCase(provider)) {
            searchProvider = lyricsProvider;
        } else {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }

        return searchProvider.search(artist, song);
    }

    @GetMapping("/findMusic/strategy")
    public String findMusic(@RequestParam String artist, @RequestParam String song, @RequestParam String strategy) {
        SearchStrategy searchStrategy;

        if ("exact".equalsIgnoreCase(strategy)) {
            searchStrategy = exactSearchStrategy;
        } else if ("fuzzy".equalsIgnoreCase(strategy)) {
            searchStrategy = fuzzySearchStrategy;
        } else {
            throw new IllegalArgumentException("Unsupported strategy: " + strategy);
        }

        throw new UnsupportedOperationException("Unimplemented method 'findMusic/strategy' - with decorator caching");
    }
}
