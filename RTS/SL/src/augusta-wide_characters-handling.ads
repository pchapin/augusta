---------------------------------------------------------------------------
-- FILE    : augusta-wide_characters-handling.ads
-- SUBJECT : Specification of wide character handling package.
-- AUTHOR  : (C) Copyright 2013 by the Augusta Contributors
--
-- Please send comments or bug reports to
--
--      Peter C. Chapin <PChapin@vtc.vsc.edu>
---------------------------------------------------------------------------

-- See A.3.5.
package Augusta.Wide_Characters.Handling is
   pragma Pure(Handling);

   function Character_Set_Version return String;
   function Is_Control(Item : in Wide_Character) return Boolean;
   function Is_Letter(Item : in Wide_Character) return Boolean;
   function Is_Lower(Item : in Wide_Character) return Boolean;
   function Is_Upper(Item : in Wide_Character) return Boolean;
   function Is_Digit(Item : in Wide_Character) return Boolean;
   function Is_Decimal_Digit(Item : in Wide_Character) return Boolean renames Is_Digit;
   function Is_Hexadecimal_Digit(Item : in Wide_Character) return Boolean;
   function Is_Alphanumeric(Item : in Wide_Character) return Boolean;
   function Is_Special(Item : in Wide_Character) return Boolean;
   function Is_Line_Terminator(Item : in Wide_Character) return Boolean;
   function Is_Mark(Item : in Wide_Character) return Boolean;
   function Is_Other_Format(Item : in Wide_Character) return Boolean;
   function Is_Punctuation_Connector(Item : in Wide_Character) return Boolean;
   function Is_Space(Item : in Wide_Character) return Boolean;
   function Is_Graphic(Item : in Wide_Character) return Boolean;

   function To_Lower(Item : in Wide_Character) return Wide_Character;
   function To_Upper(Item : in Wide_Character) return Wide_Character;

   function To_Lower(Item : in Wide_String) return Wide_String;
   function To_Upper(Item : in Wide_String) return Wide_String;

end Augusta.Wide_Characters.Handling;
