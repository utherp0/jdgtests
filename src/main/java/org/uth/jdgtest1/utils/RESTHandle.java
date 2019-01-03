package org.uth.jdgtest1.utils;

import java.io.*;
import java.net.*;

public class RESTHandle
{
  public int put(String urlServerAddress, String value) throws IOException 
  {
    URL address = new URL(urlServerAddress); 
    HttpURLConnection connection = (HttpURLConnection)
    address.openConnection();

    connection.setRequestMethod("PUT"); 
    connection.setRequestProperty("Content-Type", "text/plain"); 
    connection.setDoOutput(true);
    
    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
    outputStreamWriter.write(value);
    connection.connect(); 
    outputStreamWriter.flush();

    int responseCode = connection.getResponseCode();

    connection.disconnect();

    return responseCode;
  }  

  public String get(String urlServerAddress) throws IOException 
  { 
    String line = new String();
    StringBuilder stringBuilder = new StringBuilder();

    URL address = new URL(urlServerAddress); 
    HttpURLConnection connection = (HttpURLConnection) address.openConnection();
    connection.setRequestMethod("GET"); 
    connection.setRequestProperty("Content-Type", "text/plain"); 
    connection.setDoOutput(true);
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    connection.connect();
    
    while ((line = bufferedReader.readLine()) != null) 
    { 
      stringBuilder.append(line + '\n');
    }

    connection.disconnect();
    return stringBuilder.toString(); 
  }
  
  public int delete( String urlServerAddress, String value ) throws IOException
  {
    URL address = new URL(urlServerAddress); 
    HttpURLConnection connection = (HttpURLConnection)
    address.openConnection();

    connection.setRequestMethod("DELETE"); 
    connection.setRequestProperty("Content-Type", "text/plain"); 
    connection.setDoOutput(true);
    
    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
    outputStreamWriter.write(value);
    connection.connect(); 
    outputStreamWriter.flush();

    int responseCode = connection.getResponseCode();

    connection.disconnect();

    return responseCode;
  }
  
  public static void main( String args[] )
  {
    if( args.length !=2 )
    {
      System.out.println( "Usage: java RESTHandle datagridURL exampleData");
      System.exit(0);
    }

    RESTHandle handle = new RESTHandle();

    String urlServerAddress = args[0];
    String value = args[1];

    try
    {
      long start = 0;
      long end = 0;
      int responseCode = 0;
      String responseData = null;

      System.out.println( "Adding data to grid...");

      start = System.currentTimeMillis();
      responseCode = handle.put(urlServerAddress, value);
      end = System.currentTimeMillis();

      System.out.println( "   Response code: " + responseCode + " in " + ( end - start ) + "ms." );

      System.out.println( "Pulling data from grid...");

      start = System.currentTimeMillis();
      responseData = handle.get(urlServerAddress);
      end = System.currentTimeMillis();

      System.out.println( "   Data received: " + responseData + " in " + ( end - start ) + "ms." );

      System.out.println( "Removing data from grid...");

      start = System.currentTimeMillis();
      responseCode = handle.delete(urlServerAddress, value);
      end = System.currentTimeMillis();

      System.out.println( "   Response code: " + responseCode + " in " + ( end - start ) + "ms." );
    }
    catch( IOException exc )
    {
      System.out.println( "Exception thrown during test - " + exc.toString());
    }
  }
}