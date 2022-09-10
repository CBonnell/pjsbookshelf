package org.tomokiyo.pjs.server;

import java.util.*;
import javax.xml.parsers.SAXParser;

import com.google.gson.Gson;

import junit.framework.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * JUnit test routine for {@link YahooFuriganaService}.
 *
 * @author Takashi Tomokiyo (tomokiyo@gmail.com)
 */
public class TestYahooFuriganaService extends TestCase
{
  /** Creates an instance of the test */
  public TestYahooFuriganaService(String name) {
    super(name);
  }

  public void testContentHandler() throws Exception {
    final InputStream inputStream = OpenDBLookupServiceImpl.class.getResourceAsStream("tests/YahooFuriganaResponse001.json");

    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    final byte[] buffer = new byte[1024];

    for (int len; (len = inputStream.read(buffer)) != -1; ) {
      baos.write(buffer, 0, len);
    }

    final String responseBody = baos.toString(StandardCharsets.UTF_8.name());

    // Parse the test data.
    final YahooFuriganaService.Response response = new Gson().fromJson(responseBody, YahooFuriganaService.Response.class);

    assertEquals("はっとりがはりまちがえたデータにたいしてメッセージをしゅつりょくする", response.getResult().toString());
  }

  /**
   * test YahooFuriganaService on the fly.
   */
  public void testYahooFuriganaService() {
    final YahooFuriganaService converter = new YahooFuriganaService();
    // assertEquals("グリトグラノエンソク (コドモノトモケッサクシュウ―グリトグラノエホン)", converter.toKatakana("ぐりとぐらのえんそく (こどものとも傑作集―ぐりとぐらの絵本)"));
    // assertEquals("NTTガ、ツウシンリョウキンヲ、ヤスクスル", converter.toKatakana("NTTが、通信料金を、安くする"));
    // assertEquals("じょん・ぐりしゃむ", converter.toHiragana("ジョン・グリシャム"));
  }

  /**
   * common setup
   */
  public static Test suite() {
    return new TestSuite( TestYahooFuriganaService.class );
  }

  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }
}
