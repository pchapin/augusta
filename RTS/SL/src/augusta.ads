---------------------------------------------------------------------------
-- FILE    : augusta.ads
-- SUBJECT : The top level package of the Augusta standard library.
-- AUTHOR  : (C) Copyright 2025 by the Augusta Contributors
--
-- Please send comments or bug reports to
--
--      Peter Chapin <spicacality@kelseymountain.org>
---------------------------------------------------------------------------
package Augusta is
   pragma Pure(Augusta);

   -- Temporary exception for use as a place holder in not implemented bodies.
   Not_Implemented : exception;
end Augusta;
