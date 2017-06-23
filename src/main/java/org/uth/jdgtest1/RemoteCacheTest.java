package org.uth.jdgtest1;
 
import java.util.Random;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.ConfigurationProperties;
 
public class RemoteCacheTest 
{
  private static Random _random = new Random();
  
  public static void main(String[] args) 
  {
    if( args.length != 3 )
    {
      System.out.println( "Usage: java RemoteCacheTest targetHost fetchCount fetchKeyMax" );
      System.exit(0);
    }
    
    int fetchCount = Integer.parseInt( args[1] );
    int fetchMax = Integer.parseInt( args[2] );
    
    // Create a configuration for a locally-running server
    ConfigurationBuilder builder = new ConfigurationBuilder();
    builder.addServer().host(args[0]).port(ConfigurationProperties.DEFAULT_HOTROD_PORT);
      
    // Connect to the server
    RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());
      
    // Obtain the remote cache
    RemoteCache<String, String> cache = cacheManager.getCache();
      
    /// Assume Fill Test has run
    long start = System.currentTimeMillis();
    
    for( int loop = 0; loop < fetchCount; loop++ )
    {
      int target = _random.nextInt(fetchMax);
      String output = cache.get( Integer.toString(target));
      
      RemoteCacheTest.log("Sought " + target + ", received " + output );
    }
    
    long end = System.currentTimeMillis();

    RemoteCacheTest.log( "Sought complete in " + ( end - start ) + "ms.");
    
    // Stop the cache manager and release all resources
    cacheManager.stop();
  }
  
  private static void log( String message )
  {
    System.out.println( "[Tech-Talk - Remote Cache Fetch] " + message );
  }    
}