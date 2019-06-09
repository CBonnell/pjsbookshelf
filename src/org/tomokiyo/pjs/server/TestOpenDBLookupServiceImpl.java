package org.tomokiyo.pjs.server;

import java.io.*;
import java.util.*;
import junit.framework.*;

import javax.xml.parsers.SAXParser;
import org.tomokiyo.pjs.client.PublicBookInfo;

/**
 * JUnit test routine for {@link OpenDBLookupServiceImpl}.
 *
 * @author Takashi Tomokiyo (tomokiyo@gmail.com)
 */
public class TestOpenDBLookupServiceImpl extends TestCase {
  /** Creates an instance of the test */
  public TestOpenDBLookupServiceImpl(String name) {
    super(name);
  }

  /**
   * Test the ContentHandler.
   */
  public void testContentHandler() throws Exception {
    final ArrayList<PublicBookInfo> accum = new ArrayList<PublicBookInfo>();
    final InputStream inputStream = OpenDBLookupServiceImpl.class.getResourceAsStream("tests/AmazonResponse001.xml");

    // Parse the test data.
    final SAXParser parser = OpenDBLookupServiceImpl.createSAXParser();
    parser.parse(inputStream, new OpenDBLookupServiceImpl.MyContentHandler(accum));
    assertEquals(1, accum.size());
    final PublicBookInfo info = accum.get(0);
    
    assertEquals("CDできくよみきかせおはなし絵本〈1〉", info.getTitle());
    assertEquals("成美堂出版", info.getPublisher());
    assertEquals("4415030629", info.getISBN());
    assertEquals("9784415030623", info.getEAN());
    assertEquals("http://ecx.images-amazon.com/images/I/61ZJ2T9NDPL._SL75_.jpg", info.getSmallImageURL());
    assertEquals("http://ecx.images-amazon.com/images/I/61ZJ2T9NDPL._SL160_.jpg", info.getMediumImageURL());
    assertEquals("http://ecx.images-amazon.com/images/I/61ZJ2T9NDPL.jpg", info.getLargeImageURL());

    HashSet<String> authorSet = new HashSet<String>(Arrays.asList(info.getAuthors()));
    assertEquals(2, authorSet.size());
    assertTrue(authorSet.contains("千葉 幹夫"));
    assertTrue(authorSet.contains("久保 純子"));
  }

  /**
   * common setup
   */
  public static Test suite() {
    return new TestSuite( TestOpenDBLookupServiceImpl.class );
  }

  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }
}
