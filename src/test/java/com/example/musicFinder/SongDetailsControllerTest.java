package com.example.musicFinder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SongDetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetSongDetails_withValidArtistAndSong() throws Exception {
        // Test with a valid artist and song name
        mockMvc.perform(get("/song/Coldplay/Yellow"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.song").value("Yellow"))
                .andExpect(jsonPath("$.artist").value("Coldplay"))
                .andExpect(jsonPath("$.youtubeSearch").exists())
                .andExpect(jsonPath("$.lyrics").exists());
    }
    @Test
    void testSpecialCharacters_inArtistName_arePreserved() throws Exception {
        // Artist name with special character '!' (e.g., P!nk)
        mockMvc.perform(get("/song/{artist}/{name}", "P!nk", "Sober"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.artist").value("P!nk"))
                .andExpect(jsonPath("$.song").value("Sober"))
                .andExpect(jsonPath("$.youtubeSearch").exists())
                .andExpect(jsonPath("$.lyrics").exists());
    }

    @Test
    void testYoutubeSearchUrl_containsYoutubeAndQueryParameters() throws Exception {
        // Verify youtubeSearch looks like a YouTube search/watch URL containing expected query parts
        mockMvc.perform(get("/song/{artist}/{name}", "Coldplay", "Yellow"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.youtubeSearch").value(matchesPattern("https?://.*(youtube|youtu\\.be).*(search_query|watch|v=).*")));
    }

    @Test
    void testLyrics_useBrTags_and_noNewlineCharacters() throws Exception {
        // Lyrics should contain <br> tags and not raw newline characters
        mockMvc.perform(get("/song/{artist}/{name}", "The Beatles", "Hey Jude"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.lyrics").value(anyOf(containsString("<br>"), containsString("{\"error\":\"Lyrics not found\"}"))))
                .andExpect(jsonPath("$.lyrics").value(not(containsString("\n"))))
                .andExpect(jsonPath("$.lyrics").value(not(containsString("\r"))));
    }

    // Additional edge-case tests can be added similarly (very long names, numeric names, missing song, etc.)
    // TODO: Add at least 3 edge case tests for the /song/{artist}/{name} endpoint
    // Consider testing scenarios such as:
    // - Artist and song names with spaces (e.g., "Stiff Little Fingers" and "Alternative Ulster")
    // - Non-existent songs (should still return 200 OK with response structure)
    // - Special characters in names (e.g., "AC/DC", "P!nk")
    // - Case preservation (verify UPPERCASE, lowercase, MixedCase are preserved)
    // - YouTube search URL format validation
    // - Lyrics formatting (check for <br> tags instead of \n or \r)
    // - Response structure validation (all required fields present)
    // - Very long artist or song names
    // - Numbers in artist or song names

}


