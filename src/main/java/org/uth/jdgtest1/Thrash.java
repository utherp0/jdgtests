package org.uth.jdgtest1;

import org.uth.jdgtest1.listeners.QuietEventLogListener;
import java.util.Random;
import org.infinispan.client.hotrod.*;

public class Thrash
{
  private static Random random = new Random();
  
  public static void main( String[] args )
  {
    if( args.length != 3 )
    {
      System.out.println( "Usage: java Thrash entryCount dataLength lookupCount");
      System.exit(0);
    }
    
    try
    {
      int entryCount = Integer.parseInt( args[0] );
      int dataLength = Integer.parseInt( args[1] );
      int lookupCount = Integer.parseInt( args[2] );
      
      RemoteCacheManager rcm = new RemoteCacheManager();
      RemoteCache<Integer,String> cache = rcm.getCache();
      QuietEventLogListener listener = new QuietEventLogListener();

      try
      {
        cache.addClientListener( listener );

        Thrash.log( "Adding " + entryCount + " entries of length " + dataLength + " to local cache (default Xmx/Xms)...");
      
        long start = System.currentTimeMillis();
        for( int loop = 0; loop < entryCount; loop++ )
        {
          cache.put( loop, Thrash.generateData(dataLength) );
        }
        long end = System.currentTimeMillis();
      
        Thrash.log( "Added " + entryCount + " items in " + ( end - start) + "ms." );
      
        Thrash.log( "Cache thinks it has " + cache.size() + " entries." );
        Thrash.log( "Performing " + lookupCount + " random lookups...");
        
        start = System.currentTimeMillis();        
        for( int loop = 0; loop < lookupCount; loop ++ )
        {
          int seek = random.nextInt(entryCount);
          
          Thrash.log( "Seeking " + seek + " finds " + cache.get(seek));
        }
        end = System.currentTimeMillis();
        
        Thrash.log( "Sought " + lookupCount + " entries in " + ( end - start ) + "ms.");        
      }
      finally
      {
        cache.removeClientListener( listener );
      
        Thrash.log( "Clearing down cache (removing " + entryCount + " entries)");
      
        long start = System.currentTimeMillis();
        cache.clear();
        long end = System.currentTimeMillis();
      
        Thrash.log( "Cache cleared in " + ( end - start ) + "ms.");
      }
    }
    catch( NumberFormatException exc )
    {
      Thrash.log( "Both parameters *must* be numerical...");
    }
  }

  private static String generateData( int dataSize )
  {
    String chars = "abcdefghijklmnopqrstuvwxyz";
    
    StringBuilder data = new StringBuilder();
    
    for( int loop = 0; loop < dataSize; loop++ )
    {
      data.append( chars.charAt( random.nextInt(25) ));
    }
    
    return data.toString();
  }
  
  private static void log( String message )
  {
    System.out.println( "[Thrash] " + message );
  }
}