package org.uth.jdgtest1;

import org.uth.jdgtest1.listeners.ExpirationEventLogListener;
import java.util.concurrent.TimeUnit;
import org.infinispan.client.hotrod.*;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;

public class RemoteExpirationTest
{
  public static void main( String[] args )
  {
    if( args.length != 1 )
    {
      RemoteExpirationTest.log("Usage: RemoteExpirationTest targetHost");
      System.exit(0);
    }

    // Create a configuration for a locally-running server
    ConfigurationBuilder builder = new ConfigurationBuilder();
    builder.addServer().host(args[0]).port(ConfigurationProperties.DEFAULT_HOTROD_PORT);
      
    // Connect to the server
    RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());

    RemoteCache<Integer,String> cache = cacheManager.getCache();
    
    ExpirationEventLogListener listener = new ExpirationEventLogListener();

    try
    {
      cache.addClientListener( listener );

      RemoteExpirationTest.log( "Adding 20 entries with 10 seconds expiration...");
      
      long start = System.currentTimeMillis();
      for( int loop = 0; loop < 20; loop++ )
      {
        cache.put( loop, Integer.toString( loop ) );
        cache.put(loop, Integer.toString(loop), 10, TimeUnit.SECONDS);
      }
      long end = System.currentTimeMillis();
      
      RemoteExpirationTest.log( "Added 20 items in " + ( end - start) + "ms." );
      RemoteExpirationTest.log( "Cache thinks it has " + cache.size() + "entries." );
      
      for( int loop = 0; loop < 20; loop++ )
      {
        String fetch = cache.get(loop);
        RemoteExpirationTest.log( "Fetched " + loop + " and received " + fetch );
      }
      
      // Manual remove
      cache.remove(10);
      cache.remove(15);
      RemoteExpirationTest.log( "After manual remove cache thinks it has " + cache.size() + " entries." );
      
      try
      {
        Thread.sleep(11000);
      }
      catch( Exception exc )
      {
      }
      
      RemoteExpirationTest.log( "After 11 seconds cache thinks it has " + cache.size() + " entries." );
    }
    finally
    {
      cache.removeClientListener( listener );
      
      RemoteExpirationTest.log( "Clearing down cache (removing " + cache.size() + " entries)");
      
      long start = System.currentTimeMillis();
      cache.clear();
      long end = System.currentTimeMillis();
      
      RemoteExpirationTest.log( "Cache cleared in " + ( end - start ) + "ms.");
    }
  }

  private static void log( String message )
  {
    System.out.println( "[TechTalk - RemoteExpirationTest] " + message );
  }
}