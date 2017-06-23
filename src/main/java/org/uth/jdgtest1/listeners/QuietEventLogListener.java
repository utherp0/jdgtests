package org.uth.jdgtest1.listeners;

import org.infinispan.client.hotrod.annotation.*;
import org.infinispan.client.hotrod.event.*;

@ClientListener
public class QuietEventLogListener
{
  @ClientCacheEntryCreated
  @ClientCacheEntryModified
  @ClientCacheEntryRemoved
  public void handleRemoteEvent( ClientEvent event )
  {
    //System.out.println( event );
  }
}