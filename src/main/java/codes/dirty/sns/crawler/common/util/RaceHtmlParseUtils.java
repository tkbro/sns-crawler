package codes.dirty.sns.crawler.common.util;

public class RaceHtmlParseUtils {
    /***
     *   when parse attribute has javascript function call statement,
     *   parse parameters to string array.
     * @param href
     *   "someJsFoo('a',1,true)"
     * @return
     *   ["a","1","true"]
     */
    public static String[] parseJavascriptParamtersToArray(String href) {
        int startInclusive = href.indexOf('(') + 1;
        int endExclusive = href.indexOf(')');
        String parameterString = href.substring(startInclusive, endExclusive);
        parameterString = parameterString.replaceAll("\'", "");
        return parameterString.split(",");
    }
}
