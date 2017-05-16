package com.github.abepralle.JData;

import java.io.*;
import java.util.*;

public class JData
{
  // GLOBAL METHODS
  static public Value logical( boolean value )
  {
    return value ? LogicalValue.true_value : LogicalValue.false_value;
  }

  static public Value number( double value )
  {
    return new NumberValue( value );
  }

  static public Value null_value()
  {
    return NullValue.singleton;
  }

  static public StringValue string( String value )
  {
    return new StringValue( value );
  }

  static public TableValue table()
  {
    return new TableValue();
  }

  // INNER CLASSES
  static public abstract class Value
  {
    // METHODS
    public boolean contains( String key )
    {
      return false;
    }

    public boolean contains( Value value )
    {
      return contains( value.toString() );
    }

    public int count()
    {
      return 0;
    }

    public boolean equals( Object other )
    {
      if (other == null) return this.isNull();

      if ( !(other instanceof Value) ) return false;

      return equals( (Value) other );
    }

    public boolean equals( Value other )
    {
      if (other == null) return false;
      return other.toString().equals( this.toString() );
    }

    public Value get( String key )
    {
      return NullValue.singleton;
    }

    public boolean isNonNull()
    {
      return this != NullValue.singleton;
    }

    public boolean isNull()
    {
      return this == NullValue.singleton;
    }

    public Value set( String key, double value )
    {
      // No action
      return this;
    }

    public double toDouble()
    {
      return 0.0;
    }

    public int toInt()
    {
      return (int) toDouble();
    }

    public String toJSON()
    {
      return toString();
    }

    public boolean toLogical()
    {
      return false;
    }

    public String toString()
    {
      Writer writer = new Writer();
      writeJSON( writer );
      return writer.toString();
    }

    public void writeJSON( Writer writer )
    {
      writer.print( toJSON() );
    }

  }

  static public class NullValue extends Value
  {
    static NullValue singleton = new NullValue();

    public boolean equals( Value other )
    {
      if (other == null) return true;
      return other.isNull();
    }

    public String toString()
    {
      return "null";
    }
  }

  static public class LogicalValue extends Value
  {
    static LogicalValue true_value = new LogicalValue( true );
    static LogicalValue false_value = new LogicalValue( false );

    public boolean value;

    public LogicalValue( boolean value )
    {
      this.value = value;
    }

    public boolean equals( Value other )
    {
      if (other == null) return (value == false);
      return other.toLogical() == value;
    }

    public double toDouble()
    {
      return value ? 1.0 : 0.0;
    }

    public boolean toLogical()
    {
      return value;
    }

    public String toString()
    {
      return value ? "true" : "false";
    }
  }

  static public class NumberValue extends Value
  {
    public double value;

    public NumberValue( double value )
    {
      this.value = value;
    }

    public boolean equals( Value other )
    {
      if (other == null) return (value == 0.0);
      return other.toDouble() == value;
    }

    public double toDouble()
    {
      return value;
    }

    public int toInt()
    {
      return (int) value;
    }

    public boolean toLogical()
    {
      return value == 0.0;
    }

    public String toString()
    {
      if (value == Math.floor(value)) return "" + (int) value;
      return "" + value;
    }
  }

  static public class StringValue extends Value
  {
    public String value;

    public StringValue( String value )
    {
      this.value = value;
    }

    public boolean equals( Value other )
    {
      if (other == null) return (value == null || value.equals(""));
      return other.toString().equals( toString() );
    }

    public double toDouble()
    {
      try
      {
        return Double.parseDouble( value );
      }
      catch (Exception ignore)
      {
        return 0.0;
      }
    }

    public boolean toLogical()
    {
      return (value == null);
    }

    public String toString()
    {
      return value;
    }

    public void writeJSON( Writer writer )
    {
      writeJSON( value, writer );
    }

    // GLOBAL METHODS
    static public void writeJSON( String st, Writer writer )
    {
      if (st == null)
      {
        writer.print( "null" );
        return;
      }

      writer.print( '"' );
      for (int i=0; i<st.length(); ++i)
      {
        char ch = st.charAt( i );
        switch (ch)
        {
          case '"':
            writer.print( "\\\"" );
          case '\\':
            writer.print( "\\\\" );
          case '\b':
            writer.print( "\\b" );
          case '\f':
            writer.print( "\\f" );
          case '\n':
            writer.print( "\\n" );
          case '\r':
            writer.print( "\\r" );
          case '\t':
            writer.print( "\\t" );
          default:
            if (ch >= 32 && ch <= 126)
            {
              writer.print( ch );
            }
            else if (ch < 32 || ch == 127 || ch == 0x2028 || ch == 0x2029)
            {
              // RE: 2028/9:
              // http://stackoverflow.com/questions/2965293/javascript-parse-error-on-u2028-unicode-character
              writer.print( "\\u" );
              int n = ch;
              for (int nibble=0; nibble<=3; ++nibble)
              {
                int digit = (n >> 12) & 15;
                n = n << 4;
                if (digit <= 9)
                {
                  writer.print( digit );
                }
                else
                {
                  writer.print( (char)('a' + (digit - 10)) );
                }
              }
            }
            else
            {
              // Store printable Unicode without encoding as \\uXXXX
              writer.print( ch );
            }
        }
      }
      writer.print( '"' );
    }
  }

  /*
  static public class ListValue extends Value
  {
    public ArrayList<Value> data = new ArrayList<Value>();

    public boolean contains( String key )
    {
      return false;
    }

    public int count()
    {
      return data.size();
    }

    public boolean equals( Value other )
    {
      if (other == null) return (value == null || value.equals(""));
      return other.toString().equals( toString() );
    }

    public Value get( String key )
    {
      Value result = data.get( key );
      if (result != null) return result;
      else                return NullValue.singleton;
    }

    public TableValue set( String key, double value )
    {
      data.put( key, number(value) );
      return this;
    }

    public void writeJSON( Writer writer )
    {
      writer.print( '{' );
      for (String key : data.keySet())
      {
        StringValue.writeJSON( key, writer );
        writer.print( ':' );
        Value value = data.get( key );
        if (value == null) writer.print( "null" );
        else               value.writeJSON( writer );
      }
      writer.print( '}' );
    }
  }
  */

  static public class TableValue extends Value
  {
    public HashMap<String,Value> data = new HashMap<String,Value>();

    public boolean contains( String key )
    {
      return data.containsKey( key );
    }

    public int count()
    {
      return data.size();
    }

    public boolean equals( Value other )
    {
      if (other == null) return false;
      if (count() != other.count()) return false;
      if ( !(other instanceof TableValue) ) return false;

      return other.toString().equals( this.toString() );
    }

    public Value get( String key )
    {
      Value result = data.get( key );
      if (result != null) return result;
      else                return NullValue.singleton;
    }

    public TableValue set( String key, double value )
    {
      data.put( key, number(value) );
      return this;
    }

    public double toDouble()
    {
      return 0.0;
    }

    public boolean toLogical()
    {
      return true;
    }

    public void writeJSON( Writer writer )
    {
      writer.print( '{' );
      for (String key : data.keySet())
      {
        StringValue.writeJSON( key, writer );
        writer.print( ':' );
        Value value = data.get( key );
        if (value == null) writer.print( "null" );
        else               value.writeJSON( writer );
      }
      writer.print( '}' );
    }
  }

  // UTILITY
  static public class Writer
  {
    protected ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    protected OutputStreamWriter writer;

    public Writer print( char ch )
    {
      if (writer == null) writer = new OutputStreamWriter( bytes );
      try
      {
        writer.write( ch );
      }
      catch (IOException ignore)
      {
      }
      return this;
    }

    public Writer print( int value )
    {
      return print( ""+value );
    }

    public Writer print( String st )
    {
      if (writer == null) writer = new OutputStreamWriter( bytes );
      try
      {
        for (int i=0; i<st.length(); ++i)
        {
          writer.write( st.charAt(i) );
        }
      }
      catch (IOException ignore)
      {
      }
      return this;
    }

    public String toString()
    {
      if (writer == null) return "";

      try
      {
        writer.close();
      }
      catch (IOException ignore)
      {
      }

      String result = bytes.toString();
      bytes.reset();
      writer = null;
      return result;
    }
  }
}

