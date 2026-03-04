# Toy Robot Simulator

The following should be written in Kotlin

## Description:
- The application is a simulation of a toy robot moving on a square tabletop, of dimensions 5 units x 5 units.
- There are no other obstructions on the table surface.
- The robot is free to roam around the surface of the table, but must be prevented from falling to destruction. Any movement
  that would result in the robot falling from the table must be prevented, however further valid movement commands must still
  be allowed.

## Commands
- *PLACE X,Y,F*: will put the toy robot on the table in position X,Y and facing NORTH, SOUTH, EAST or WEST. The origin (0,0) can be considered to be the SOUTH WEST most corner. The first valid command to the robot is a PLACE command, after that, any sequence of commands may be issued, in any order, including another PLACE command. The application should discard all commands in the sequence until a valid PLACE command has been executed.
- *MOVE*: will move the toy robot one unit forward in the direction it is currently facing.
- *LEFT*: will rotate the robot 90 degrees in the specified direction without changing the position of the robot.
- *RIGHT*: will rotate the robot 90 degrees in the specified direction without changing the position of the robot.
- *REPORT*: will announce the X,Y and F of the robot. This can be in any form, but standard output is sufficient.

## Notes
- A robot that is not on the table can choose the ignore the MOVE, LEFT, RIGHT and REPORT commands.
- Inputs must be provided through a user interface, and the robot’s movement and position can be displayed either as text output or graphically (on a grid).
- Provide test data to exercise the application.


## Constraints

The toy robot must not fall off the table during movement. This also includes the initial placement of the toy robot.
Any move that would cause the robot to fall must be ignored.

## Example Input and Output

a)
PLACE 0,0,NORTH
MOVE
REPORT
Output: 0,1,NORTH

b)
PLACE 0,0,NORTH
LEFT
REPORT
Output: 0,0,WEST

c)
PLACE 1,2,EAST
MOVE
MOVE
LEFT
MOVE
REPORT
Output: 3,3,NORTH


## Deliverables
The source files, the test data and any test code as well as instructions on how to build, run and test it.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.3.10 |
| UI | Jetpack Compose (BOM 2026.02.01) |
| Architecture | Clean Architecture — domain / presentation |
| State management | `StateFlow` + `ViewModel` |
| Dependency injection | Koin 4.1.1 |
| Unit testing | JUnit 4 + MockK 1.14.9 |
| UI testing | Compose Test |
| Min SDK | 24 |
| Target SDK | 36 |

---

## Architecture

The project follows **Clean Architecture** with a strict separation between domain and presentation:

```
com.example.toyrobot/
├── ToyRobotApp.kt                  
├── di/
│   └── AppModule.kt                # Koin module — use cases + ViewModel
├── domain/
│   ├── model/
│   │   ├── Constants.kt            # TABLE_SIZE = 5
│   │   ├── Direction.kt            # Enum: NORTH, EAST, SOUTH, WEST with turnLeft/turnRight
│   │   ├── Robot.kt                # Immutable data class (x, y, facing)
│   │   └── RobotResult.kt          # Immutable result: (robot, success)
│   └── usecase/
│       ├── PlaceRobotUseCase.kt    # PLACE — validates position then creates new Robot
│       ├── MoveRobotUseCase.kt     # MOVE — advances one unit in facing direction
│       ├── TurnLeftUseCase.kt      # LEFT — rotates 90° counter-clockwise
│       ├── TurnRightUseCase.kt     # RIGHT — rotates 90° clockwise
│       ├── ReportRobotUseCase.kt   # REPORT — returns "x,y,FACING" string
│       └── ValidatePositionUseCase.kt  # Checks (x,y) is within 0–TABLE_SIZE bounds
└── presentation/
    ├── MainActivity.kt             
    ├── MainScreen.kt               
    ├── ViewState.kt                
    ├── theme/                      
    ├── viewmodel/
    │   └── RobotViewModel.kt       
    └── ui/
        ├── ActionButtonsLayout.kt  
        ├── CommandLogLayout.kt     
        ├── PlaceControls.kt        
        └── TableGridLayout.kt      
```

---

## How to Build

### Prerequisites
- Android Studio Meerkat (2024.3) or later
- JDK 11+
- Android SDK with API 36

### Steps
1. Clone or open this project in Android Studio
2. Let Gradle sync complete

---

## How to Run

### On an Emulator or Device
1. Connect a device or start an Android emulator (API 24+)
2. Press **Run ▶** in Android Studio, or:
```bash
./gradlew installDebug
```

---

## How to Test

### Unit Tests — JVM (no device needed)
```bash
./gradlew testDebugUnitTest
```
Results: `app/build/reports/tests/testDebugUnitTest/index.html`

### Instrumented Tests — device or emulator required
```bash
./gradlew connectedDebugAndroidTest
```

---

## Test Data

The scenarios below cover all key behaviours. Each row shows the sequence of commands entered in the UI and the expected REPORT output or log entry.

### PLACE

| # | Commands | Expected output |
|---|---|---|
| 1 | `PLACE 0,0,NORTH` → `REPORT` | `0,0,NORTH` |
| 2 | `PLACE 4,4,SOUTH` → `REPORT` | `4,4,SOUTH` |
| 3 | `PLACE 5,5,NORTH` → `REPORT` | `PLACE 5,5,NORTH (ignored)` — out of bounds |
| 4 | `PLACE 0,0,NORTH` → `PLACE 2,3,EAST` → `REPORT` | `2,3,EAST` — second PLACE repositions robot |

### MOVE

| # | Commands | Expected output |
|---|---|---|
| 5 | `PLACE 0,0,NORTH` → `MOVE` → `REPORT` | `0,1,NORTH` |
| 6 | `PLACE 0,0,EAST` → `MOVE` → `REPORT` | `1,0,EAST` |
| 7 | `PLACE 0,0,SOUTH` → `MOVE` → `REPORT` | `MOVE (ignored)` — would fall off south edge |
| 8 | `PLACE 0,0,WEST` → `MOVE` → `REPORT` | `MOVE (ignored)` — would fall off west edge |
| 9 | `PLACE 4,4,NORTH` → `MOVE` → `REPORT` | `MOVE (ignored)` — would fall off north edge |
| 10 | `PLACE 4,4,EAST` → `MOVE` → `REPORT` | `MOVE (ignored)` — would fall off east edge |

### LEFT / RIGHT

| # | Commands | Expected output |
|---|---|---|
| 11 | `PLACE 0,0,NORTH` → `LEFT` → `REPORT` | `0,0,WEST` |
| 12 | `PLACE 0,0,NORTH` → `RIGHT` → `REPORT` | `0,0,EAST` |
| 13 | `PLACE 0,0,NORTH` → `LEFT` → `LEFT` → `LEFT` → `LEFT` → `REPORT` | `0,0,NORTH` — 4× left returns to original |
| 14 | `PLACE 0,0,NORTH` → `RIGHT` → `RIGHT` → `RIGHT` → `RIGHT` → `REPORT` | `0,0,NORTH` — 4× right returns to original |

### Commands before PLACE are ignored

| # | Commands | Expected output |
|---|---|---|
| 15 | `MOVE` | `MOVE (ignored)` |
| 16 | `LEFT` | `LEFT (ignored)` |
| 17 | `RIGHT` | `RIGHT (ignored)` |
| 18 | `REPORT` | `REPORT → (no robot placed)` |

### Combined sequences

| #  | Commands | Expected output |
|----|---|---|
| 19 | `PLACE 1,2,EAST` → `MOVE` → `MOVE` → `LEFT` → `MOVE` → `REPORT` | `3,3,NORTH` |
| 20 | `PLACE 0,0,NORTH` → `MOVE` × 4 → `MOVE` → `REPORT` | 5th MOVE ignored, robot stays at `0,4,NORTH` |
| 21 | `PLACE 2,2,NORTH` → `LEFT` → `MOVE` → `MOVE` → `RIGHT` → `MOVE` → `REPORT` | `0,3,NORTH` |

## 🎥 Demo Video

[![Watch the demo](https://img.youtube.com/vi/34KXePwIbGw/0.jpg)](https://www.youtube.com/watch?v=34KXePwIbGw)
