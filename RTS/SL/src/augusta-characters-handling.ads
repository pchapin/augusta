---------------------------------------------------------------------------
-- FILE    : augusta-characters-handling.ads
-- SUBJECT : Specification of character handling package.
-- AUTHOR  : (C) Copyright 2025 by the Augusta Contributors
--
-- Please send comments or bug reports to
--
--      Peter Chapin <spicacality@kelseymountain.org>
---------------------------------------------------------------------------
with Augusta.Characters.Conversions;

-- See A.3.2.
package Augusta.Characters.Handling is
   pragma Pure(Handling);

   function Is_Control(Item : in Character) return Boolean;
   function Is_Graphic(Item : in Character) return Boolean;
   function Is_Letter(Item : in Character) return Boolean;
   function Is_Lower(Item : in Character) return Boolean;
   function Is_Upper(Item : in Character) return Boolean;
   function Is_Basic(Item : in Character) return Boolean;
   function Is_Digit(Item : in Character) return Boolean;
   function Is_Decimal_Digit(Item : in Character) return Boolean renames Is_Digit;
   function Is_Hexadecimal_Digit(Item : in Character) return Boolean;
   function Is_Alphanumeric(Item : in Character) return Boolean;
   function Is_Special(Item : in Character) return Boolean;
   function Is_Line_Terminator(Item : in Character) return Boolean;
   function Is_Mark(Item : in Character) return Boolean;
   function Is_Other_Format(Item : in Character) return Boolean;
   function Is_Punctuation_Connector(Item : in Character) return Boolean;
   function Is_Space(Item : in Character) return Boolean;

   function To_Lower(Item : in Character) return Character;
   function To_Upper(Item : in Character) return Character;
   function To_Basic(Item : in Character) return Character;

   function To_Lower(Item : in String) return String;
   function To_Upper(Item : in String) return String;
   function To_Basic(Item : in String) return String;

   subtype ISO_646 is Character range Character'Val(0) .. Character'Val(127);

   function Is_ISO_646(Item : in Character) return Boolean;
   function Is_ISO_646(Item : in String) return Boolean;
   function To_ISO_646(Item : in Character; Substitute : in ISO_646 := ' ') return ISO_646;
   function To_ISO_646(Item : in String; Substitute : in ISO_646 := ' ') return String;

   -- The following are obsolescent. See J.14.

   function Is_Character(Item : in Wide_Character) return Boolean
     renames Conversions.Is_Character;

   function Is_String(Item : in Wide_String) return Boolean
     renames Conversions.Is_String;

   function To_Character
     (Item : in Wide_Character; Substitute : in Character := ' ') return Character
     renames Conversions.To_Character;

   function To_String(Item : in Wide_String; Substitute : in Character := ' ') return String
     renames Conversions.To_String;

   function To_Wide_Character(Item : in Character) return Wide_Character
     renames Conversions.To_Wide_Character;

   function To_Wide_String(Item : in String) return Wide_String
     renames Conversions.To_Wide_String;

end Augusta.Characters.Handling;
