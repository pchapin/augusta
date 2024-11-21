---------------------------------------------------------------------------
-- FILE    : check_characters_handling.adb
-- SUBJECT : Package containing tests of the character handling package.
-- AUTHOR  : (C) Copyright 2025 by the Augusta Contributors
--
-- Please send comments or bug reports to
--
--      Peter Chapin <spicacality@kelseymountain.org>
---------------------------------------------------------------------------
with AUnit.Assertions; use AUnit.Assertions;
with Augusta.Characters.Handling; use Augusta.Characters.Handling;

package body Check_Characters_Handling is

   procedure Test_Is_Control(T : in out AUnit.Test_Cases.Test_Case'Class) is
   begin
      Assert(    Is_Control(Character'Val(  0)), "Failed #1");
      Assert(    Is_Control(Character'Val( 31)), "Failed #2");
      Assert(    Is_Control(Character'Val(127)), "Failed #3");
      Assert(    Is_Control(Character'Val(159)), "Failed #4");
      Assert(not Is_Control(Character'Val( 32)), "Failed #5");
      Assert(not Is_Control(Character'Val(126)), "Failed #6");
      Assert(not Is_Control(Character'Val(160)), "Failed #7");
   end Test_Is_Control;


   procedure Register_Tests(T : in out Characters_Test) is
   begin
      AUnit.Test_Cases.Registration.Register_Routine
        (T, Test_Is_Control'Access, "Is_Control Test");
   end Register_Tests;


   function Name(T : in Characters_Test) return AUnit.Message_String is
   begin
      return AUnit.Format("Character Handling");
   end Name;

end Check_Characters_Handling;
