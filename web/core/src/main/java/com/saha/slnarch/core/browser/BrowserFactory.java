package com.saha.slnarch.core.browser;

import com.saha.slnarch.common.exception.SlnException;
import com.saha.slnarch.common.helper.StringHelper;
import com.saha.slnarch.common.helper.SystemPropertyHelper;
import com.saha.slnarch.core.browser.local.ChromeBrowser;
import com.saha.slnarch.core.browser.local.FirefoxBrowser;
import com.saha.slnarch.core.browser.local.SafariBrowser;
import com.saha.slnarch.core.browser.remote.JenkinsBrowser;
import com.saha.slnarch.core.browser.remote.RemoteBrowser;
import com.saha.slnarch.core.browser.remote.TestiniumBrowser;
import com.saha.slnarch.core.model.Configuration;
import java.net.MalformedURLException;
import javax.annotation.Nullable;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;

public class BrowserFactory {

  public WebDriver getWebDriver(@Nullable Capabilities capabilities, @Nullable Proxy proxy,
      Configuration configuration)
      throws MalformedURLException {
    Browser browser;
    if (!StringHelper.isEmpty(SystemPropertyHelper.getTestiniumKey())) {
      browser = new TestiniumBrowser(configuration);
    } else {
      if (configuration.getBrowserType().equalsIgnoreCase("Chrome")) {
        browser = new ChromeBrowser(configuration);
      } else if (configuration.getBrowserType().equalsIgnoreCase("Firefox")) {
        browser = new FirefoxBrowser(configuration);
      } else if (configuration.getBrowserType().equalsIgnoreCase("Safari")) {
        browser = new SafariBrowser(configuration);
      } else if (configuration.getBrowserType().equalsIgnoreCase("Remote")) {
        browser = new RemoteBrowser(configuration);
      } else if (configuration.getBrowserType().equalsIgnoreCase("Jenkins")) {
        browser = new JenkinsBrowser(configuration);
      } else {
        throw new SlnException("Browser Type Not Found");
      }
    }
    return browser.buildWebDriver(capabilities, proxy);
  }

}