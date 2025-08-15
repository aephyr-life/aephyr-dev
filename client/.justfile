# Hidden orchestrator for client (iOS + Android later)
set shell := ["bash", "-eu", "-o", "pipefail", "-c"]

# --- iOS config: adjust SCHEME and BUNDLE_ID to match your Xcode project ---
XCODEPROJ := "ios/AephyrApp/AephyrApp.xcodeproj"
SCHEME    := "AephyrApp"            # use `xcodebuild -list -project ios/AephyrApp.xcodeproj` to verify
DEVICE    := "iPhone 15 Pro"        # pick any installed simulator device
BUNDLE_ID := "com.aephyr.app"       # set to your actual bundle id
DERIVED   := "build"                # local derived data so paths are predictable
CONFIG    := "Debug"

# Derived paths used by xcodebuild when -derivedDataPath is set
APP_PATH := "{{DERIVED}}/Build/Products/{{CONFIG}}-iphonesimulator/{{SCHEME}}.app"

# --- Helpers ---
@default:
	@just --list

list-schemes:
	xcodebuild -list -project "{{XCODEPROJ}}"

boot-sim:
	# Boot the simulator and bring it to front (no-op if already booted)
	xcrun simctl boot "{{DEVICE}}" || true
	open -a Simulator

# --- iOS run in Simulator ---
dev-ios: boot-sim
	xcodebuild \
	  -project "{{XCODEPROJ}}" \
	  -scheme "{{SCHEME}}" \
	  -configuration "{{CONFIG}}" \
	  -destination 'platform=iOS Simulator,name={{DEVICE}}' \
	  -derivedDataPath "{{DERIVED}}" \
	  build

	# Install & launch
	xcrun simctl install booted "{{APP_PATH}}" || true
	xcrun simctl launch booted "{{BUNDLE_ID}}" || true

# Clean local build artifacts
clean-ios:
	rm -rf "{{DERIVED}}"

# --- Android placeholders (fill once android app exists) ---
dev-android:
	@echo "TODO: wire up Gradle/AGP once your Android app is initialized."

clean-android:
	@echo "TODO: gradlew clean"


