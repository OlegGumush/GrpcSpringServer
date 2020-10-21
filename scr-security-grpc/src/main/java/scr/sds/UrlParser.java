package scr.sds;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UrlParser {

    public static String getPath(String url) {
        int index = url.indexOf("?");
        if (index == -1) {
            return url;
        }
        return url.substring(0, index);
    }

    public static Map<String, String> getParameters(String url) throws UnsupportedEncodingException {
        int index = url.indexOf("?");
        if (index == -1 || index + 1 == url.length()) {
            return Collections.emptyMap();
        }
        String[] pairs = url.substring(index + 1).split("&");
        Map<String, String> parameters = new ConcurrentHashMap<>(pairs.length);
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            parameters.put(
                    URLDecoder.decode(pair.substring(0, idx), "UTF-8").toLowerCase(),
                    URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
            );
        }
        return parameters;
    }

    public static String getUrl(String path, Map<String, String> parameters) throws UnsupportedEncodingException {
        if (parameters.size() == 0) {
            return path;
        }
        StringBuilder builder = new StringBuilder(path).append("?");
        boolean notFirst = false;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (notFirst) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append("=")
                    .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            notFirst = true;
        }
        return builder.toString();
    }

}
