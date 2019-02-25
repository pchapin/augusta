procedure Main is
   X : Integer;
   Y : Integer;
   Z : Integer;
   B : Boolean;
begin
   -- All correct
   if X + Y < Z then
      while True loop
         null;
      end loop;
      B := Y < Z;
   end if;
   
   -- All wrong! :)
   if X + Y then
      while True < False loop
         X := Y < Z;
         B := 1;
         X := Y + 2*B;
         B := X - Y;
      end loop;
   end if;
end Main;
