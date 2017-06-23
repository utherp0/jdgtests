package org.uth.jdgtest1;
 
import java.util.Random;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;
import org.uth.jdgtest1.listeners.QuietEventLogListener;
 
public class RemoteCacheFill 
{
  private static Random random = new Random();
  
  public static void main( String[] args )
  {
    if( args.length != 4 )
    {
      System.out.println( "Usage: java RemoteCacheFill host entryCount dataLength pause");
      System.exit(0);
    }
    
    try
    {
      String targetHost = args[0];
      int entryCount = Integer.parseInt( args[1] );
      int dataLength = Integer.parseInt( args[2] );
      int pause = Integer.parseInt( args[3] );
      
      // Create a configuration for a locally-running server
      ConfigurationBuilder builder = new ConfigurationBuilder();
      builder.addServer().host(args[0]).port(ConfigurationProperties.DEFAULT_HOTROD_PORT);
      
      // Connect to the server
      RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());
      
      // Obtain the remote cache
      RemoteCache<String, String> cache = cacheManager.getCache();

      QuietEventLogListener listener = new QuietEventLogListener();

      try
      {
        cache.addClientListener( listener );

        RemoteCacheFill.log( "Adding " + entryCount + " entries of length " + dataLength + " to remote cache (" + targetHost + ")...");
      
        long start = System.currentTimeMillis();
        for( int loop = 0; loop < entryCount; loop++ )
        {
          cache.put( Integer.toString(loop), RemoteCacheFill.generateData(dataLength) );
        }
        long end = System.currentTimeMillis();
      
        RemoteCacheFill.log( "Added " + entryCount + " items in " + ( end - start) + "ms." );
      
        RemoteCacheFill.log( "Cache thinks it has " + cache.size() + " entries." );
        
        RemoteCacheFill.log( "Sleeping for " + pause + " seconds.");
        Thread.sleep( pause * 1000 );
      }
      catch( Exception exc )
      {
        // Interrupted Exception Catch
      }
      finally
      {
        cache.removeClientListener( listener );
      
        RemoteCacheFill.log( "Clearing down cache (removing " + entryCount + " entries)");
      
        long start = System.currentTimeMillis();
        cache.clear();
        long end = System.currentTimeMillis();
      
        RemoteCacheFill.log( "Cache cleared in " + ( end - start ) + "ms.");
        
        // Stop the cache manager and release all resources
        cacheManager.stop();
      }
    }
    catch( NumberFormatException exc )
    {
      RemoteCacheFill.log( "Both parameters *must* be numerical...");
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
    System.out.println( "[Tech-Talk - Remote Cache Fill] " + message );
  }  
}