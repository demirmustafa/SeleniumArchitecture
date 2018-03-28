package com.saha.slnarch.common.helper;

import com.saha.slnarch.common.log.LogHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;

public class RegexHelper {
  private static Logger logger= LogHelper.getSlnLogger();

  private static final String ONLY_NUMBER = "\\d+";

  private RegexHelper() {
  }

  public static int getStringInOnlyNumber(String text) {
    return getMatcherListByInt(getMatcher(getPattern(ONLY_NUMBER), text)).stream().findFirst()
        .orElse(0);
  }

  public static List<Integer> getStringInOnlyNumbers(String text) {
    return getMatcherListByInt(getMatcher(getPattern(ONLY_NUMBER), text));
  }

  private static List<Integer> getMatcherListByInt(Matcher matcher) {
    List<Integer> numbers = new ArrayList<>();
    try {
      while (matcher.find()) {
        for (int i = 0; i <= matcher.groupCount(); i++) {
          numbers.add(Integer.parseInt(matcher.group(i)));
        }
      }
    } catch (Exception e) {
      logger.error("Regex Error", e);
    }
    return numbers;
  }

  private static Pattern getPattern(String regex) {
    return Pattern.compile(regex);
  }

  private static Matcher getMatcher(Pattern pattern, String text) {
    return pattern.matcher(text);
  }
}