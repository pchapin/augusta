---------------------------------------------------------------------------
-- FILE    : primary_suite.adb
-- SUBJECT : The main test suite of the Augusta standard library unit test program.
-- AUTHOR  : (C) Copyright 2013 by Peter C. Chapin
--
-- Please send comments or bug reports to
--
--      Peter C. Chapin <PChapin@vtc.vsc.edu>
---------------------------------------------------------------------------
with Check_Characters_Handling;
with Check_Strings_Fixed;

package body Primary_Suite is
   use AUnit.Test_Suites;

   -- The suite itself.
   Suite_Object : aliased Test_Suite;

   -- The various tests in this suite. Low level tests should be done first.
   Test_1 : aliased Check_Characters_Handling.Characters_Test;
   Test_2 : aliased Check_Strings_Fixed.Strings_Test;

   -- Function to return an access to the configured suite
   function Suite return Access_Test_Suite is
   begin
      Add_Test(Suite_Object'Access, Test_1'Access);
      Add_Test(Suite_Object'Access, Test_2'Access);
      return Suite_Object'Access;
   end Suite;

end Primary_Suite;
