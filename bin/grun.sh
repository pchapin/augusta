#
# This is the testing tool described in "Definitive ANTRL 4."
#

# Set up the class path to grab the jar files from the Ivy cache.
export CPATH=$HOME/.ivy2/cache/org.antlr/antlr4/jars/antlr4-4.7.2.jar
export CPATH=$CPATH:$HOME/.ivy2/cache/org.antlr/antlr4-runtime/jars/antlr4-runtime-4.7.2.jar
export CPATH=$CPATH:$HOME/.ivy2/cache/org.abego.treelayout/org.abego.treelayout.core/bundles/org.abego.treelayout.core-1.0.3.jar

# Execute the tool on the Ada grammar. Note that the test data is at ../../../testData
cd target/scala-2.12/classes
java -cp .:$CPATH org.antlr.v4.gui.TestRig org.pchapin.augusta.Ada $*
cd ../../..
