package org.tomokiyo.pjs.server;

import org.tomokiyo.pjs.client.KakasiService;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.io.*;
import java.util.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.net.ProtocolException;

import com.google.gson.Gson;

public class YahooFuriganaService extends RemoteServiceServlet implements KakasiService {
  private final static String APP_ID = ServerUtil.properties.getProperty("yahoo.appid");
  private final static String ENDPOINT_URL_STRING = "https://jlp.yahooapis.jp/FuriganaService/V2/furigana";

  public String toKatakana(String text) {
    return JapaneseUtil.hiraganaToKatakana(toHiragana(text));
  }

  static class Result {
    private ArrayList<Word> word;

    public String toString() {
      final StringBuilder sb = new StringBuilder();
      for (final Word w : this.word) {
        sb.append(w);
      }

      return sb.toString();
    }
  }

  static class Response {
    private String id;
    private String jsonrpc;
    private Result result;

    public Response() { }

    public Result getResult() {
      return this.result;
    }
  }

  static class Word {
    private String furigana;
    private String surface;

    public Word() { }

    public String toString() {
      if (this.furigana != null) {
        return this.furigana;
      }

      if (this.surface != null) {
        if (StringUtil.isWhitespace(this.surface)) {
          return " ";
        }
        else {
          return this.surface;
        }
      }

      throw new IllegalStateException();
    }
  }

  private static String buildRequestBody(final String text) {
    final Map<String, Object> reqParams = new HashMap<String, Object>();

    // https://developer.yahoo.co.jp/webapi/jlp/furigana/v2/furigana.html に参照
    reqParams.put("id", "1"); // 使わなくて、任意の値
    reqParams.put("jsonrpc", "2.0");
    reqParams.put("method", "jlp.furiganaservice.furigana");
    
    final Map<String, Object> queryParams = new HashMap<String, Object>();
    queryParams.put("q", text);
    queryParams.put("grade", 1);

    reqParams.put("params", queryParams);

    return new Gson().toJson(reqParams);
  }

  private static String performRequest(final String requestBody) throws IOException {
    final HttpURLConnection con = (HttpURLConnection) new URL(YahooFuriganaService.ENDPOINT_URL_STRING).openConnection();
    con.setRequestMethod("POST");
    con.setDoOutput(true);

    final byte[] requestBodyOctets = requestBody.getBytes(StandardCharsets.UTF_8);
    con.setFixedLengthStreamingMode(requestBodyOctets.length);
    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    con.setRequestProperty("User-Agent", String.format("Yahoo AppID: %s", YahooFuriganaService.APP_ID));
    con.connect();

    final OutputStream os = con.getOutputStream();
    try {
      os.write(requestBodyOctets);
    }
    finally {
      os.close();
    }

    final InputStream is = con.getInputStream();
    try {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      final byte[] buffer = new byte[1024];

      for (int len; (len = is.read(buffer)) != -1; ) {
        baos.write(buffer, 0, len);
      }

      return baos.toString(StandardCharsets.UTF_8.name());
    }
    finally {
      is.close();
    }
  }

  private static String parseResponse(final String responseBody) {
    final Response parsedResponse = new Gson().fromJson(responseBody, Response.class);

    return parsedResponse.result.toString();
  }

  public String toHiragana(String text) {
    if (text == null) return null;
    text = JapaneseUtil.normalize(text);
    if (text.isEmpty()) return "";

    final String requestBody = YahooFuriganaService.buildRequestBody(text);
    System.out.println("Query: " + requestBody);

    String responseBody;
    try {
      responseBody = performRequest(requestBody);
    }
    catch (final IOException ex) {
      throw new IllegalStateException(ex);
    }

    System.out.println("Raw response: " + responseBody);

    final String parsedResponse = parseResponse(responseBody);
    System.out.println("Parsed response: " + parsedResponse);

    return parsedResponse;
  }
  
}
