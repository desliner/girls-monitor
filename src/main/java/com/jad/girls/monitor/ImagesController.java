package com.jad.girls.monitor;

import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author Max Myslyvtsev
 * @since 7/29/15
 */
@RestController
public class ImagesController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final RateLimiter rateLimiter = RateLimiter.create(0.1);


    @Autowired
    private ImageProvider imageProvider;


    private volatile List<String> lastFetchedPage = null;


    @RequestMapping("images/next")
    public List<String> getNextPage() throws Exception {
        if (rateLimiter.tryAcquire()) {
            return fetchNextPage();
        } else {
            if (lastFetchedPage != null) {
                return lastFetchedPage;
            } else {
                rateLimiter.acquire();
                return fetchNextPage();
            }
        }
    }

    private List<String> fetchNextPage() throws Exception {
        try {
            return lastFetchedPage = imageProvider.getRandomImages();
        } catch (Exception e) {
            if (lastFetchedPage != null) {
                log.error("Unable to fetch images, using last cached page", e);
                return lastFetchedPage;
            } else {
                throw e;
            }
        }
    }

}
