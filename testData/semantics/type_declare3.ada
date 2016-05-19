
procedure Type_Declare3 is
   type T1 is range 1 .. 10;
   subtype S1 is Boolean range 1 .. 2;
   subtype S2 is Integer range 1 .. 2;
   subtype S3 is T1 range 0 .. 5;
   subtype S4 is T1 range 5 .. 15;
   subtype S5 is T1 range 1 .. 5;
   subtype S6 is S5 range 0 .. 5;
   subtype S7 is S5 range 1 .. 6;
   subtype S8 is S5 range 2 .. 4;
begin
   null;
end;
