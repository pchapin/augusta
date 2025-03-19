; Declare macOS system call functions
declare i64 @syscall(i64, ...)

@.hello = private unnamed_addr constant [14 x i8] [
    i8  72, i8 101, i8 108, i8 108, i8 111, 
    i8  44, i8  32, i8  87, i8 111, i8 114, 
    i8 108, i8 100, i8  33, i8  10   ; "Hello, World!\n" in UTF-8
], align 1

define i32 @main() {
entry:
    ; Get pointer to "Hello, World!" message
    %msg_ptr = getelementptr [14 x i8], [14 x i8]* @.hello, i32 0, i32 0

    ; Call write(1, message, length)
    %call = call i64 @syscall(i64 0x2000004, i64 1, i8* %msg_ptr, i64 14)

    ; Explicitly exit the process
    call i64 @syscall(i64 0x2000001, i64 0)
    unreachable
}