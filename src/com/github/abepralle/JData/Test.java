package com.github.abepralle.JData;

import java.io.*;

/*
😄
grinning face with squinting eyes
Unicode: U+1F604, UTF-8: F0 9F 98 84
*/

public class Test
{
  static public void main( String[] args )
  {
    JData.Value n = JData.number( 5 );
    System.out.println( n );
    n = JData.string( "Testing" );
    System.out.println( n );
    n = JData.logical( true );
    System.out.println( n );
    n = JData.logical( false );
    System.out.println( n );
    n = JData.table();
    n.set( "One", 1 );
    System.out.println( n );
    System.out.println( n.get("One") );
    System.out.println( n.get("Two") );

    /*
    System.out.println( "JData Test" );

    try
    {
      String st = "";
      InputStreamReader reader = new InputStreamReader( new FileInputStream("Test.txt") );
      int ch = reader.read();
      while (ch != -1)
      {
        //System.out.println( ch );
        st += (char) ch;
        ch = reader.read();
      }
      System.out.println( st );
      System.out.println( st.length() );
    }
    catch (IOException e)
    {
      System.out.println( e );
    }
    */
  }
}

