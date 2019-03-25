-- A silly program to exercise some error conditions.
--
procedure Hello is
  X : Integer;
  Y : Integer;
  Z : Integer := Z + 1;  -- Semantic error (Z undefined in initializer).

  X : Integer;  -- Semantic error (duplicate declaration of X).
  A : String;   -- Semantic error (unknown type name).
begin
  X := A + 1;   -- Semantic error (A is undefined).
  A := X + Y;   -- Semantic error (A is undefined).
end;
