
procedure Type_Declare4 is
   type T1 is range 1 .. 10;
   subtype S1 is T1 range 1 .. 5;
   type A1 is array(T1) of Boolean;  -- Use an integer type as an index.
   type A2 is array(S1) of T1;       -- Use a subtype as an index, a user defined type for elements.
   type A3 is array(S1) of A1;       -- An array of arrays.
   
   type A4 is array(Boolean) of Integer;
   type A5 is array(A1) of Integer;
   type A6 is array(Undefined_Index_Type) of Integer;
   type A7 is array(T1) of Undefined_Element_Type;
begin
   null;
end;
