package org.tomokiyo.pjs.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Bindings for RPC services.
 */
class RPCServices {

  // private constructor to prevent from being instantiated.
  private RPCServices() {}
  
  static public final KakasiServiceAsync getKakasiService() {
    return LazyKakasiServiceHolder.service;
  }
  
  // Initialize static variable lazily and safely without DCL idiom.
  static private final class LazyKakasiServiceHolder {
    static private final KakasiServiceAsync service =
      (KakasiServiceAsync)GWT.create(KakasiService.class);
    static {
      ((ServiceDefTarget)service).setServiceEntryPoint(
        GWT.getModuleBaseURL() + "kakasiService");
    }
  }

  static public final DBLookupServiceAsync getDBLookupService() {
    return LazyDBLookupServiceHolder.service;
  }
  
  // Initialize static variable lazily and safely without DCL idiom.
  static private final class LazyDBLookupServiceHolder {
    static private final DBLookupServiceAsync service =
      (DBLookupServiceAsync)GWT.create(DBLookupService.class);
    static {
      ((ServiceDefTarget)service).setServiceEntryPoint(
        GWT.getModuleBaseURL() + "dbLookupService");
    }
  }

  static public final PublicBookInfoLookupServiceAsync getOpenDBLookupService() {
    return LazyOpenDBLookupServiceHolder.service;
  }
  
  // Initialize static variable lazily and safely without DCL idiom.
  static private final class LazyOpenDBLookupServiceHolder {
    static private final PublicBookInfoLookupServiceAsync service =
      (PublicBookInfoLookupServiceAsync)GWT.create(PublicBookInfoLookupService.class);
    static {
      ((ServiceDefTarget)service).setServiceEntryPoint(
        GWT.getModuleBaseURL() + "openDBRPCService");
    }
  }
}
