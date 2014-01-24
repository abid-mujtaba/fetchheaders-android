# Author: Abid H. Mujtaba
# Date: 2013-01-24
#
# Makefile used in conjunction with gradle to compile, install and run applications for testing purposes

# We define the various files involved.
APK=build/apk/fetchheaders-android-debug-unaligned.apk
PKG=com.abid_mujtaba.fetchheaders
MAIN=MainActivity

SRC_PATH=src/com/abid_mujtaba/fetchheaders

# We define the source files which when changed trigger recompilation
SRC=$(wildcard $(SRC_PATH)/*.java) $(wildcard $(SRC_PATH)/*/*.java) $(wildcard res/*/*.xml) $(assets/icons/*.svg)

.PHONY: defult, build, run, stop, restart

# We define the first rule which is the one run by default when only 'make' is executed
default: build

# We define the APK file to be the one whose timestamp is studied by make to determine if it should be compiled
build: $(APK)

$(APK):		$(SRC) build.gradle
	gradle build
	touch $(APK)

# EMPTY target for installing the apk on the device
install: $(APK)
	adb install -r $(APK)
	touch install

# Runs an installed application
run: 	install
	adb shell am start -n $(PKG)/$(PKG).$(MAIN)

# Stops a running applications
stop:
	adb shell am force-stop $(PKG)

# We restart a running application by recursively calling the stop and run commands
restart:
	make stop
	make run
