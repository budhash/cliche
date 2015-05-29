##################################################################
#          Project Convenience Makefile Wrapper for Maven        #
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

clean:
	@ $(MVN) $(MVNFLAGS) clean

compile: 
	@ $(MVN) $(MVNFLAGS) compile

test:
	@ $(MVN) $(MVNFLAGS) test

qulice:	
	@ $(MVN) clean install -Pqulice

install:
	@ $(MVN) clean install
				
site:
	@ $(MVN) site -Psite
	
gh-pages:	
	@ $(MVN) clean test install site-deploy -Pgh-pages

doc:
	@ $(MVN) $(MVNFLAGS) javadoc:javadoc
		
package: 
	@ $(MVN) $(MVNFLAGS) package

deploy-staging:
	@ $(MVN) clean deploy

release:
	@ $(MVN) release:clean release:prepare release:perform

release-silent:
	@ $(MVN) -B release:clean release:prepare release:perform
	
#clean:
#	@- rm -rf ./bin/*
#	@- rm -rf ./build/*
#	@- rm -rf ./docs/*

update-versions:
	@ $(MVN) versions:update-properties
	
distclean: clean ;

docs: doc ;

javadoc: doc ;

documentation: doc ;

help:
	@ echo "Usage   :  make <target>"
	@ echo "Targets :"
	@ echo "   all ........... Builds the project"
	@ echo "   clean ......... Removes build products"	
	@ echo "   compile ....... Compiles all Java files"	
	@ echo "   test .......... Builds and runs all unit tests"
	@ echo "   qulice ....... Builds and runs various static code analysis tools"	
	@ echo "   install .......... Builds and installs to local repository"	
	@ echo "   docs .......... Generates project documentation."
	@ echo "   deploy-staging .......... Deploys snapshot to staging"
	@ echo "   release .......... Releases to maven central (interactive)"
	@ echo "   release-silent .......... Releases to maven central (non-interactive)"		
	@ echo "   help .......... Prints this help message"