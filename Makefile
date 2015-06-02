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

ifeq ($(RELEASE_VERSION),)
    RELEASE_VERSION  := $(shell xmllint --xpath "/*[local-name() = 'project']/*[local-name() = 'version']/text()" pom.xml | perl -pe 's/-SNAPSHOT//')
endif

ifeq ($(NEXT_VERSION),)
    NEXT_VERSION  := $(shell echo $(RELEASE_VERSION) | perl -pe 's{^(([0-9]\.)+)?([0-9]+)$$}{$$1 . ($$3 + 1)}e')
endif

ifneq (,$(findstring -SNAPSHOT,$(RELEASE_VERSION)))
	RELEASE_VERSION_NSNP = $(shell echo $(RELEASE_VERSION) | perl -pe 's/-SNAPSHOT//')
else
	RELEASE_VERSION_NSNP = $(RELEASE_VERSION)
endif

ifeq (,$(findstring -SNAPSHOT,$(NEXT_VERSION)))
	NEXT_VERSION_SNP = $(NEXT_VERSION)-SNAPSHOT
else
	NEXT_VERSION_SNP = $(NEXT_VERSION)
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

release-prepare:
	@ $(MVN) release:clean release:prepare

release-perform:
	@ $(MVN) release:perform -Prelease-sign

release-rollback:
	@ $(MVN) release:rollback
			
release-all:
	@ $(MVN) release:clean release:prepare release:perform -Prelease-sign

release-silent:
	@ $(MVN) -B release:clean release:prepare release:perform -Prelease-sign

version-bump:
	@echo setting version: $(NEXT_VERSION_SNP)
	@ $(MVN) versions:set -DgenerateBackupPoms=false -DnewVersion=$(NEXT_VERSION_SNP)

version-release:
	@echo setting version: $(RELEASE_VERSION_NSNP)
	@ $(MVN) versions:set -DgenerateBackupPoms=false -DnewVersion=$(RELEASE_VERSION_NSNP)

nexus-deploy:
	@ $(MVN) -Pnexus-release -Prelease-sign clean verify source:jar javadoc:jar gpg:sign
					
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