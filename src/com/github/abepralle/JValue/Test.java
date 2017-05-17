package com.github.abepralle.JValue;

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
    JValue list = JValue.list();
    list.add( (JValue.TableValue) null ).add( 3 ).add( "Four" ).add( true ).add( false ).add( JValue.table() );
    System.out.println( list );
    System.out.println( list.get(0).isNull() );

    System.out.println( new File("src/com/github/abepralle/JValue/JValue.java").getPath() );

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

