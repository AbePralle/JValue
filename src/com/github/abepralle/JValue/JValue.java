package com.github.abepralle.JValue;

import java.io.*;
import java.util.*;

public class JValue
{
  // GLOBAL METHODS
  static public JValue list()
  {
    return new ListValue();
  }

  static public JValue logical( boolean value )
  {
    return value ? LogicalValue.true_value : LogicalValue.false_value;
  }

  static public JValue number( double value )
  {
    return new NumberValue( value );
  }

  static public JValue null_value()
  {
    return NullValue.singleton;
  }

  static public JValue string( String value )
  {
    if (value == null) return NullValue.singleton;
    return new StringValue( value );
  }

  static public JValue table()
  {
    return new TableValue();
  }

  // METHODS
  public JValue add( JValue value )
  {
    // No action
    return this;
  }

  public JValue add( double value )
  {
    return add( JValue.number(value) );
  }

  public JValue add( boolean value )
  {
    return add( JValue.logical(value) );
  }

  public JValue add( String value )
  {
    return add( JValue.string(value) );
  }

  public JValue clear()
  {
    // No action
    return this;
  }

  public boolean contains( String key )
  {
    return false;
  }

  public boolean contains( JValue value )
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

    if ( !(other instanceof JValue) ) return false;

    return equals( (JValue) other );
  }

  public boolean equals( JValue other )
  {
    if (other == null) return false;
    return other.toString().equals( this.toString() );
  }

  public JValue get( String key )
  {
    return NullValue.singleton;
  }

  public JValue get( int index )
  {
    return NullValue.singleton;
  }

  public JValue get( JValue index )
  {
    if (index.isNumber()) return get( index.toInt() );
    else                  return get( index.toString() );
  }

  public boolean isList()
  {
    return false;
  }

  public boolean isLogical()
  {
    return false;
  }

  public boolean isNonNull()
  {
    return this != NullValue.singleton;
  }

  public boolean isNull()
  {
    return this == NullValue.singleton;
  }

  public boolean isNumber()
  {
    return false;
  }

  public boolean isString()
  {
    return false;
  }

  public boolean isTable()
  {
    return false;
  }

  public JValue set( int index, JValue value )
  {
    // No action
    return this;
  }

  public JValue set( int index, double value )
  {
    return set( index, JValue.number(value) );
  }

  public JValue set( int index, boolean value )
  {
    return set( index, JValue.logical(value) );
  }

  public JValue set( int index, String value )
  {
    return set( index, JValue.string(value) );
  }

  public JValue set( String key, JValue value )
  {
    // No action
    return this;
  }

  public JValue set( String key, double value )
  {
    return set( key, JValue.number(value) );
  }

  public JValue set( String key, boolean value )
  {
    return set( key, JValue.logical(value) );
  }

  public JValue set( String key, String value )
  {
    return set( key, JValue.string(value) );
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
    JSONWriter writer = new JSONWriter();
    write( writer );
    return writer.toString();
  }

  public void write( JSONWriter writer )
  {
    writer.print( toJSON() );
  }

  static public class NullValue extends JValue
  {
    static NullValue singleton = new NullValue();

    public boolean equals( JValue other )
    {
      if (other == null) return true;
      return other.isNull();
    }

    public String toString()
    {
      return "null";
    }
  }

  static public class LogicalValue extends JValue
  {
    static LogicalValue true_value = new LogicalValue( true );
    static LogicalValue false_value = new LogicalValue( false );

    public boolean value;

    public LogicalValue( boolean value )
    {
      this.value = value;
    }

    public boolean equals( JValue other )
    {
      if (other == null) return (value == false);
      return other.toLogical() == value;
    }

    public boolean isLogical()
    {
      return true;
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

  static public class NumberValue extends JValue
  {
    public double value;

    public NumberValue( double value )
    {
      this.value = value;
    }

    public boolean equals( JValue other )
    {
      if (other == null) return (value == 0.0);
      return other.toDouble() == value;
    }

    public boolean isNumber()
    {
      return true;
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

  static public class StringValue extends JValue
  {
    public String value;

    public StringValue( String value )
    {
      this.value = value;
    }

    public boolean equals( JValue other )
    {
      if (other == null) return (value == null || value.equals(""));
      return other.toString().equals( toString() );
    }

    public boolean isString()
    {
      return true;
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

    public void write( JSONWriter writer )
    {
      write( value, writer );
    }

    // GLOBAL METHODS
    static public void write( String st, JSONWriter writer )
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

  static public class ListValue extends JValue
  {
    public ArrayList<JValue> data = new ArrayList<JValue>();

    public JValue add( JValue value )
    {
      if (value == null) value = NullValue.singleton;
      data.add( value );
      return this;
    }

    public JValue clear()
    {
      data.clear();
      return this;
    }

    public boolean contains( String value )
    {
      int size = data.size();
      for (int i=0; i<size; ++i)
      {
        JValue v = data.get( i );
        if (v.isString() && v.toString().equals(value)) return true;
      }
      return false;
    }

    public boolean contains( JValue value )
    {
      int size = data.size();
      for (int i=0; i<size; ++i)
      {
        if (data.get(i).equals(value)) return true;
      }
      return false;
    }

    public int count()
    {
      return data.size();
    }

    public boolean equals( JValue other )
    {
      if (other == null) return false;
      int size = count();
      if (size != other.count()) return false;
      if ( !other.isList() ) return false;

      for (int i=0; i<size; ++i)
      {
        if ( !get(i).equals(other.get(i)) ) return false;
      }

      return true;
    }

    public JValue get( String key )
    {
      try
      {
        return get( Integer.parseInt(key) );
      }
      catch (Exception ignore)
      {
        return NullValue.singleton;
      }
    }

    public JValue get( int index )
    {
      return data.get( index );
    }

    public boolean isList()
    {
      return true;
    }

    public ListValue set( int index, JValue value )
    {
      if (index < 0 || index >= data.size()) return this;
      data.set( index, (value != null) ? value : NullValue.singleton );
      return this;
    }

    public ListValue set( String index, JValue value )
    {
      return set( ""+index, value );
    }

    public boolean toLogical()
    {
      return true;
    }

    public void write( JSONWriter writer )
    {
      writer.print( '[' );
      int size = count();
      for (int i=0; i<size; ++i)
      {
        if (i > 0) writer.print( ',' );
        get( i ).write( writer );
      }
      writer.print( ']' );
    }
  }

  static public class TableValue extends JValue
  {
    public LinkedHashMap<String,JValue> data = new LinkedHashMap<String,JValue>();

    public JValue clear()
    {
      data.clear();
      return this;
    }

    public boolean contains( String key )
    {
      return data.containsKey( key );
    }

    public int count()
    {
      return data.size();
    }

    public boolean equals( JValue other )
    {
      if (other == null) return false;
      if (count() != other.count()) return false;
      if ( !other.isTable() ) return false;

      LinkedHashMap<String,JValue> other_data = ((TableValue)other).data;
      for (String key : data.keySet())
      {
        if ( !other_data.containsKey(key) ) return false;
        if ( !get(key).equals(other_data.get(key)) ) return false;
      }

      return true;
    }

    public boolean isTable()
    {
      return true;
    }

    public JValue get( String key )
    {
      JValue result = data.get( key );
      if (result != null) return result;
      else                return NullValue.singleton;
    }

    public JValue get( int index )
    {
      return get( ""+index );
    }

    public TableValue set( String key, JValue value )
    {
      data.put( key, (value != null) ? value : NullValue.singleton );
      return this;
    }

    public boolean toLogical()
    {
      return true;
    }

    public void write( JSONWriter writer )
    {
      writer.print( '{' );
      for (String key : data.keySet())
      {
        StringValue.write( key, writer );
        writer.print( ':' );
        data.get( key ).write( writer );
      }
      writer.print( '}' );
    }
  }

  // UTILITY
  static public class JSONWriter
  {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    OutputStreamWriter writer;

    JSONWriter print( char ch )
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

    JSONWriter print( int value )
    {
      return print( ""+value );
    }

    JSONWriter print( String st )
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

  static public class JSONReader
  {
    StringBuilder data;
    int count;
    int position;

    JSONReader( String source )
    {
      count = source.length();
      data = new StringBuilder( count );
      data.append( source );
    }

    JSONReader( File file )
    {
      try
      {
        data = new StringBuilder( (int) file.length() );
        BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream(file.getPath()) ) );
        for (int ch=reader.read(); ch!=-1; ch=reader.read())
        {
          data.append( (char) ch );
        }
        count = data.length();
      }
      catch (Exception err)
      {
        data = new StringBuilder();
      }
    }
  }
}

