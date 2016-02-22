---------------------------------------------------------------------------
-- FILE    : augusta.ads
-- SUBJECT : The top level package of the Augusta standard library.
-- AUTHOR  : (C) Copyright 2013 by Peter C. Chapin
--
-- Please send comments or bug reports to
--
--      Peter C. Chapin <PChapin@vtc.vsc.edu>
---------------------------------------------------------------------------
package Augusta is
   pragma Pure(Augusta);

   -- Temporary exception for use as a place holder in not implemented bodies.
   Not_Implemented : exception;
end Augusta;
