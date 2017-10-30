package com.saha.slnarch.core.element.by;

import org.openqa.selenium.By;

public final class ByXpath implements ByCreate<By> {

  @Override
  public By createBy(String name) {
    return By.xpath(name);
  }
}
