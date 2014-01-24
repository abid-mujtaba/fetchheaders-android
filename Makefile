# Author: Abid H. Mujtaba
# Date: 2013-01-24
#
# Makefile used in conjunction with gradle to compile, install and run applications for testing purposes

.PHONY: build, run, install, stop, restart

build:
	gradle build

install:
	adb install -r build/apk/fetchheaders-android-debug-unaligned.apk
	touch build/apk/fetchheaders-android-debug-unaligned.apk

# Runs an installed application
run:
	adb shell am start -n com.abid_mujtaba.fetchheaders/com.abid_mujtaba.fetchheaders.MainActivity

# Stops a running applications
stop:
	adb shell am force-stop com.abid_mujtaba.fetchheaders

# We restart a running application by recursively calling the stop and run commands
restart:
	make stop
	make run
