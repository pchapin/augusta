---------------------------------------------------------------------------
-- FILE    : augusta-strings-fixed.adb
-- SUBJECT : Augusta standard library fixed length string handling package.
-- AUTHOR  : (C) Copyright 2013 by Peter C. Chapin
--
-- Please send comments or bug reports to
--
--      Peter C. Chapin <PChapin@vtc.vsc.edu>
---------------------------------------------------------------------------

package body Augusta.Strings.Fixed is

   -- TODO: FINISH ME!
   -- "Copy" procedure for strings of possibly different lengths
   procedure Move
     (Source  : in  String;
      Target  : out String;
      Drop    : in  Truncation := Error;
      Justify : in  Alignment := Left;
      Pad     : in  Character := Space) is

      Workspace : String(1 .. Target'Length) := (others => Pad);
   begin
      -- This works only if Source and Target are the same size.
      Workspace := Source;

      Target := Workspace;
   end Move;


   function Index
     (Source  : in String;
      Pattern : in String;
      From    : in Positive;
      Going   : in Direction := Forward;
      Mapping : in Maps.Character_Mapping := Maps.Identity) return Natural is
   begin
      raise Not_Implemented;
      return 0;
   end Index;


   function Index
     (Source  : in String;
      Pattern : in String;
      From    : in Positive;
      Going   : in Direction := Forward;
      Mapping : in Maps.Character_Mapping_Function) return Natural is
   begin
      raise Not_Implemented;
      return 0;
   end Index;


   function Index
     (Source  : in String;
      Pattern : in String;
      Going   : in Direction := Forward;
      Mapping : in Maps.Character_Mapping := Maps.Identity) return Natural is
   begin
      raise Not_Implemented;
      return 0;
   end Index;


   function Index
     (Source  : in String;
      Pattern : in String;
      Going   : in Direction := Forward;
      Mapping : in Maps.Character_Mapping_Function) return Natural is
   begin
      raise Not_Implemented;
      return 0;
   end Index;


   function Index
     (Source : in String;
      Set    : in Maps.Character_Set;
      From   : in Positive;
      Test   : in Membership := Inside;
      Going  : in Direction := Forward) return Natural is
   begin
      raise Not_Implemented;
      return 0;
   end Index;


   function Index
     (Source : in String;
      Set    : in Maps.Character_Set;
      Test   : in Membership := Inside;
      Going  : in Direction := Forward) return Natural is
   begin
      raise Not_Implemented;
      return 0;
   end Index;


   function Index_Non_Blank
     (Source : in String;
      From   : in Positive;
      Going  : in Direction := Forward) return Natural is
   begin
      raise Not_Implemented;
      return 0;
   end Index_Non_Blank;


   function Index_Non_Blank
     (Source : in String;
      Going  : in Direction := Forward) return Natural is
   begin
      raise Not_Implemented;
      return 0;
   end Index_Non_Blank;


   function Count
     (Source  : in String;
      Pattern : in String;
      Mapping : in Maps.Character_Mapping := Maps.Identity) return Natural is
   begin
      raise Not_Implemented;
      return 0;
   end Count;


   function Count
     (Source  : in String;
      Pattern : in String;
      Mapping : in Maps.Character_Mapping_Function) return Natural is
   begin
      raise Not_Implemented;
      return 0;
   end Count;


   function Count
     (Source : in String;
      Set    : in Maps.Character_Set) return Natural is
   begin
      raise Not_Implemented;
      return 0;
   end Count;


   procedure Find_Token
     (Source : in  String;
      Set    : in  Maps.Character_Set;
      From   : in  Positive;
      Test   : in  Membership;
      First  : out Positive;
      Last   : out Natural) is
   begin
      raise Not_Implemented;
   end Find_Token;


   procedure Find_Token
     (Source : in  String;
      Set    : in  Maps.Character_Set;
      Test   : in  Membership;
      First  : out Positive;
      Last   : out Natural) is
   begin
      raise Not_Implemented;
   end Find_Token;


   function Translate
     (Source  : in String;
      Mapping : in Maps.Character_Mapping) return String is
   begin
      raise Not_Implemented;
      return "";
   end Translate;


   procedure Translate
     (Source  : in out String;
      Mapping : in Maps.Character_Mapping) is
   begin
      raise Not_Implemented;
   end Translate;


   function Translate
     (Source  : in String;
      Mapping : in Maps.Character_Mapping_Function) return String is
   begin
      raise Not_Implemented;
      return "";
   end Translate;


   procedure Translate
     (Source  : in out String;
      Mapping : in Maps.Character_Mapping_Function) is
   begin
      raise Not_Implemented;
   end Translate;


   function Replace_Slice
     (Source : in String;
      Low    : in Positive;
      High   : in Natural;
      By     : in String) return String is
   begin
      raise Not_Implemented;
      return "";
   end Replace_Slice;


   procedure Replace_Slice
     (Source  : in out String;
      Low     : in Positive;
      High    : in Natural;
      By      : in String;
      Drop    : in Truncation := Error;
      Justify : in Alignment := Left;
      Pad     : in Character := Space) is
   begin
      raise Not_Implemented;
   end Replace_Slice;


   function Insert
     (Source   : in String;
      Before   : in Positive;
      New_Item : in String) return String is
   begin
      raise Not_Implemented;
      return "";
   end Insert;


   procedure Insert
     (Source   : in out String;
      Before   : in Positive;
      New_Item : in String;
      Drop     : in Truncation := Error) is
   begin
      raise Not_Implemented;
   end Insert;


   function Overwrite
     (Source   : in String;
      Position : in Positive;
      New_Item : in String) return string is
   begin
      raise Not_Implemented;
      return "";
   end Overwrite;


   procedure Overwrite
     (Source   : in out String;
      Position : in Positive;
      New_Item : in String;
      Drop     : in Truncation := Right) is
   begin
      raise Not_Implemented;
   end Overwrite;


   function Delete
     (Source   : in String;
      From     : in Positive;
      Throught : in Natural) return String is
   begin
      raise Not_Implemented;
      return "";
   end Delete;


   procedure Delete
     (Source  : in out String;
      From    : in Positive;
      Through : in Natural;
      Justify : in Alignment := Left;
      Pad     : in Character := Space) is
   begin
      raise Not_Implemented;
   end Delete;


   function Trim
     (Source : in String;
      Side   : in Trim_End) return String is
   begin
      raise Not_Implemented;
      return "";
   end Trim;


   procedure Trim
     (Source  : in out String;
      Side    : in Trim_end;
      Justify : in Alignment := Left;
      Pad     : in Character := Space) is
   begin
      raise Not_Implemented;
   end Trim;


   function Trim
     (Source : in String;
      Left   : in Maps.Character_Set;
      Right  : in Maps.Character_Set) return String is
   begin
      raise Not_Implemented;
      return "";
   end Trim;


   procedure Trim
     (Source  : in out String;
      Left    : in Maps.Character_Set;
      Right   : in Maps.Character_Set;
      Justify : in Alignment := Strings.Left;
      Pad     : in Character := Space) is
   begin
      raise Not_Implemented;
   end Trim;


   function Head
     (Source : in String;
      Count  : in Natural;
      Pad    : in Character := Space) return String is
   begin
      raise Not_Implemented;
      return "";
   end Head;


   procedure Head
     (Source  : in out String;
      Count   : in Natural;
      Justify : in Alignment := Left;
      Pad     : in Character := Space) is
   begin
      raise Not_Implemented;
   end Head;


   function Tail
     (Source : in String;
      Count  : in Natural;
      Pad    : in Character := Space) return String is
   begin
      raise Not_Implemented;
      return "";
   end Tail;


   procedure Tail
     (Source  : in out String;
      Count   : in Natural;
      Justify : in Alignment := Left;
      Pad     : in Character := Space) is
   begin
      raise Not_Implemented;
   end Tail;


   function "*"
     (Left  : in Natural;
      Right : in Character) return String is
   begin
      raise Not_Implemented;
      return "";
   end "*";


   function "*"
     (Left  : in Natural;
      Right : in String) return String is
   begin
      raise Not_Implemented;
      return "";
   end "*";

end Augusta.Strings.Fixed;
