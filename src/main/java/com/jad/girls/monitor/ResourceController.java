package com.jad.girls.monitor;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * @author Max Myslyvtsev
 * @since 7/29/15
 */
@Controller
public class ResourceController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/resource/{name:.*}")
    public void getResource(@PathVariable String name, HttpServletResponse response) throws Exception {
        if (name.endsWith(".png")) {
            response.setContentType("image/png");
        } else {
            log.warn("Unknown file extension: {}", name);
        }
        OutputStream outputStream = response.getOutputStream();
        FileInputStream inputStream = new FileInputStream(name);
        try {
            IOUtils.copy(inputStream, outputStream);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

}
