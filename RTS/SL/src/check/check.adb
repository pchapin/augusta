---------------------------------------------------------------------------
-- FILE    : check.adb
-- SUBJECT : Main procedure of the Augusta standard library unit test program.
-- AUTHOR  : (C) Copyright 2025 by the Augusta Contributors
--
-- Please send comments or bug reports to
--
--      Peter Chapin <spicacality@kelseymountain.org>
---------------------------------------------------------------------------
with AUnit.Run;
with AUnit.Reporter.Text;

with Primary_Suite;

procedure Check is
   procedure Run is new AUnit.Run.Test_Runner(Primary_Suite.Suite);
   Reporter : AUnit.Reporter.Text.Text_Reporter;
begin
   Run(Reporter);
end Check;
