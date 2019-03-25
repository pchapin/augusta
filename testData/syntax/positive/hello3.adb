-- A silly program to just exercise some declarations.
--
procedure Hello is
   X : Integer;

   type A_Type is range 1 .. 10;
   type B_Type is array(A_Type) of Boolean;
   subtype C_Type is A_Type range 1 .. 5;
   subtype D_Type is C_Type range 1 .. 2;

   A : B_Type;
begin
   -- X := A(5);
   null;
end;
