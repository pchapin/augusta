---------------------------------------------------------------------------
-- FILE    : augusta-characters-handling.adb
-- SUBJECT : Body of character conversions package.
-- AUTHOR  : (C) Copyright 2013 by Peter C. Chapin
--
-- Please send comments or bug reports to
--
--      Peter C. Chapin <PChapin@vtc.vsc.edu>
---------------------------------------------------------------------------

package body Augusta.Characters.Conversions is

   function Is_Character(Item : in Wide_Character) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Character;


   function Is_String(Item : in Wide_String) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_String;


   function Is_Character(Item : in Wide_Wide_Character) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Character;


   function Is_String(Item : in Wide_Wide_String) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_String;


   function Is_Wide_Character(Item : in Wide_Wide_Character) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Wide_Character;


   function Is_Wide_String(Item : in Wide_Wide_String) return Boolean is
   begin
      raise Not_Implemented;
      return False;
   end Is_Wide_String;


   function To_Wide_Character(Item : in Character) return Wide_Character is
   begin
      raise Not_Implemented;
      return ' ';
   end To_Wide_Character;


   function To_Wide_String(Item : in String) return Wide_String is
   begin
      raise Not_Implemented;
      return "";
   end To_Wide_String;


   function To_Wide_Wide_Character(Item : in Character) return Wide_Wide_Character is
   begin
      raise Not_Implemented;
      return ' ';
   end To_Wide_Wide_Character;


   function To_Wide_Wide_String(Item : in String) return Wide_Wide_String is
   begin
      raise Not_Implemented;
      return "";
   end To_Wide_Wide_String;


   function To_Wide_Wide_Character(Item : in Wide_Character) return Wide_Wide_Character is
   begin
      raise Not_Implemented;
      return ' ';
   end To_Wide_Wide_Character;


   function To_Wide_Wide_String(Item : in Wide_String) return Wide_Wide_String is
   begin
      raise Not_Implemented;
      return "";
   end To_Wide_Wide_String;


   function To_Character
     (Item : in Wide_Character; Substitute : in Character := ' ') return Character is
   begin
      raise Not_Implemented;
      return ' ';
   end To_Character;


   function To_String(Item : in Wide_String; Substitute : in Character := ' ') return String is
   begin
      raise Not_Implemented;
      return "";
   end To_String;


   function To_Character
     (Item : in Wide_Wide_Character; Substitute : in Character := ' ') return Character is
   begin
      raise Not_Implemented;
      return ' ';
   end To_Character;


   function To_String
     (Item : in Wide_Wide_String; Substitute : in Character := ' ') return String is
   begin
      raise Not_Implemented;
      return "";
   end To_String;


   function To_Wide_Character
     (Item       : in Wide_Wide_Character;
      Substitute : in Wide_Character := ' ') return Wide_Character is
   begin
      raise Not_Implemented;
      return ' ';
   end To_Wide_Character;


   function To_Wide_String
     (Item       : in Wide_Wide_String;
      Substitute : in Wide_Character := ' ') return Wide_String is
   begin
      raise Not_Implemented;
      return "";
   end To_Wide_String;


end Augusta.Characters.Conversions;
