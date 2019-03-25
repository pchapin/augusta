-- A silly program to just exercise some computations.
--
procedure Hello is
  X : Integer;
  Y : Integer := 5;
begin
  X := X + X*Y - 42;
  Y := X/3 + 1;
  X := Y/(3 + 1);
  Y := X - 2#1111_1111#;
  X := Y * 1E2;
end;
