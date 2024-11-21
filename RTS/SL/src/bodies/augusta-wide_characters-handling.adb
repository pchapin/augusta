---------------------------------------------------------------------------
-- FILE    : augusta-wide_characters-handling.adb
-- SUBJECT : Body of wide character handling package.
-- AUTHOR  : (C) Copyright 2025 by the Augusta Contributors
--
-- Please send comments or bug reports to
--
--      Peter Chapin <spicacality@kelseymountain.org>
---------------------------------------------------------------------------

package body Augusta.Wide_Characters.Handling is

   function Character_Set_Version return String is
   begin
      raise Not_Implemented;
      return "TODO";
   end Character_Set_Version;


   function Is_Control(Item : in Wide_Character) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Control;


   function Is_Letter(Item : in Wide_Character) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Letter;


   function Is_Lower(Item : in Wide_Character) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Lower;


   function Is_Upper(Item : in Wide_Character) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Upper;


   function Is_Digit(Item : in Wide_Character) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Digit;


   function Is_Hexadecimal_Digit(Item : in Wide_Character) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Hexadecimal_Digit;


   function Is_Alphanumeric(Item : in Wide_Character) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Alphanumeric;


   function Is_Special(Item : in Wide_Character) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Special;


   function Is_Line_Terminator(Item : in Wide_Character) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Line_Terminator;


   function Is_Mark(Item : in Wide_Character) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Mark;


   function Is_Other_Format(Item : in Wide_Character) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Other_Format;


   function Is_Punctuation_Connector(Item : in Wide_Character) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Punctuation_Connector;


   function Is_Space(Item : in Wide_Character) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Space;


   function Is_Graphic(Item : in Wide_Character) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Graphic;


   function To_Lower(Item : in Wide_Character) return Wide_Character is
   begin
      raise Not_Implemented;
      return ' ';
   end To_Lower;


   function To_Upper(Item : in Wide_Character) return Wide_Character is
   begin
      raise Not_Implemented;
      return ' ';
   end To_Upper;


   function To_Lower(Item : in Wide_String) return Wide_String is
   begin
      raise Not_Implemented;
      return "";
   end To_Lower;


   function To_Upper(Item : in Wide_String) return Wide_String is
   begin
      raise Not_Implemented;
      return "";
   end To_Upper;


end Augusta.Wide_Characters.Handling;
