package com.vmware.talentboost.networkofgiving.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class UrlDecoder {
    private static final Logger LOGGER = Logger.getLogger(UrlDecoder.class.getName());

    public static String getDecodeName(String name) {
        if (name.contains("%")) {
            try {
                return URLDecoder.decode(name, "utf-8");
            } catch (UnsupportedEncodingException e) {
                LOGGER.log(Level.WARNING, "Unsupported character in the name", e);
            }
        }
        return name;
    }
}
