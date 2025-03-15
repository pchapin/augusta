; Declare Windows API functions
declare i32 @WriteConsoleW(i32, i16*, i32, i32*, i32)
declare i32 @GetStdHandle(i32)
declare void @ExitProcess(i32)

@.hello = private unnamed_addr constant [15 x i16] [
    i16  72, i16 101, i16 108, i16 108, i16 111, 
    i16  44, i16  32, i16  87, i16 111, i16 114, 
    i16 108, i16 100, i16  33, i16  13, i16  10  ; "Hello, World!\r\n" in UTF-16
], align 2

define i32 @main() {
entry:
    ; Get handle to standard output (STD_OUTPUT_HANDLE = -11)
    %stdout = call i32 @GetStdHandle(i32 -11)

    ; Get pointer to "Hello, World!" message
    %msg_ptr = getelementptr [15 x i16], [15 x i16]* @.hello, i32 0, i32 0

    ; Call WriteConsoleW(stdout, message, length, NULL, NULL)
    %written = alloca i32, align 4
    %call = call i32 @WriteConsoleW(i32 %stdout, i16* %msg_ptr, i32 15, i32* %written, i32 0)

    ; Explicitly exit the process
    call void @ExitProcess(i32 0)
    unreachable
}
