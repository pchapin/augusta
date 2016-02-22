---------------------------------------------------------------------------
-- FILE    : augusta-strings.ads
-- SUBJECT : Augusta standard library string handling package.
-- AUTHOR  : (C) Copyright 2011 by Peter C. Chapin
--
-- Please send comments or bug reports to
--
--      Peter C. Chapin <PChapin@vtc.vsc.edu>
---------------------------------------------------------------------------
package Augusta.Strings is
   pragma Pure(Strings);

   Space             : constant Character           := ' ';
   Wide_Space        : constant Wide_Character      := ' ';
   Wide_Wide_Space   : constant Wide_Wide_Character := ' ';

   Length_Error      : exception;
   Pattern_Error     : exception;
   Index_Error       : exception;
   Translation_Error : exception;

   type Alignment  is (Left, Right, Center);
   type Truncation is (Left, Right, Error);
   type Membership is (Inside, Outside);
   type Direction  is (Forward, Backward);
   type Trim_End   is (Left, Right, Both);

end Augusta.Strings;
