---------------------------------------------------------------------------
-- FILE    : check_strings_fixed.ads
-- SUBJECT : Package containing tests of the fixed string handling package.
-- AUTHOR  : (C) Copyright 2025 by the Augusta Contributors
--
-- Please send comments or bug reports to
--
--      Peter Chapin <spicacality@kelseymountain.org>
---------------------------------------------------------------------------
with AUnit;
with AUnit.Test_Cases;

package Check_Strings_Fixed is

   type Strings_Test is new AUnit.Test_Cases.Test_Case with null record;

   overriding procedure Register_Tests(T : in out Strings_Test);
   overriding function Name(T : in Strings_Test) return AUnit.Message_String;

end Check_Strings_Fixed;
