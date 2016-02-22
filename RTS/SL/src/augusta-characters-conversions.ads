---------------------------------------------------------------------------
-- FILE    : augusta-characters-handling.ads
-- SUBJECT : Specification of character conversions package.
-- AUTHOR  : (C) Copyright 2013 by the Augusta Contributors
--
-- Please send comments or bug reports to
--
--      Peter C. Chapin <PChapin@vtc.vsc.edu>
---------------------------------------------------------------------------

-- See A.3.4.
package Augusta.Characters.Conversions is
   pragma Pure(Conversions);

   -- Type Character.
   function Is_Character(Item : in Wide_Character) return Boolean;
   function Is_String(Item : in Wide_String) return Boolean;
   function Is_Character(Item : in Wide_Wide_Character) return Boolean;
   function Is_String(Item : in Wide_Wide_String) return Boolean;
   function Is_Wide_Character(Item : in Wide_Wide_Character) return Boolean;
   function Is_Wide_String(Item : in Wide_Wide_String) return Boolean;

   -- Type Wide_Character.
   function To_Wide_Character(Item : in Character) return Wide_Character;
   function To_Wide_String(Item : in String) return Wide_String;
   function To_Wide_Wide_Character(Item : in Character) return Wide_Wide_Character;
   function To_Wide_Wide_String(Item : in String) return Wide_Wide_String;
   function To_Wide_Wide_Character(Item : in Wide_Character) return Wide_Wide_Character;
   function To_Wide_Wide_String(Item : in Wide_String) return Wide_Wide_String;

   -- Type Wide_Wide_Character.
   function To_Character
     (Item       : in Wide_Character;
      Substitute : in Character := ' ') return Character;

   function To_String
     (Item       : in Wide_String;
      Substitute : in Character := ' ') return String;

   function To_Character
     (Item       : in Wide_Wide_Character;
      Substitute : in Character := ' ') return Character;

   function To_String
     (Item       : in Wide_Wide_String;
      Substitute : in Character := ' ') return String;

   function To_Wide_Character
     (Item       : in Wide_Wide_Character;
      Substitute : in Wide_Character := ' ') return Wide_Character;

   function To_Wide_String
     (Item       : in Wide_Wide_String;
      Substitute : in Wide_Character := ' ') return Wide_String;

end Augusta.Characters.Conversions;
