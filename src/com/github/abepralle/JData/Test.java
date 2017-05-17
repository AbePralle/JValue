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
    JData.Value list = JData.list();
    System.out.println( list );

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

