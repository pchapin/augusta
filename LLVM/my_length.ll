
define i32 @my_length(i8 *%p) {
entry:
  %result_ptr = alloca i32
  store i32 0, i32* %result_ptr
  br label %loop_top

loop_top:
  %tmp1 = phi i8* [ %p, %entry], [ %tmp5, %loop_body ]
  %tmp2 = load i8, i8* %tmp1
  %cond = icmp ne i8 %tmp2, 0
  br i1 %cond, label %loop_body, label %loop_done

loop_body:
  %tmp3 = load i32, i32* %result_ptr
  %tmp4 = add i32 %tmp3, 1
  store i32 %tmp4, i32* %result_ptr
  %tmp5 = getelementptr i8, i8* %tmp1, i8 1
  br label %loop_top

loop_done:
  %tmp6 = load i32, i32* %result_ptr
  ret i32 %tmp6
}
