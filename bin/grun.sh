#
# This is the testing tool described in "Definitive ANTRL 4."
#

cd build/production/Augusta
java -cp .:../../../../../lib/antlr-4.5.1.jar org.antlr.v4.runtime.misc.TestRig org.pchapin.augusta.Augusta $1 $2 $3 $4
cd ../../..
