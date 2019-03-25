begin
   -- Basic IF
   if X + Y then
      A := B;
   end if;
   
   -- IF with ELSE
   if X + Y then
      A := B;
   else
      C := D;
   end if;
   
   -- IF with ELSIF and no ELSE
   if X + Y then
      A := B;
   elsif X + Y then
      C := D;
      C := D;
   end if;
   
   -- IF with ELSIF and ELSE
   if X + Y then
      X := B;
   elsif X + Y then
      C := D;
      null;
   else
      E := F;
   end if;
   
   -- IF with multiple ELSIF and no ELSE
   if X + Y then
      X := B;
   elsif X + Y then
      C := D;
   elsif X + Y then
      E := F;
      E := F;
   end if;
   
   -- IF with multiple ELSIF and ELSE
   if X + Y then
      X := B;
      X := B;
   elsif X + Y then
      C := D;
   elsif X + Y then
      E := F;
   else
      G := H;
   end if;
end

