begin
   while X + Y LOOP
      null;
   end loop;

   while X + Y LOOP
      X := A + B;
      Y := C + D;
   end loop;
end
