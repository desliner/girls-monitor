package com.jad.girls.monitor;

import java.util.List;

/**
 * @author Max Myslyvtsev
 * @since 7/29/15
 */
public interface ImageProvider {

    List<String> getRandomImages() throws Exception;
}
