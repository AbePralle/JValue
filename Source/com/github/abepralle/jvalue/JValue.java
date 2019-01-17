//=============================================================================
//  JValue.java
//=============================================================================
package com.github.abepralle.jvalue;

import java.io.*;
import java.text.*;
import java.util.*;

public class JValue implements Iterable<JValue>
{
  static DecimalFormat formatter;

  // GLOBAL METHODS
  static public JValue load( File file )
  {
    try
    {
      return new JSONReader( file ).parseValue();
    }
    catch (JSONParseError err)
    {
      return UndefinedValue.singleton;
    }
  }

  static public JValue list()
  {
    return new ListValue();
  }

  static public JValue list( File file )
  {
    try
    {
      JValue result = new JSONReader( file ).parseValue();
      if (result.isList()) return result;
    }
    catch (JSONParseError err)
    {
    }
    return new ListValue();
  }

  static public JValue list( String json )
  {
    try
    {
      JValue result = new JSONReader( json ).parseValue();
      if (result.isList()) return result;
    }
    catch (JSONParseError err)
    {
    }
    return new ListValue();
  }

  static public JValue logical( boolean value )
  {
    return value ? LogicalValue.true_singleton : LogicalValue.false_singleton;
  }

  static public JValue number( double value )
  {
    return new NumberValue( value );
  }

  static public JValue nullValue()
  {
    return NullValue.singleton;
  }

  static public JValue parse( File file )
  {
    try
    {
      return new JSONReader( file ).parseValue();
    }
    catch (JSONParseError err)
    {
      return UndefinedValue.singleton;
    }
  }

  static public JValue parse( String json )
  {
    try
    {
      return new JSONReader( json ).parseValue();
    }
    catch (JSONParseError err)
    {
      return UndefinedValue.singleton;
    }
  }

  static public JValue string( String value )
  {
    if (value == null) return NullValue.singleton;
    if (value.length() == 0) return StringValue.empty_singleton;
    return new StringValue( value );
  }

  static public JValue table()
  {
    return new TableValue();
  }

  static public JValue table( File file )
  {
    try
    {
      JValue result = new JSONReader( file ).parseValue();
      if (result.isTable()) return result;
    }
    catch (JSONParseError err)
    {
    }
    return new TableValue();
  }

  static public JValue table( String json )
  {
    try
    {
      JValue result = new JSONReader( json ).parseValue();
      if (result.isTable()) return result;
    }
    catch (JSONParseError err)
    {
    }
    return new TableValue();
  }

  static public JValue undefinedValue()
  {
    return UndefinedValue.singleton;
  }

  static String format( double n )
  {
    if (formatter == null)
    {
      formatter = new DecimalFormat( "0", DecimalFormatSymbols.getInstance(Locale.ENGLISH) );
      formatter.setMaximumFractionDigits( 340 );
    }
    return formatter.format( n );
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

  public JValue apply( JValue.Processor fn )
  {
    JValue result = fn.process( this );
    if (result == null) return JValue.nullValue();
    else                return result;
  }

  public JValue clear()
  {
    // No action
    return this;
  }

  public JValue cloned()
  {
    return this;
  }

  public double compareTo( JValue other )
  {
    return toString().compareTo( other.toString() );
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

  public JValue ensureList( String name )
  {
    if ( !this.isTable() ) return JValue.list();

    JValue result = this.get(name);
    if (result.isList()) return result;

    result = JValue.list();
    this.set( name, result );
    return result;
  }

  public JValue ensureTable( String name )
  {
    if ( !this.isTable() ) return JValue.table();

    JValue result = this.get(name);
    if (result.isTable()) return result;

    result = JValue.table();
    this.set( name, result );
    return result;
  }

  public boolean exists()
  {
    return !isUndefined();
  }

  public boolean equals( Object other )
  {
    if (other == null) return this.isNull();

    if ( !(other instanceof JValue) ) return toString().equals(other.toString());

    return equals( (JValue) other );
  }

  public boolean equals( JValue other )
  {
    if (other == null) return false;
    return other.toString().equals( this.toString() );
  }

  public JValue get( String key )
  {
    return UndefinedValue.singleton;
  }

  public JValue get( int index )
  {
    return UndefinedValue.singleton;
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
    return false;
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

  public boolean isUndefined()
  {
    return false;
  }

  public Iterator<JValue> iterator()
  {
    return new Iterator<JValue>()
    {
      public boolean hasNext() { return false; }
      public JValue  next() { return null; }
      public void    remove() {}
    };
  }

  public JValue keys()
  {
    return UndefinedValue.singleton;
  }

  public JValue remove( String key )
  {
    return remove( JValue.string(key) );
  }

  public JValue remove( int index )
  {
    return remove( JValue.number(index) );
  }

  public JValue remove( JValue value )
  {
    return UndefinedValue.singleton;
  }

  public boolean save( File file )
  {
    try
    {
      String json = this.toJSON();
      BufferedWriter writer = new BufferedWriter( new FileWriter(file), 1024 );
      writer.write( json );
      writer.close();
      return true;
    }
    catch (IOException ignore)
    {
      return false;
    }
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

  public JValue set( JValue key, JValue value )
  {
    if (key.isNumber()) return set( key.toInt(), value );
    else                return set( key.toString(), value );
  }

  public JValue set( JValue key, double value )
  {
    return set( key, JValue.number(value) );
  }

  public JValue set( JValue key, boolean value )
  {
    return set( key, JValue.logical(value) );
  }

  public JValue set( JValue key, String value )
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

  static class NullValue extends JValue
  {
    static NullValue singleton = new NullValue();

    public double compareTo( JValue other )
    {
      if (other.isNull()) return 0;
      return super.compareTo( other );
    }

    public boolean equals( JValue other )
    {
      if (other == null) return true;
      return other.isNull();
    }

    public boolean isNull()
    {
      return true;
    }

    public String toString()
    {
      return "null";
    }
  }

  static class UndefinedValue extends JValue
  {
    static UndefinedValue singleton = new UndefinedValue();

    public double compareTo( JValue other )
    {
      if (other.isUndefined()) return 0;
      return super.compareTo( other );
    }

    public boolean equals( JValue other )
    {
      if (other == null) return false;
      return other.isUndefined();
    }

    public String toString()
    {
      return "";
    }

    public boolean isUndefined()
    {
      return true;
    }
  }

  static class LogicalValue extends JValue
  {
    static LogicalValue true_singleton = new LogicalValue( true );
    static LogicalValue false_singleton = new LogicalValue( false );

    boolean value;

    public LogicalValue( boolean value )
    {
      this.value = value;
    }

    public JValue cloned()
    {
      return new LogicalValue( value );
    }

    public double compareTo( JValue other )
    {
      if (other.isLogical()) return other.toInt() - this.toInt();
      return super.compareTo( other );
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

  static class NumberValue extends JValue
  {
    double value;

    public NumberValue( double value )
    {
      this.value = value;
    }

    public JValue cloned()
    {
      return new NumberValue( value );
    }

    public double compareTo( JValue other )
    {
      if (other.isNumber()) return other.toDouble() - this.toDouble();
      return super.compareTo( other );
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
      return value != 0.0;
    }

    public String toString()
    {
      if (value == Math.floor(value)) return "" + (int) value;
      return format( value );
    }
  }

  static class StringValue extends JValue
  {
    static public StringValue empty_singleton = new StringValue( "" );

    String value;

    public StringValue( String value )
    {
      this.value = value;
    }

    public boolean contains( String key )
    {
      return value.contains( key );
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

    public String toJSON()
    {
      JSONWriter writer = new JSONWriter();
      write( writer );
      return writer.toString();
    }

    public boolean toLogical()
    {
      return (value != null);
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
            break;
          case '\\':
            writer.print( "\\\\" );
            break;
          case '\b':
            writer.print( "\\b" );
            break;
          case '\f':
            writer.print( "\\f" );
            break;
          case '\n':
            writer.print( "\\n" );
            break;
          case '\r':
            writer.print( "\\r" );
            break;
          case '\t':
            writer.print( "\\t" );
            break;
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

  static class ListValue extends JValue
  {
    ArrayList<JValue> data = new ArrayList<JValue>();

    public JValue add( JValue value )
    {
      if (value == null) value = NullValue.singleton;
      data.add( value );
      return this;
    }

    public JValue apply( JValue.Processor fn )
    {
      int write_index = 0;
      for (int i=0; i<data.size(); ++i)
      {
        JValue processed_element = data.get(i).apply( fn );
        if (processed_element != null && !processed_element.isUndefined())
        {
          data.set( write_index++, processed_element );
        }
      }
      while (data.size() > write_index) data.remove( data.size()-1 );
      return super.apply( fn );
    }

    public JValue clear()
    {
      data.clear();
      return this;
    }

    public JValue cloned()
    {
      JValue.ListValue result = new ListValue();
      result.data.ensureCapacity( count() );
      int n = count();
      for (int i=0; i<n; ++i)
      {
        result.add( get(i).cloned() );
      }
      return result;
    }

    public double compareTo( JValue other )
    {
      if (this == other) return 0;
      if (this.count() != other.count()) return other.count() - this.count();

      int n = count();
      for (int i=0; i<n; ++i)
      {
        double result = this.get(i).compareTo( other.get(i) );
        if (result != 0) return result;
      }

      return 0;
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
        return UndefinedValue.singleton;
      }
    }

    public JValue get( int index )
    {
      if (index < 0 || index >= data.size()) return UndefinedValue.singleton;
      return data.get( index );
    }

    public boolean isList()
    {
      return true;
    }

    public Iterator<JValue> iterator()
    {
      return data.iterator();
    }

    public JValue keys()
    {
      JValue result = JValue.list();
      int n = count();
      for (int i=0; i<n; ++i) result.add( i );
      return result;
    }

    public JValue remove( String key )
    {
      return remove( JValue.string(key) );
    }

    public JValue remove( JValue value )
    {
      data.remove( value );
      return value;
    }

    public JValue remove( int index )
    {
      if (index < 0 || index >= count()) return UndefinedValue.singleton;
      JValue result = get( index );
      data.remove( index );
      return result;
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

  static class TableValue extends JValue
  {
    LinkedHashMap<String,JValue> data = new LinkedHashMap<String,JValue>();

    public JValue apply( JValue.Processor fn )
    {
      for (String key : data.keySet())
      {
        JValue old_value = data.get( key );
        JValue new_value = old_value.apply( fn );
        if (new_value != old_value)
        {
          if (new_value == null || new_value.isUndefined())
          {
            data.remove( key );
          }
          else
          {
            data.put( key, new_value );
          }
        }
      }
      return super.apply( fn );
    }

    public JValue clear()
    {
      data.clear();
      return this;
    }

    public JValue cloned()
    {
      JValue.TableValue result = new JValue.TableValue();
      for (String key : data.keySet())
      {
        result.set( key, get(key).cloned() );
      }
      return result;
    }

    public double compareTo( JValue other )
    {
      if (this == other) return 0;
      if (this.count() != other.count()) return other.count() - this.count();
      return super.compareTo( other );
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

    public JValue get( String key )
    {
      JValue result = data.get( key );
      if (result != null) return result;
      else                return UndefinedValue.singleton;
    }

    public JValue get( int index )
    {
      return get( ""+index );
    }

    public boolean isTable()
    {
      return true;
    }

    public Iterator<JValue> iterator()
    {
      return data.values().iterator();
    }

    public JValue keys()
    {
      JValue list = JValue.list();
      for (String key : data.keySet())
      {
        list.add( JValue.string(key) );
      }
      return list;
    }

    public JValue remove( String key )
    {
      JValue result = data.get( key );
      if (result == null) return UndefinedValue.singleton;
      data.remove( key );
      return result;
    }

    public JValue remove( JValue value )
    {
      return remove( value.toString() );
    }

    public TableValue set( int key, JValue value )
    {
      return set( ""+key, value );
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
      boolean first = true;
      for (String key : data.keySet())
      {
        if (first) first = false;
        else       writer.print( ',' );
        StringValue.write( key, writer );
        writer.print( ':' );
        data.get( key ).write( writer );
      }
      writer.print( '}' );
    }
  }

  // UTILITY
  static class JSONWriter
  {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedWriter writer;

    JSONWriter print( char ch )
    {
      if (writer == null) writer = new BufferedWriter( new OutputStreamWriter( bytes ), 1024 );
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
      if (writer == null) writer = new BufferedWriter( new OutputStreamWriter( bytes ), 1024 );
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

  static class JSONReader
  {
    StringBuilder parse_buffer = new StringBuilder();
    HashMap<String,String> consolidation_table = new HashMap<String,String>();

    StringBuilder data;
    int count;
    int position;

    JSONReader( String source )
    {
      count = source.length();
      data = new StringBuilder( count );
      if (count >= 1 && source.charAt(0) == 0xFEFF)
      {
        // Discard Byte Order Mark (BOM)
        for (int i=1; i<count; ++i)
        {
          data.append( source.charAt(i) );
        }
      }
      else if (count >= 3 && source.charAt(0) == 0xEF && source.charAt(1) == 0xBB && source.charAt(2) == 0xBF)
      {
        // Discard Byte Order Mark (BOM)
        for (int i=3; i<count; ++i)
        {
          data.append( source.charAt(i) );
        }
      }
      else
      {
        data.append( source );
      }
    }

    JSONReader( File file )
    {
      try
      {
        count = (int) file.length();
        data = new StringBuilder( count );
        BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream(file.getPath()) ), 1024 );

        int firstCh = reader.read();
        if (firstCh != -1 && firstCh != 0xFEFF)
        {
          // Discard Byte Order Mark (BOM)
          data.append( (char) firstCh );
        }

        for (int ch=reader.read(); ch!=-1; ch=reader.read())
        {
          data.append( (char) ch );
        }
        count = data.length();
        reader.close();
      }
      catch (Exception err)
      {
        data = new StringBuilder();
      }
    }

    String consolidate( String st )
    {
      String consolidated = consolidation_table.get( st );
      if (consolidated != null) return consolidated;
      consolidation_table.put( st, st );
      return st;
    }

    boolean consume( char ch )
    {
      if (position == count) return false;
      if (ch != data.charAt(position)) return false;
      ++position;
      return true;
    }

    boolean consumeEOLs()
    {
      boolean consumed_any = false;
      while (consume('\n') || consume('\r')) consumed_any = true;
      return consumed_any;
    }

    boolean consumeSpaces()
    {
      boolean consumed_any = false;
      while (consume(' ') || consume('\t')) consumed_any = true;
      return consumed_any;
    }

    void consumeSpacesAndEOLs()
    {
      while (consumeSpaces() || consumeEOLs()) {}
    }

    boolean hasAnother()
    {
      return position < count;
    }

    JValue parseValue()
    {
      consumeSpacesAndEOLs();

      if ( !hasAnother() ) return UndefinedValue.singleton;

      char ch = peek();
      if (ch == '{') return parseTable();
      if (ch == '[') return parseList();

      if (ch == '-')              return parseNumber();
      if (ch >= '0' && ch <= '9') return parseNumber();

      if (ch == '"' || ch == '\'')
      {
        String result = parseString();
        if (result.length() == 0) return StringValue.empty_singleton;

        char first_ch = result.charAt( 0 );
        if (first_ch == 't' && result.equals("true"))  return LogicalValue.true_singleton;
        if (first_ch == 'f' && result.equals("false")) return LogicalValue.false_singleton;
        if (first_ch == 'n' && result.equals("null"))  return NullValue.singleton;

        return new StringValue( result );
      }
      else if (nextIsIdentifier())
      {
        String result = parseIdentifier();
        if (result.length() == 0) return StringValue.empty_singleton;

        char first_ch = result.charAt( 0 );
        if (first_ch == 't' && result.equals("true"))  return LogicalValue.true_singleton;
        if (first_ch == 'f' && result.equals("false")) return LogicalValue.false_singleton;
        if (first_ch == 'n' && result.equals("null"))  return NullValue.singleton;

        return new StringValue( result );
      }
      else
      {
        return UndefinedValue.singleton;
      }
    }

    JValue parseTable()
    {
      consumeSpacesAndEOLs();

      if ( !consume('{')) return UndefinedValue.singleton;

      consumeSpacesAndEOLs();

      JValue table = new TableValue();
      if (consume('}')) return table;

      int prev_pos = position;
      boolean first = true;
      while (first || consume(',') || (hasAnother() && peek()!='}' && position>prev_pos))
      {
        first = false;
        prev_pos = position;

        consumeSpacesAndEOLs();

        if (nextIsIdentifier())
        {
          String key = parseIdentifier();
          consumeSpacesAndEOLs();

          if (key.length() > 0)
          {
            if (consume(':'))
            {
              consumeSpacesAndEOLs();
              JValue value = parseValue();
              table.set( key, value );
            }
            else
            {
              table.set( key, JValue.logical(true) );
            }
            consumeSpacesAndEOLs();
          }
        }
      }

      if ( !consume('}')) throw new JSONParseError( "'}' expected." );

      return table;
    }

    JValue parseList()
    {
      consumeSpacesAndEOLs();

      if ( !consume('[')) return UndefinedValue.singleton;

      consumeSpacesAndEOLs();

      JValue list = new ListValue();
      if (consume(']')) return list;

      int prev_pos = position;
      boolean first = true;
      while (first || consume(',') || (hasAnother() && peek()!=']' && position>prev_pos))
      {
        first = false;
        prev_pos = position;
        consumeSpacesAndEOLs();
        if (peek() == ']') break;
        list.add( parseValue() );
        consumeSpacesAndEOLs();
      }

      if ( !consume(']')) throw new JSONParseError( "']' expected." );

      return list;
    }

    String parseString()
    {
      consumeSpacesAndEOLs();

      char terminator = '"';
      if      (consume( '"' ))  terminator = '"';
      else if (consume( '\'' )) terminator = '\'';

      if ( !hasAnother()) return "";

      StringBuilder buffer = parse_buffer;
      buffer.setLength( 0 );
      char ch = read();
      while (hasAnother() && ch != terminator)
      {
        if (ch == '\\')
        {
          ch = read();
          if (ch == 'b')     buffer.append( '\b' );
          else if (ch == 'f') buffer.append( '\f' );
          else if (ch == 'n') buffer.append( '\n' );
          else if (ch == 'r') buffer.append( '\r' );
          else if (ch == 't') buffer.append( '\t' );
          else if (ch == 'u') buffer.append( parseHexQuad() );
          else               buffer.append( ch );
        }
        else
        {
          buffer.append( ch );
        }
        ch = read();
      }

      return consolidate( buffer.toString() );
    }

    int hexCharacterToValue( char ch )
    {
      if (ch >= '0' && ch <= '9') return (ch - '0');
      if (ch >= 'A' && ch <= 'Z') return (ch - 'A') + 10;
      if (ch >= 'a' && ch <= 'z') return (ch - 'a') + 10;
      return 0;
    }

    char parseHexQuad()
    {
      int code = 0;
      for (int i=1; i<=4; ++i)
      {
        if (hasAnother())
        {
          code = (code << 4) | hexCharacterToValue( read() );
        }
      }
      return (char) code;
    }

    boolean isIdentifierStart( char ch )
    {
      return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || ch == '_';
    }

    boolean isIdentifierContinuation( char ch )
    {
      return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') || ch == '_';
    }

    String parseIdentifier()
    {
      consumeSpacesAndEOLs();

      char ch = peek();
      if (ch == '"' || ch == '\'')
      {
        return parseString();
      }
      else
      {
        StringBuilder buffer = parse_buffer;
        buffer.setLength( 0 );
        boolean finished = false;
        while ( !finished && hasAnother() )
        {
          if (isIdentifierStart(ch))
          {
            read();
            buffer.append( ch );
            ch = peek();
          }
          else
          {
            finished = true;
          }
        }

        if (buffer.length() == 0) throw new JSONParseError( "Identifier expected." );
        return consolidate( buffer.toString() );
      }
    }

    boolean nextIsIdentifier()
    {
      char ch = peek();
      if (isIdentifierContinuation(ch)) return true;
      return (ch == '"' || ch == '\'');
    }

    JValue parseNumber()
    {
      consumeSpacesAndEOLs();

      double sign = 1.0;
      if (consume( '-' ))
      {
        sign = -1.0;
        consumeSpacesAndEOLs();
      }

      double n = 0.0;
      char ch = peek();
      while (hasAnother() && ch >= '0' && ch <= '9')
      {
        read();
        n = n * 10 + (ch - '0');
        ch = peek();
      }

      boolean is_real = false;

      if (consume( '.' ))
      {
        is_real = true;
        double decimal = 0.0;
        double power = 0.0;
        ch = peek();
        while (hasAnother() && ch >= '0' && ch <= '9')
        {
          read();
          decimal = decimal * 10 + (ch - '0');
          power += 1.0;
          ch = peek();
        }
        n += decimal / (Math.pow(10.0,power));
      }

      if (consume( 'e' ) || consume( 'E' ))
      {
        boolean negexp = false;
        if ( !consume('+') && consume('-')) negexp = true;

        double power = 0.0;
        ch = peek();
        while (hasAnother() && ch >= '0' && ch <= '9')
        {
          read();
          power = power * 10.0 + (ch - '0');
          ch = peek();
        }

        if (negexp) n /= Math.pow(10,power);
        else        n *= Math.pow(10,power);
      }

      n = n * sign;

      return JValue.number( n );
    }

    char peek()
    {
      if (position == count) return (char) 0;
      return data.charAt( position );
    }

    char read()
    {
      return data.charAt( position++ );
    }

  }

  static class JSONParseError extends RuntimeException
  {
    JSONParseError( String message )
    {
      super( message );
    }
  }

  static class Processor
  {
    public JValue process( JValue value )
    {
      return value;
    }
  }
}

