package com.saha.slnarch.core.element.by;

public class ByFactory {

  public static ByCreate buildBy(ByType byType) {
    if (byType == ByType.CSS) {
      return new ByCss();
    } else if (byType == ByType.XPATH) {
      return new ByXpath();
    } else if (byType == ByType.NAME) {
      return new ByName();
    } else if (byType == ByType.TAG) {
      return new ByTag();
    } else if (byType == ByType.CLASS) {
      return new ByClass();
    } else if (byType == ByType.ID) {
      return new ById();
    } else {
      return null;
    }
  }
}
