package org.uth.jdgtest1.listeners;

import org.uth.jdgtest1.utils.DateFormatter;
import org.infinispan.client.hotrod.annotation.*;
import org.infinispan.client.hotrod.event.*;

@ClientListener
public class ExpirationEventLogListener
{
  @ClientCacheEntryCreated
  //@ClientCacheEntryModified  
  @ClientCacheEntryRemoved
  public void handleRemoteEvent( ClientEvent event )
  {
    System.out.println( "[TechTalk - ExpirationTest] " + DateFormatter.timeFormat(System.currentTimeMillis() ) + " " + event );
  }
}