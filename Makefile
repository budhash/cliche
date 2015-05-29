##################################################################
#          Project Convenience Makefile Wrapper for Maven2       #
##################################################################

# This makefile is just a convenience wrapper for the Maven
# program. The actual building rules for this project may
# be found in the Maven "pom.xml" file located in this folder.

######################### DEFINITIONS ############################

# Define the commandline invocation of Maven if necessary:
ifeq ($(MVN),)
    MVN  := mvn
endif

######################## BUILD TARGETS ###########################

.PHONY: all package compile check test doc docs javadoc clean help

all: 
	@ $(MVN) $(MVNFLAGS) package

install:
	@ $(MVN) clean install
	
site:
	@ $(MVN) site -Psite
	
gh-pages:	
	@ $(MVN) clean test install site-deploy -Pgh-pages
	
package: 
	@ $(MVN) $(MVNFLAGS) package

compile: 
	@ $(MVN) $(MVNFLAGS) compile

test:
	@ $(MVN) $(MVNFLAGS) test

doc:
	@ $(MVN) $(MVNFLAGS) javadoc:javadoc

deploy-staging:
	@ $(MVN) clean deploy

release:
	@ $(MVN) clean deploy -Psonatype -Psign-gpg
	
qulice:	
	@ $(MVN) clean install -Pqulice
	
clean:
	@- rm -rf ./bin/*
	@- rm -rf ./build/*
	@- rm -rf ./docs/*

update-versions:
	@ $(MVN) versions:update-properties
	
distclean: clean ;

docs: doc ;

javadoc: doc ;

documentation: doc ;

help:
	@ echo "Usage   :  make <target>"
	@ echo "Targets :"
	@ echo "   all ........... Builds the project."
	@ echo "   package ....... Archives all *.class files."
	@ echo "   compile ....... Compiles all Java files."
	@ echo "   check ......... Builds and runs all unit tests."
	@ echo "   test .......... Builds and runs all unit tests."
	@ echo "   docs .......... Generates project documentation."
	@ echo "   clean ......... Removes build products."
	@ echo "   help .......... Prints this help message."
