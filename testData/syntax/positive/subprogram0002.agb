
procedure Hello is
  type T is range 1 .. 10;
  A : T;  -- A is an integer.

  procedure P1 is
    subtype Index_Type is Integer range 1 .. 5;
    type T is array(Index_Type) of Integer;
    B : T;  -- B is an array.
  begin
    A := B; -- Type error. Can't put an array into an integer.
  end;

begin
  null;
end;
