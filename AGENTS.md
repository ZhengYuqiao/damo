# damo

A simple Spring Boot 1.5.16 demo web application. The Maven project lives in `demoBoot/`.

## Cursor Cloud specific instructions

### Service overview
- Single service: a Spring Boot 1.5.16 web app (module `demoBoot`). Endpoints include `/home`, `/home/get?name=...`, `/api/hello` (POST), `/web` (FreeMarker page), `/file/upload`, and a `/hello` servlet.

### Java version (important)
- This project targets Java 8 and uses Spring Boot 1.5, which does NOT run on the default Java 21 in this image. A Temurin JDK 8 is installed at `/usr/lib/jvm/temurin-8`.
- `JAVA_HOME` is exported to that JDK in `~/.bashrc`, so new interactive shells use Java 8 automatically. Non-interactive commands (and the startup update script) must set `JAVA_HOME=/usr/lib/jvm/temurin-8` explicitly.

### Build / test / run (all run from `demoBoot/`)
- Use the Maven wrapper `./mvnw` (it must be invoked from `demoBoot/`; running it from the repo root via `-f` fails to locate the wrapper jar).
- Build: `./mvnw -B clean package` (add `-DskipTests` to skip tests).
- Test: `./mvnw -B test`.
- Run (dev): `./mvnw -B spring-boot:run`.

### Active profile & ports (gotcha)
- The active Spring profile is `test` (set in `application.properties`), so the app listens on port **8082**, not 8080. Other profiles: `dev` -> 8081, the base config -> 8080, `prod` -> 8083.
- Under the `test` profile, logback (`logback-zyq.xml`) writes ONLY to a rolling file (no console output), so `spring-boot:run` produces little stdout after the banner. Check the log file for startup confirmation.

### Hardcoded macOS paths (gotcha)
- `logback-zyq.xml` logs to `/Users/macos/logger` and the upload path (`dharma.upload.path`) is `/Users/macos/`. These macOS paths are hardcoded. The directory `/Users/macos/logger` must exist (and be writable) or context loading / tests fail with a logback "Failed to create parent directories" error. It is created during environment setup; recreate with `mkdir -p /Users/macos/logger` if missing.
