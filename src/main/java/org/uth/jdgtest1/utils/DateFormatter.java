package org.uth.jdgtest1.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple consistent date formatter utility.
 * @author Ian Lawson
 */
public class DateFormatter
{
  private DateFormatter()
  {
  }
  
  public static String longDateFormat( long msDate )
  {
    Date date = new Date( msDate );
    SimpleDateFormat formatter = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss");
    
    return formatter.format( date );
  }
  
  public static String timeFormat( long msDate )
  {
    Date date = new Date( msDate );
    SimpleDateFormat formatter = new SimpleDateFormat( "HH:mm:ss" );
    
    return formatter.format( date );
  }
}