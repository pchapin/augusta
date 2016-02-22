---------------------------------------------------------------------------
-- FILE    : check_strings_fixed.adb
-- SUBJECT : Package containing tests of the fixed string handling package.
-- AUTHOR  : (C) Copyright 2013 by Peter C. Chapin
--
-- Please send comments or bug reports to
--
--      Peter C. Chapin <PChapin@vtc.vsc.edu>
---------------------------------------------------------------------------
with AUnit.Assertions; use AUnit.Assertions;
with Augusta.Strings.Fixed;

package body Check_Strings_Fixed is

   procedure Test_Move(T : in out AUnit.Test_Cases.Test_Case'Class) is
      S0 : String(1 .. 0);
      S1 : String(1 .. 1) := "X";
      S2 : String(2 .. 3) := "XY";

      -- Initialize destination strings so that the comparisons below are well defined even if
      -- no move is done.
      D0 : String(1 .. 0);
      D1 : String(1 .. 1) := ".";
      D2 : String(1 .. 2) := "..";
   begin
      -- Test moves between strings of the same length.
      Augusta.Strings.Fixed.Move(Source => S0, Target => D0);  -- Should not raise an exception.
      Augusta.Strings.Fixed.Move(Source => S1, Target => D1);
      Augusta.Strings.Fixed.Move(Source => S2, Target => D2);  -- Different bounds.
      Augusta.Strings.Fixed.Move(Source => S0, Target => S0);  -- Should not raise an exception.
      Augusta.Strings.Fixed.Move(Source => S1, Target => S1);
      Augusta.Strings.Fixed.Move(Source => S2, Target => S2);
      Assert(D1 = "X",  "Single character string to same size destination");
      Assert(D2 = "XY", "Multi-character string to same size destination");
      Assert(S1 = "X",  "Single character string to same space");
      Assert(S2 = "XY", "Multi-character string to same space");
   end Test_Move;


   procedure Register_Tests(T : in out Strings_Test) is
   begin
      AUnit.Test_Cases.Registration.Register_Routine(T, Test_Move'Access, "Move Test");
   end Register_Tests;


   function Name(T : in Strings_Test) return AUnit.Message_String is
   begin
      return AUnit.Format("Fixed Strings");
   end Name;

end Check_Strings_Fixed;
