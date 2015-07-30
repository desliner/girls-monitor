package com.jad.images;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author Max Myslyvtsev
 * @since 7/29/15
 */
@Component
public class GoogleImageProvider implements ImageProvider {

    private static final List<Integer> OFFSETS = ImmutableList.of(
            0, 8, 16, 24, 32, 40, 48, 56
    );

    private static final int PER_PAGE = 8;

    private static final List<String> MANDATORY_KEYWORDS = Arrays.asList(
            "sexy", "hot"
    );

    private static final Map<String, List<String>> CLASSIFIERS = Collections.emptyMap();

    private static final List<String> DIVERSITY_KEYWORDS = Arrays.asList(
            "asian", "car", "school", "beach", "bike", "swimsuite", "glasses", "tatoo"
    );
    private static final int DIVERSITY_KEYWORDS_MIN = 0;
    private static final int DIVERSITY_KEYWORDS_MAX = 1;

    private static final String KEYWORD_SUFFIX = "girls wallpaper";


    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Random random = new Random();


    @PostConstruct
    public void init() {
        long pages = OFFSETS.size() * MANDATORY_KEYWORDS.size();

        for (List<String> classifierKeywords : CLASSIFIERS.values()) {
            pages *= classifierKeywords.size() + 1;
        }

        int diversityStates = 0;
        for (int i = DIVERSITY_KEYWORDS_MIN; i <= DIVERSITY_KEYWORDS_MAX; i++) {
            diversityStates += CombinatoricsUtils.binomialCoefficient(DIVERSITY_KEYWORDS.size(), i);
        }

        pages *= diversityStates;

        long images = pages * PER_PAGE;

        log.info("Will randomly serve {} different pages containing up to {} different images", pages, images);
    }

    @Override
    public List<String> getRandomImages() throws Exception {
        String url = buildRandomUrl();
        log.info("Fetching {}", url);
        JSONObject jsonObject = callForJson(url);
        return extractUrls(jsonObject);
    }

    private String buildRandomUrl() throws UnsupportedEncodingException {
        int offset = randomItem(OFFSETS);
        List<String> keywords = new ArrayList<String>();
        keywords.add(randomItem(MANDATORY_KEYWORDS));
        for (List<String> classifierKeywords : CLASSIFIERS.values()) {
            if (random.nextDouble() > 0.8) {
                keywords.add(randomItem(classifierKeywords));
            }
        }
        int diversityKeywords = DIVERSITY_KEYWORDS_MIN;
        if (random.nextDouble() > 0.5) {
            diversityKeywords += random.nextInt(DIVERSITY_KEYWORDS_MAX + 1);
        }
        keywords.addAll(randomSubList(DIVERSITY_KEYWORDS, diversityKeywords));
        keywords.add(KEYWORD_SUFFIX);
        String query = StringUtils.join(keywords, " ");
        query = URLEncoder.encode(query, "UTF-8");
        return String.format("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&safe=off&imgsz=xxlarge&start=%d&rsz=%d&q=%s&nonce=%s",
                offset, PER_PAGE, query, UUID.randomUUID().toString());
    }

    private <T> T randomItem(List<T> input) {
        return input.get(random.nextInt(input.size()));
    }

    private <T> List<T> randomSubList(List<T> input, int size) {
        List<T> list = new LinkedList<T>(input);
        Collections.shuffle(list);
        return list.subList(0, size);
    }

    private List<String> extractUrls(JSONObject jsonObject) {
        try {
            JSONArray results = jsonObject.getJSONObject("responseData").getJSONArray("results");
            List<String> urls = new ArrayList<String>(results.length());
            for (int i = 0; i < results.length(); i++) {
                String url = results.getJSONObject(i).getString("url");
                urls.add(url);
            }
            return urls;
        } catch (RuntimeException e) {
            throw new RuntimeException("Unable to parse response: " + jsonObject);
        }
    }

    private JSONObject callForJson(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return new JSONObject(sb.toString());
        } finally {
            is.close();
        }
    }
}
