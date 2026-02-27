# Project: Termux Modern

## Objective
Modernize the official Termux Android application by replacing its legacy XML/View-based UI with **Jetpack Compose** and **Material 3**, while maintaining 100% functional compatibility with the original Termux environment and package ecosystem.

## Current Status
- **Source:** Cloned from `https://github.com/termux/termux-app`.
- **Repository:** `https://github.com/The412Banner/termux-modern`.
- **UI Framework:** Integrated Jetpack Compose and Material 3.
- **Activity:** Converted `TermuxActivity.java` to `TermuxActivity.kt` using `setContent`.
- **Terminal View:** Created a `TermuxTerminalView` Compose component that wraps the native `com.termux.view.TerminalView`.
- **Build System:** 
    - Upgraded all modules to **Java 17** (JVM target) to support Compose.
    - Configured Kotlin 2.0.0 and the Compose compiler plugin.
- **Features Implemented:**
    - Basic Terminal UI with Material 3 Scaffold.
    - Session management (switching between multiple sessions).
    - Extra Keys bar (ported from NewTerminal).
    - Reset Terminal functionality.

## Technical Notes
- **Prefix Emulation:** The app continues to use the official `/data/data/com.termux` paths via the existing Termux core logic.
- **Preference Management:** Transitioning from custom `PreferencesManager` to the native `TermuxAppSharedPreferences`.

## Next Steps
1. **Refine UI:** Add a navigation drawer for session management to mirror original Termux functionality.
2. **Settings:** Port the settings screens to Compose.
3. **Theming:** Implement full Material You dynamic color support.
4. **Validation:** Exhaustive testing of `pkg` and `apt` commands in the new UI.
