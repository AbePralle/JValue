# JValue
v1.0 - October 30, 2018

## Overview
JValue is a lightweight, robust, and full-featured Java library for managing "JSON-style" data as well as serializing to and from JSON. It is adapted from the [Rogue Language](https://github.com/AbePralle/Rogue) "Value System".

JValue uses the permissive MIT License.

## Examples
    JValue data = JValue.table();
    JValue names = data.ensureList( "names" );
    names.add( JValue.table().set("first","Abe").set("last","Pralle") );
    names.add( JValue.parse( "{\"first\":\"April\",\"last\":\"Lee\"}" ) );
    names.save( new File("Data.json") );
    ...

    JValue data  = JValue.load( new File("Data.json") );
    JValue names = data.get( "names" );
    for (int i=0; i<names.count(); ++i)
    {
      JValue name = names.get( i );
      if ( !name.contains("full") )
      {
        name.set( "full", name.get("first").toString() + " "  + name.get("last").toString() );
      }
    }
    System.out.println( names.toJSON() );

