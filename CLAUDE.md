# CLAUDE.md

Java remake of *Master of Olympus: Zeus* (Swing, isometric). Hobby project, early stage. Comments and commit messages are often in French; in-game names in `ObjectType` are French.

## Build and run

- Gradle 7.4 via the wrapper (`./gradlew build`). Sources sit directly in `src/` (no `src/main/java`).
- Requires **JDK 17** (pattern-matching `instanceof` is used). IntelliJ is configured with corretto-17; the `java` on PATH may be 1.8, so set `JAVA_HOME` to a JDK 17 before running Gradle from a shell.
- Dependencies: `SGReaderJava` (JitPack, the author's own `.sg3`/`.555` sprite reader), JOAL + gluegen (OpenAL sound), j-ogg.
- Entry point: `com.exiro.Main`. No args = game; first arg containing `map` = random map creator (`terrainGenerator.MapCreatorFrame`).
- The game needs the original game's assets in `Assets/DATA` (`.sg3`/`.555`) and `Assets/Audio`. Both are gitignored and never committed. The starting map is `Assets/savedMap.map` (seed + generator settings, read by `MapSettings`).
- `.gitignore` also excludes `build.gradle`, `settings.gradle` and `gradle/`, so build file changes are not tracked unless that is changed.
- There are no tests.

## Architecture (`src/com/exiro`)

- `systemCore`: `GameLoop` runs on its own thread but executes each frame on the Swing EDT (`invokeAndWait`): `Simulation` catches up in fixed 1/60 s ticks (max 5 per frame), then panels paint. All city state and input handling live on the EDT; do not add extra threads or `synchronized` blocks around city state. Also `TimeManager`, `PathManager`, `BuildingManager`, `GameManager`.
- `object`: `City`, `CityMap`, `Case` (one map tile), `Player`, `Resource`, `ResourceManager`, and `ObjectType`, the registry of every placeable thing (name, sprite file, bitmap/image ids, size, class). Adding a building means a class plus an `ObjectType` entry.
- `buildingList`: abstract `Building` (`process`, `processSprite`, `populate`, `addPopulation`), with subpackages per category (agriculture, industry, stockage, delivery/agora, culture, security, palace, army). `StoreBuilding` handles storage orders (accept/reject/empty; "obtain" is not implemented).
- `sprite`, `ai`: walkers, carters, harvesters, animals, boats, immigrants; `MovingSprite` registers itself on the tile `(ceil x, ceil y)`.
- `moveRelated`: road pathfinding (`PathAlgo`, `RoadMap`, `FreeState` masks).
- `constructionList`, `terrainList`: roads, rubble, trees/vines/olives; terrain tiles. `terrainGenerator`: Perlin-noise map generation.
- `render`: isometric rendering. `WorldRenderer` draws a ground pass first, then multi-tile objects cut into half-tile vertical strips ordered by their front-most footprint tile, then sprites. Keep this ordering when adding renderables. UI in `render/layout` and `render/interfaceList`.
- `fileManager`: `ImageLoader` (cache key `file|bitmap|image`), sound/font/map loaders. `soundManager`: OpenAL playback.

## Status

Branch `refactor/game-loop-and-render-order` holds the single-threaded loop and render-order rewrite (not yet merged into `master`). Missing: save/load of a city in progress, military, gods/religion, trade, overall economy. Several walker/service classes (culture, security, water, immigrant, tax, guild) are thin stubs.
