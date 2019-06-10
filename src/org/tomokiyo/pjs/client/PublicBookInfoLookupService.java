package org.tomokiyo.pjs.client;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Service for looking up public book DB for retrieving book title etc given ISBN.
 *
 * @author Takashi Tomokiyo (tomokiyo@gmail.com)
 */
public interface PublicBookInfoLookupService extends RemoteService {
  /**
   * Describe <code>lookupByISBN</code> method here.
   *
   * @param isbn a <code>String</code> value
   * @return an <code>PublicBookInfo</code> value
   */
  public PublicBookInfo lookupByISBN(String isbn);
}
