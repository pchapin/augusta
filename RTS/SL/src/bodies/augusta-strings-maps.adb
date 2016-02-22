---------------------------------------------------------------------------
-- FILE    : augusta-strings-fixed.ads
-- SUBJECT : Augusta standard library fixed length string handling package.
-- AUTHOR  : (C) Copyright 2013 by Peter C. Chapin
--
-- Please send comments or bug reports to
--
--      Peter C. Chapin <PChapin@vtc.vsc.edu>
---------------------------------------------------------------------------
package body Augusta.Strings.Maps is

   function To_Set(Ranges : in Character_Ranges) return Character_Set is
   begin
      raise Not_Implemented;
      return 0;
   end To_Set;


   function To_Set(Span : in Character_Range) return Character_Set is
   begin
      raise Not_Implemented;
      return 0;
   end To_Set;


   function To_Ranges(Set : in Character_Set) return Character_Ranges is
   begin
      raise Not_Implemented;
      return (1 => (Low => 'A', High => 'A'));
   end To_Ranges;


   function "="(Left, Right : in Character_Set) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end "=";


   function "not"(Right : in Character_Set) return Character_Set is
   begin
      raise Not_Implemented;
      return 0;
   end "not";


   function "and"(Left, Right : in Character_Set) return Character_Set is
   begin
      raise Not_Implemented;
      return 0;
   end "and";


   function "or"(Left, Right : in Character_Set) return Character_Set is
   begin
      raise Not_Implemented;
      return 0;
   end "or";


   function "xor"(Left, Right : in Character_Set) return Character_Set is
   begin
      raise Not_Implemented;
      return 0;
   end "xor";


   function "-"(Left, Right : in Character_Set) return Character_Set is
   begin
      raise Not_Implemented;
      return 0;
   end "-";


   function Is_In(Element : in Character; Set : in Character_Set) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_In;


   function Is_Subset(Elements : in Character_Set; Set : in Character_Set) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Subset;


   function To_Set(Sequence : in Character_Sequence) return Character_Set is
   begin
      raise Not_Implemented;
      return 0;
   end To_Set;


   function To_Set(Singleton : in Character) return Character_Set is
   begin
      raise Not_Implemented;
      return 0;
   end To_Set;


   function To_Sequence(Set : in Character_Set) return Character_Sequence is
   begin
      raise Not_Implemented;
      return "";
   end To_Sequence;


   function Value(Map : in Character_Mapping; Element : in Character) return Character is
   begin
      raise Not_Implemented;
      return 'A';
   end Value;


   function To_Mapping(From, To : in Character_Sequence) return Character_Mapping is
   begin
      raise Not_Implemented;
      return 0;
   end To_Mapping;


   function To_Domain(Map : in Character_Mapping) return Character_Sequence is
   begin
      raise Not_Implemented;
      return "";
   end To_Domain;


   function To_Range(Map : in Character_Mapping) return Character_Sequence is
   begin
      raise Not_Implemented;
      return "";
   end To_Range;


end Augusta.Strings.Maps;
