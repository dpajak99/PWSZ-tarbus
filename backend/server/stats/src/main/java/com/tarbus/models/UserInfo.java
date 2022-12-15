package com.tarbus.models;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class UserInfo {
  public String referer;
  public String fullURL;
  public String clientIpAddr;
  public String clientOS;
  public String clientBrowser;
  public String userAgent;

  public UserInfo(HttpServletRequest request) {
    referer = getReferer(request);
    fullURL = getFullURL(request);
    clientIpAddr = getClientIpAddr(request);
    clientOS = getClientOS(request);
    clientBrowser = getClientBrowser(request);
    userAgent = getUserAgent(request);
  }

  public Map<String, Object> getClientInfo() {
    Map<String, Object> result = new HashMap();

    result.put("User Agent", userAgent);
    result.put("Operating System", clientOS);
    result.put("Browser Name", clientBrowser);
    result.put("IP Address", clientIpAddr);
    result.put("Full URL", fullURL);
    result.put("Referrer", referer);
    return result;
  }

  public String getReferer(HttpServletRequest request) {
    final String referer = request.getHeader("referer");
    return referer;
  }

  public String getFullURL(HttpServletRequest request) {
    final StringBuffer requestURL = request.getRequestURL();
    final String queryString = request.getQueryString();

    final String result = queryString == null ? requestURL.toString() : requestURL.append('?')
      .append(queryString)
      .toString();

    return result;
  }

  //http://stackoverflow.com/a/18030465/1845894
  public String getClientIpAddr(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

  //http://stackoverflow.com/a/18030465/1845894
  public String getClientOS(HttpServletRequest request) {
    final String browserDetails = request.getHeader("User-Agent");

    //=================OS=======================
    final String lowerCaseBrowser = browserDetails.toLowerCase();
    if (lowerCaseBrowser.contains("windows")) {
      return "Windows";
    } else if (lowerCaseBrowser.contains("mac")) {
      return "Mac";
    } else if (lowerCaseBrowser.contains("x11")) {
      return "Linux";
    } else if (lowerCaseBrowser.contains("android")) {
      return "Android";
    } else if (lowerCaseBrowser.contains("iphone")) {
      return "IPhone";
    } else {
      return "UnKnown, More-Info: " + browserDetails;
    }
  }

  //http://stackoverflow.com/a/18030465/1845894
  public String getClientBrowser(HttpServletRequest request) {
    final String browserDetails = request.getHeader("User-Agent");
    final String user = browserDetails.toLowerCase();

    String browser = "";

    //===============Browser===========================
    if (user.contains("msie")) {
      String substring = browserDetails.substring(browserDetails.indexOf("MSIE")).split(";")[0];
      browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
    } else if (user.contains("safari") && user.contains("version")) {
      browser = (browserDetails.substring(browserDetails.indexOf("Safari")).split(" ")[0]).split(
        "/")[0] + "-" + (browserDetails.substring(
        browserDetails.indexOf("Version")).split(" ")[0]).split("/")[1];
    } else if (user.contains("opr") || user.contains("opera")) {
      if (user.contains("opera"))
        browser = (browserDetails.substring(browserDetails.indexOf("Opera")).split(" ")[0]).split(
          "/")[0] + "-" + (browserDetails.substring(
          browserDetails.indexOf("Version")).split(" ")[0]).split("/")[1];
      else if (user.contains("opr"))
        browser = ((browserDetails.substring(browserDetails.indexOf("OPR")).split(" ")[0]).replace("/",
          "-")).replace(
          "OPR", "Opera");
    } else if (user.contains("chrome")) {
      browser = (browserDetails.substring(browserDetails.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
    } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1) || (user.indexOf(
      "mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf(
      "mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1)) {
      //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
      browser = "Netscape-?";

    } else if (user.contains("firefox")) {
      browser = (browserDetails.substring(browserDetails.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
    } else if (user.contains("rv")) {
      browser = "IE";
    } else {
      browser = "UnKnown, More-Info: " + browserDetails;
    }

    return browser;
  }

  public String getUserAgent(HttpServletRequest request) {
    return request.getHeader("User-Agent");
  }
}
