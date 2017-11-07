package com.saha.slnarch.common.helper;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringHelper {

  private static Logger logger = LoggerFactory.getLogger(StringHelper.class);

  private StringHelper() {

  }

  public static boolean isEmpty(String value) {
    return Strings.isNullOrEmpty(value);
  }

  public static int convertStringToInt(String value) {
    int converted = 0;
    if (!isEmpty(value)) {
      try {
        converted = Integer.parseInt(value);
      } catch (Exception e) {
        logger.error("String is not number", e);
      }
    }
    return converted;
  }

  public static String convertIntToString(int value) {
    String converted = "";
    try {
      converted = String.valueOf(value);
    } catch (Exception e) {
      logger.error("Int Convert String Error", e);
    }
    return converted;
  }
}
