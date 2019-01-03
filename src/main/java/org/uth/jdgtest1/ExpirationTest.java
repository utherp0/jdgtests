package org.uth.jdgtest1;

import org.uth.jdgtest1.listeners.ExpirationEventLogListener;
import java.util.concurrent.TimeUnit;
import org.infinispan.client.hotrod.*;

public class ExpirationTest
{
  public static void main( String[] args )
  {
    RemoteCacheManager rcm = new RemoteCacheManager();
    RemoteCache<Integer,String> cache = rcm.getCache();
    //rcm.start();
    
    ExpirationEventLogListener listener = new ExpirationEventLogListener();

    try
    {
      cache.addClientListener( listener );

      ExpirationTest.log( "Adding 20 entries with 10 seconds expiration...");
      
      long start = System.currentTimeMillis();
      for( int loop = 0; loop < 20; loop++ )
      {
        cache.put( loop, Integer.toString( loop ) );
        cache.put(loop, Integer.toString(loop), 10, TimeUnit.SECONDS);
      }
      long end = System.currentTimeMillis();
      
      ExpirationTest.log( "Added 20 items in " + ( end - start) + "ms." );
      ExpirationTest.log( "Cache thinks it has " + cache.size() + "entries." );
      
      for( int loop = 0; loop < 20; loop++ )
      {
        String fetch = cache.get(loop);
        ExpirationTest.log( "Fetched " + loop + " and received " + fetch );
      }
      
      // Manual remove
      cache.remove(10);
      cache.remove(15);
      
      try
      {
        Thread.sleep(11000);
      }
      catch( Exception exc )
      {
      }
      
      ExpirationTest.log( "After 11 seconds cache thinks it has " + cache.size() + " entries." );
      
      //rcm.stop();
    }
    finally
    {
      cache.removeClientListener( listener );
      
      ExpirationTest.log( "Clearing down cache (removing " + cache.size() + " entries)");
      
      long start = System.currentTimeMillis();
      cache.clear();
      long end = System.currentTimeMillis();
      
      ExpirationTest.log( "Cache cleared in " + ( end - start ) + "ms.");
    }
  }

  private static void log( String message )
  {
    System.out.println( "[ExpirationTest] " + message );
  }
}