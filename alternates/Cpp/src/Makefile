
# Compiler and flags
CXX = g++
CXXFLAGS = -Wall -std=c++17 -I../hpp -O2

# Target executable
TARGET = agc++

# Source and object files
SRCS = main.cpp errors.cpp Lexer.cpp SourceFile.cpp ada.tab.cpp
OBJS = $(SRCS:.cpp=.o)

# Default rule: build the target
all: $(TARGET)

ada.tab.cpp ada.tab.hpp:	ada.ypp
	bison --language=c++ -d -t ada.ypp

# Link object files to create the executable
$(TARGET): $(OBJS)
	$(CXX) $(CXXFLAGS) -o $@ $^

main.o:	ada.tab.hpp

# Compile source files into object files
%.o: %.cpp
	$(CXX) $(CXXFLAGS) -c $< -o $@

# Clean up build files
clean:
	rm -f $(OBJS) $(TARGET) ada.tab.*

# Phony targets
.PHONY: all clean
