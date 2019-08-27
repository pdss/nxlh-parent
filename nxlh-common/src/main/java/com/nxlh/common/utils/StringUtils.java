package com.nxlh.common.utils;


import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtils {
    public static final String EMPTY = "";
    public static final String IS = "is";
    public static final char UNDERLINE = '_';
    public static final String PLACE_HOLDER = "{%s}";
    private static final Pattern P_IS_COLUMN = Pattern.compile("^\\w\\S*[\\w\\d]*$");

    private StringUtils() {
    }

    public static String blob2String(Blob blob) {
        if (null != blob) {
            try {
                byte[] returnValue = blob.getBytes(1L, (int)blob.length());
                return new String(returnValue, StandardCharsets.UTF_8);
            } catch (Exception var2) {
                throw ExceptionUtils.mpe("Blob Convert To String Error!");
            }
        } else {
            return null;
        }
    }

    public static boolean isEmpty(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isNotColumnName(String str) {
        return !P_IS_COLUMN.matcher(str).matches();
    }

    public static String camelToUnderline(String param) {
        if (isEmpty(param)) {
            return "";
        } else {
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);

            for(int i = 0; i < len; ++i) {
                char c = param.charAt(i);
                if (Character.isUpperCase(c) && i > 0) {
                    sb.append('_');
                }

                sb.append(Character.toLowerCase(c));
            }

            return sb.toString();
        }
    }

    public static String resolveFieldName(String getMethodName) {
        if (getMethodName.startsWith("get")) {
            getMethodName = getMethodName.substring(3);
        } else if (getMethodName.startsWith("is")) {
            getMethodName = getMethodName.substring(2);
        }

        return firstToLowerCase(getMethodName);
    }

    public static String underlineToCamel(String param) {
        if (isEmpty(param)) {
            return "";
        } else {
            String temp = param.toLowerCase();
            int len = temp.length();
            StringBuilder sb = new StringBuilder(len);

            for(int i = 0; i < len; ++i) {
                char c = temp.charAt(i);
                if (c == '_') {
                    ++i;
                    if (i < len) {
                        sb.append(Character.toUpperCase(temp.charAt(i)));
                    }
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        }
    }

    public static String firstToLowerCase(String param) {
        return isEmpty(param) ? "" : param.substring(0, 1).toLowerCase() + param.substring(1);
    }

    public static boolean isUpperCase(String str) {
        return matches("^[A-Z]+$", str);
    }

    public static boolean matches(String regex, String input) {
        return null != regex && null != input ? Pattern.matches(regex, input) : false;
    }

    public static String sqlArgsFill(String content, Object... args) {
        if (isEmpty(content)) {
            return null;
        } else {
            if (args != null) {
                int length = args.length;
                if (length >= 1) {
                    for(int i = 0; i < length; ++i) {
                        content = content.replace(String.format("{%s}", i), sqlParam(args[i]));
                    }
                }
            }

            return content;
        }
    }

    public static String sqlParam(Object obj) {
        String repStr;
        if (obj instanceof Collection) {
            repStr = quotaMarkList((Collection)obj);
        } else {
            repStr = quotaMark(obj);
        }

        return repStr;
    }

    public static String quotaMark(Object obj) {
        String srcStr = String.valueOf(obj);
        return obj instanceof CharSequence ? StringEscape.escapeString(srcStr) : srcStr;
    }

    public static String quotaMarkList(Collection<?> coll) {
        return (String)coll.stream().map(StringUtils::quotaMark).collect(Collectors.joining(",", "(", ")"));
    }

    public static String concatCapitalize(String concatStr, String str) {
        if (isEmpty(concatStr)) {
            concatStr = "";
        }

        if (str != null && str.length() != 0) {
            char firstChar = str.charAt(0);
            return Character.isTitleCase(firstChar) ? str : concatStr + Character.toTitleCase(firstChar) + str.substring(1);
        } else {
            return str;
        }
    }

    public static String capitalize(String str) {
        return concatCapitalize((String)null, str);
    }

    public static boolean checkValNotNull(Object object) {
        if (object instanceof CharSequence) {
            return isNotEmpty((CharSequence)object);
        } else {
            return object != null;
        }
    }

    public static boolean isEmpty(Object object) {
        return !checkValNotNull(object);
    }

    public static boolean containsUpperCase(String word) {
        for(int i = 0; i < word.length(); ++i) {
            char c = word.charAt(i);
            if (Character.isUpperCase(c)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isCapitalMode(String word) {
        return null != word && word.matches("^[0-9A-Z/_]+$");
    }

    public static boolean isMixedMode(String word) {
        return matches(".*[A-Z]+.*", word) && matches(".*[/_]+.*", word);
    }

    public static boolean endsWith(String str, String suffix) {
        return endsWith(str, suffix, false);
    }

    public static boolean endsWithIgnoreCase(String str, String suffix) {
        return endsWith(str, suffix, true);
    }

    private static boolean endsWith(String str, String suffix, boolean ignoreCase) {
        if (str != null && suffix != null) {
            if (suffix.length() > str.length()) {
                return false;
            } else {
                int strOffset = str.length() - suffix.length();
                return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length());
            }
        } else {
            return str == null && suffix == null;
        }
    }

    public static String[] split(String str, String separatorChars) {
        List<String> strings = splitWorker(str, separatorChars, -1, false);
        return (String[])strings.toArray(new String[0]);
    }

    public static List<String> splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
        if (str == null) {
            return null;
        } else {
            int len = str.length();
            if (len == 0) {
                return Collections.emptyList();
            } else {
                List<String> list = new ArrayList();
                int sizePlus1 = 1;
                int i = 0;
                int start = 0;
                boolean match = false;
                boolean lastMatch = false;
                if (separatorChars != null) {
                    if (separatorChars.length() != 1) {
                        label87:
                        while(true) {
                            while(true) {
                                if (i >= len) {
                                    break label87;
                                }

                                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                                    if (match || preserveAllTokens) {
                                        lastMatch = true;
                                        if (sizePlus1++ == max) {
                                            i = len;
                                            lastMatch = false;
                                        }

                                        list.add(str.substring(start, i));
                                        match = false;
                                    }

                                    ++i;
                                    start = i;
                                } else {
                                    lastMatch = false;
                                    match = true;
                                    ++i;
                                }
                            }
                        }
                    } else {
                        char sep = separatorChars.charAt(0);

                        label71:
                        while(true) {
                            while(true) {
                                if (i >= len) {
                                    break label71;
                                }

                                if (str.charAt(i) == sep) {
                                    if (match || preserveAllTokens) {
                                        lastMatch = true;
                                        if (sizePlus1++ == max) {
                                            i = len;
                                            lastMatch = false;
                                        }

                                        list.add(str.substring(start, i));
                                        match = false;
                                    }

                                    ++i;
                                    start = i;
                                } else {
                                    lastMatch = false;
                                    match = true;
                                    ++i;
                                }
                            }
                        }
                    }
                } else {
                    label103:
                    while(true) {
                        while(true) {
                            if (i >= len) {
                                break label103;
                            }

                            if (Character.isWhitespace(str.charAt(i))) {
                                if (match || preserveAllTokens) {
                                    lastMatch = true;
                                    if (sizePlus1++ == max) {
                                        i = len;
                                        lastMatch = false;
                                    }

                                    list.add(str.substring(start, i));
                                    match = false;
                                }

                                ++i;
                                start = i;
                            } else {
                                lastMatch = false;
                                match = true;
                                ++i;
                            }
                        }
                    }
                }

                if (match || preserveAllTokens && lastMatch) {
                    list.add(str.substring(start, i));
                }

                return list;
            }
        }
    }

    public static boolean isCharSequence(Class<?> clazz) {
        return clazz != null && CharSequence.class.isAssignableFrom(clazz);
    }

    public static String removeIsPrefixIfBoolean(String propertyName, Class<?> propertyType) {
        if (isBoolean(propertyType) && propertyName.startsWith("is")) {
            String property = propertyName.replaceFirst("is", "");
            if (isEmpty(property)) {
                return propertyName;
            } else {
                String firstCharToLowerStr = firstCharToLower(property);
                return property.equals(firstCharToLowerStr) ? propertyName : firstCharToLowerStr;
            }
        } else {
            return propertyName;
        }
    }

    public static boolean isBoolean(Class<?> propertyCls) {
        return propertyCls != null && (Boolean.TYPE.isAssignableFrom(propertyCls) || Boolean.class.isAssignableFrom(propertyCls));
    }

    public static String firstCharToLower(String rawString) {
        return prefixToLower(rawString, 1);
    }

    public static String prefixToLower(String rawString, int index) {
        String beforeChar = rawString.substring(0, index).toLowerCase();
        String afterChar = rawString.substring(index);
        return beforeChar + afterChar;
    }

    public static String removePrefixAfterPrefixToLower(String rawString, int index) {
        return prefixToLower(rawString.substring(index), 1);
    }

    public static String camelToHyphen(String input) {
        return wordsToHyphenCase(wordsAndHyphenAndCamelToConstantCase(input));
    }

    private static String wordsAndHyphenAndCamelToConstantCase(String input) {
        boolean betweenUpperCases = false;
        boolean containsLowerCase = containsLowerCase(input);
        StringBuilder buf = new StringBuilder();
        char previousChar = ' ';
        char[] chars = input.toCharArray();

        for(int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            boolean isUpperCaseAndPreviousIsUpperCase = Character.isUpperCase(previousChar) && Character.isUpperCase(c);
            boolean isUpperCaseAndPreviousIsLowerCase = Character.isLowerCase(previousChar) && Character.isUpperCase(c);
            boolean previousIsWhitespace = Character.isWhitespace(previousChar);
            boolean lastOneIsNotUnderscore = buf.length() > 0 && buf.charAt(buf.length() - 1) != '_';
            boolean isNotUnderscore = c != '_';
            if (!lastOneIsNotUnderscore || !isUpperCaseAndPreviousIsLowerCase && !previousIsWhitespace && (!betweenUpperCases || !containsLowerCase || !isUpperCaseAndPreviousIsUpperCase)) {
                if (Character.isDigit(previousChar) && Character.isLetter(c)) {
                    buf.append('_');
                }
            } else {
                buf.append("_");
            }

            if (shouldReplace(c) && lastOneIsNotUnderscore) {
                buf.append('_');
            } else if (!Character.isWhitespace(c) && (isNotUnderscore || lastOneIsNotUnderscore)) {
                buf.append(Character.toUpperCase(c));
            }

            previousChar = c;
        }

        if (Character.isWhitespace(previousChar)) {
            buf.append("_");
        }

        return buf.toString();
    }

    public static boolean containsLowerCase(String s) {
        char[] var1 = s.toCharArray();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            char c = var1[var3];
            if (Character.isLowerCase(c)) {
                return true;
            }
        }

        return false;
    }

    private static boolean shouldReplace(char c) {
        return c == '.' || c == '_' || c == '-';
    }

    private static String wordsToHyphenCase(String s) {
        StringBuilder buf = new StringBuilder();
        char lastChar = 'a';
        char[] var3 = s.toCharArray();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            char c = var3[var5];
            if (Character.isWhitespace(lastChar) && !Character.isWhitespace(c) && '-' != c && buf.length() > 0 && buf.charAt(buf.length() - 1) != '-') {
                buf.append("-");
            }

            if ('_' == c) {
                buf.append('-');
            } else if ('.' == c) {
                buf.append('-');
            } else if (!Character.isWhitespace(c)) {
                buf.append(Character.toLowerCase(c));
            }

            lastChar = c;
        }

        if (Character.isWhitespace(lastChar)) {
            buf.append("-");
        }

        return buf.toString();
    }

    public static String removeWordWithComma(String s, String p) {
        String match = "\\s*" + p + "\\s*,{0,1}";
        return s.replaceAll(match, "");
    }
}
