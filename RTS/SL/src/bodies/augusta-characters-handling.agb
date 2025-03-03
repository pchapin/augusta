---------------------------------------------------------------------------
-- FILE    : augusta-characters-handling.adb
-- SUBJECT : Body of character handling package.
-- AUTHOR  : (C) Copyright 2025 by the August Contributors
--
-- Please send comments or bug reports to
--
--      Peter Chapin <spicacality@kelseymountain.org>
---------------------------------------------------------------------------

package body Augusta.Characters.Handling is

   -- Traditionally character classification functions are implemented using an array of bit
   -- flags. Each element in the array corresponds to a single character and the flags in that
   -- element define the character's classes. Checking if a character is in a particular class
   -- then entails looking up the flags and applying an appropriate mask.
   --
   -- The implementation below does without the array of bit flags and thus may consume less
   -- memory (but the code is more complicated so code space is no doubt greater). However,
   -- executing the elaborate tests might also be slower OTOH, LLVM might be able to optimize
   -- these tests significantly. How much it can do that is unclear to us at this time. On a
   -- modern system using our advanced back-end, the approach here might actually be preferrable
   -- overall.
   --
   -- TODO: Execute some benchmark tests to explore how efficient (or not) this implementation
   -- *really* is.

   function Is_Control(Item : in Character) return Boolean is
      subtype Low_Control is Character range Character'Val(0) .. Character'Val(31);
      subtype High_Control is Character range Character'Val(127) .. Character'Val(159);
   begin
      return (Item in Low_Control) or (Item in High_Control);
   end Is_Control;


   function Is_Graphic(Item : in Character) return Boolean is
      subtype Low_Graphic is Character range Character'Val(32) .. Character'Val(126);
      subtype High_Graphic is Character range Character'Val(160) .. Character'Val(255);
   begin
      return (Item in Low_Graphic) or (Item in High_Graphic);
   end Is_Graphic;


   function Is_Letter(Item : in Character) return Boolean is
      subtype Uppercase_Letter is Character range 'A' .. 'Z';
      subtype Lowercase_Letter is Character range 'a' .. 'z';
      subtype High_Range is Character range Character'Val(192) .. Character'Val(255);
   begin
      return
        (Item in Uppercase_Letter) or
        (Item in Lowercase_Letter) or
        ((Item in High_Range) and not (Item = Character'Val(215) or Item = Character'Val(247)));
   end Is_Letter;


   function Is_Lower(Item : in Character) return Boolean is
      subtype Lowercase_Letter is Character range 'a' .. 'z';
      subtype High_Range is Character range Character'Val(223) .. Character'Val(255);
   begin
      return
        (Item in Lowercase_Letter) or
        ((Item in High_Range) and not (Item = Character'Val(247)));
   end Is_Lower;


   function Is_Upper(Item : in Character) return Boolean is
      subtype Uppercase_Letter is Character range 'A' .. 'Z';
      subtype High_Range is Character range Character'Val(192) .. Character'Val(222);
   begin
      return
        (Item in Uppercase_Letter) or
        ((Item in High_Range) and not (Item = Character'Val(215)));
   end Is_Upper;


   function Is_Basic(Item : in Character) return Boolean is
      subtype Uppercase_Letter is Character range 'A' .. 'Z';
      subtype Lowercase_Letter is Character range 'a' .. 'z';
   begin
      raise Not_Implemented;  -- This function needs to deal with the high basic characters.
      return (Item in Uppercase_Letter) or (Item in Lowercase_Letter);
   end Is_Basic;


   function Is_Digit(Item : in Character) return Boolean is
      subtype Digit_Letter is Character range '0' .. '9';
   begin
      return (Item in Digit_Letter);
   end Is_Digit;


   function Is_Hexadecimal_Digit(Item : in Character) return Boolean is
      subtype Upper_Hex_Digit is Character range 'A' .. 'F';
      subtype Lower_Hex_Digit is Character range 'a' .. 'f';
   begin
      return Is_Digit(Item) or (Item in Upper_Hex_Digit) or (Item in Lower_Hex_Digit);
   end Is_Hexadecimal_Digit;


   function Is_Alphanumeric(Item : in Character) return Boolean is
   begin
      return Is_Letter(Item) or Is_Digit(Item);
   end Is_Alphanumeric;


   function Is_Special(Item : in Character) return Boolean is
   begin
      return Is_Graphic(Item) and not Is_Alphanumeric(Item);
   end Is_Special;


   function Is_Line_Terminator(Item : in Character) return Boolean is
      subtype Low_Line_Terminator is Character range Character'Val(10) .. Character'Val(13);
   begin
      return (Item in Low_Line_Terminator) or (Item = Character'Val(133));
   end Is_Line_Terminator;


   function Is_Mark(Item : in Character) return Boolean is
   begin
      return False;  -- No items of type Character are marks!
   end Is_Mark;


   function Is_Other_Format(Item : in Character) return Boolean is
   begin
      return Item = Character'Val(173);
   end Is_Other_Format;


   function Is_Punctuation_Connector(Item : in Character) return Boolean is
   begin
      return Item = Character'Val(95);
   end Is_Punctuation_Connector;


   function Is_Space(Item : in Character) return Boolean is
   begin
      return (Item = Character'Val(32) or Item = Character'Val(160));
   end Is_Space;


   function To_Lower(Item : in Character) return Character is
   begin
      raise Not_Implemented;
      return ' ';
   end To_Lower;


   function To_Upper(Item : in Character) return Character is
   begin
      raise Not_Implemented;
      return ' ';
   end To_Upper;


   function To_Basic(Item : in Character) return Character is
   begin
      raise Not_Implemented;
      return ' ';
   end To_Basic;


   function To_Lower(Item : in String) return String is
      Result : String(1 .. Item'Length);  -- Lower bound of result required to be 1.
   begin
      for I in Item'Range loop
         Result(1 + (I - Item'First)) := To_Lower(Item(I));
      end loop;
      return Result;
   end To_Lower;


   function To_Upper(Item : in String) return String is
      Result : String(1 .. Item'Length);  -- Lower bound of result required to be 1.
   begin
      for I in Item'Range loop
         Result(1 + (I - Item'First)) := To_Upper(Item(I));
      end loop;
      return Result;
   end To_Upper;


   function To_Basic(Item : in String) return String is
      Result : String(1 .. Item'Length);  -- Lower bound of result required to be 1.
   begin
      for I in Item'Range loop
         Result(1 + (I - Item'First)) := To_Basic(Item(I));
      end loop;
      return Result;
   end To_Basic;


   function Is_ISO_646(Item : in Character) return Boolean is
   begin
      return (Item in ISO_646);
   end Is_ISO_646;


   function Is_ISO_646(Item : in String) return Boolean is
   begin
      return (for all I in Item'Range => Is_ISO_646(Item(I)));
   end Is_ISO_646;


   function To_ISO_646(Item : in Character; Substitute : in ISO_646 := ' ') return ISO_646 is
   begin
      return (if Is_ISO_646(Item) then Item else Substitute);
   end To_ISO_646;


   function To_ISO_646(Item : in String; Substitute : in ISO_646 := ' ') return String is
      Result : String(1 .. Item'Length);
   begin
      for I in Item'Range loop
         Result(1 + (I - Item'First)) := To_ISO_646(Item(I), Substitute);
      end loop;
      return Result;
   end To_ISO_646;


end Augusta.Characters.Handling;
