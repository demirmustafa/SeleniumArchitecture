package com.saha.slnarch.demo;

import com.saha.slnarch.junit.InjectableJunitPageTestImpl;
import com.saha.slnarch.report.annotation.TestAuthor;
import com.saha.slnarch.report.annotation.TestCategory;
import org.junit.Test;

@TestAuthor(authors = "Ali Aktolun")
public class HomePageTest extends InjectableJunitPageTestImpl {

  @Test
  @TestCategory(categories = "Demo")
  public void pageTest() {
    HomePage homePage = new HomePage();
    homePage.pageTest();
  }

}