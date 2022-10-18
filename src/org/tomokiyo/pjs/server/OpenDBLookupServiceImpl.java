package org.tomokiyo.pjs.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Objects;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tomokiyo.pjs.client.PublicBookInfo;
import org.tomokiyo.pjs.client.PublicBookInfoLookupService;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Lookup book information via OpenDB API.
 *
 * @author Koichiro Niinuma
 */
@SuppressWarnings("serial")
public class OpenDBLookupServiceImpl extends RemoteServiceServlet implements PublicBookInfoLookupService {

  //---------------------------------------------------
  // https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
  //---------------------------------------------------
  private static String readAll(Reader rd) throws IOException {
      StringBuilder sb = new StringBuilder();
      int cp;
      while ((cp = rd.read()) != -1) {
        sb.append((char) cp);
      }
      return sb.toString();
    }
  
  private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
      InputStream is = new URL(url).openStream();
      try {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String jsonText = readAll(rd);
        JSONArray jsonarray = new JSONArray(jsonText);
        return jsonarray.isNull(0) ? null : jsonarray.getJSONObject(0);
      } finally {
        is.close();
      }
    }
  
  public PublicBookInfo lookupByISBN(final String isbn) {
      final PublicBookInfo bookInfo = new PublicBookInfo();
      final String url = "https://api.openbd.jp/v1/get?isbn=" + isbn;
      System.out.println("Query: "+url);
      try {
        JSONObject json = readJsonFromUrl(url);

        if (Objects.isNull(json)) {
          return null;
        }

        bookInfo.setISBN(isbn);      
        bookInfo.setEAN(isbn); // Client側で、ISBNとしてEANを参照しているように見える
        bookInfo.setTitle(((JSONObject)json.get("summary")).get("title").toString());
        bookInfo.setPublisher(((JSONObject)json.get("summary")).get("publisher").toString());
        bookInfo.setMediumImageURL(((JSONObject)json.get("summary")).get("cover").toString());
        JSONArray contributors = (JSONArray)((JSONObject)((JSONObject)json.get("onix")).get("DescriptiveDetail")).get("Contributor");
        for (int i = 0; i < contributors.length(); ++i) {
          bookInfo.addAuthor(((JSONObject)(contributors.getJSONObject(i).get("PersonName"))).get("content").toString());
        }
      } catch (JSONException ex) {
        throw new IllegalStateException(ex);
      } catch (IOException ex) {
        throw new IllegalStateException(ex);
      }
      return bookInfo;
    }

  /**
   * Create a SAXParser instance.
   *
   * @return a <code>SAXParser</code> value
   */
  static protected final SAXParser createSAXParser() {
    try {    
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setFeature("http://xml.org/sax/features/namespaces", false);
      spf.setFeature("http://xml.org/sax/features/validation", false);
      return spf.newSAXParser();
    } catch (javax.xml.parsers.ParserConfigurationException ex) {
      throw new IllegalStateException(ex);
    } catch (org.xml.sax.SAXException ex) {
      throw new IllegalStateException(ex);
    }
  }

  /**
   * A ContentHandler to produce a list of PublicBookInfo objects.
   */
  static final class MyContentHandler extends DefaultHandler implements ContentHandler {
    private final ArrayList<PublicBookInfo> accum;
    private final StringBuilder sbuf = new StringBuilder();

    transient private PublicBookInfo bookInfo;
    transient private String imageURL;

    public MyContentHandler(ArrayList<PublicBookInfo> l) {
      accum = l;
    }

    public void startElement(String uri, String localpart, String rawname, Attributes attributes) {
      if ("Item".equals(rawname)) {
        bookInfo = new PublicBookInfo();
        accum.add(bookInfo);
      } else if ("Author".equals(rawname)
          || "Title".equals(rawname)
          || "Publisher".equals(rawname)
          || "EAN".equals(rawname)
          || "ISBN".equals(rawname)
          || "URL".equals(rawname)
          || "Subject".equals(rawname)
        ) {
        sbuf.setLength(0);
      }
    }

    public void characters(char[] ch, int offset, int length) {
      if (length > 0) sbuf.append(ch, offset, length);
    }

    public void endElement(String uri, String localpart, String rawname) {
      if ("Author".equals(rawname)) {
        bookInfo.addAuthor(sbuf.toString());
      } else if ("Title".equals(rawname)) {
        bookInfo.setTitle(sbuf.toString());
      } else if ("Subject".equals(rawname)) {
        bookInfo.addCategory(sbuf.toString());
      } else if ("Publisher".equals(rawname)) {
        bookInfo.setPublisher(sbuf.toString());
      } else if ("EAN".equals(rawname)) {
        bookInfo.setEAN(sbuf.toString());
      } else if ("ISBN".equals(rawname)) {
        bookInfo.setISBN(sbuf.toString());
      } else if ("URL".equals(rawname)) {
        imageURL = sbuf.toString();
      } else if ("SmallImage".equals(rawname)) {
        bookInfo.setSmallImageURL(imageURL);
      } else if ("MediumImage".equals(rawname)) {
        bookInfo.setMediumImageURL(imageURL);
      } else if ("LargeImage".equals(rawname)) {
        bookInfo.setLargeImageURL(imageURL);
      }
    }
  }
}
