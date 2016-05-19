
procedure Hello is

  A : Integer;

  procedure P1(X : Integer; X : Integer) is
    X : Integer;
  begin
    null;
  end;

  type T is range 1 .. 10;
  B : T;

  procedure P2(X : T; Y : Integer) is
    procedure P3 is
      X : Integer;
      Z : T;
    begin
      X := X + Y;
      X := X + A;
      Z := B;
    end;
  begin  -- P2
    null;
  end;

begin  -- Hello
  null;
end;
