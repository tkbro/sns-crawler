package codes.dirty.sns.crawler.common.util;

import java.util.HashMap;
import java.util.Map;

public final class UrlUtils {

    private UrlUtils() {
    }

    public static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();

        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    /***
     * {key1}={val1}&...&{keyn}={valn}
     * @param keyValueMap
     * @return
     */
    public static String getUrlEncodedRequestBody(Map<String, String> keyValueMap) {
        StringBuilder sb = new StringBuilder();
        keyValueMap.entrySet().forEach(entry -> {
            sb.append(String.format("%s=%s&", entry.getKey(), entry.getValue()));
        });
        sb.substring(0, sb.length() - 1);
        return sb.toString();
    }
}
