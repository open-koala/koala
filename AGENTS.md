# Repository Guidelines

## Project Structure & Module Organization

This is a Maven multi-module Java project rooted at `pom.xml`. The root reactor includes `koala-commons`, `koala-security`, `koala-organisation`, `koala-security-org`, `koala-bpm`, `koala-monitor`, `koala-gqc`, `koala-opencis`, `koala-businesslog`, and `koala-plugin`. Note that the monitor directory is `koala-monitor`, while its Maven artifact is `koala-jmonitor`.

Modules use the standard Maven layout: Java in `src/main/java`, resources in `src/main/resources`, tests in `src/test/java`, and web assets in `src/main/webapp`. Design docs live in each subsystem as `DESIGN.md`; the root operations guide is `OPERATIONS.md`.

## Build, Test, and Development Commands

- `mvn -DskipTests compile`: compile the full reactor.
- `mvn -pl koala-security/koala-security-core test`: run tests for one module and its configured test scope.
- `mvn -pl koala-gqc/koala-gqc-web -am jetty:run`: start the GQC web app on port `7652`.
- `mvn -pl koala-monitor/koala-jmonitor-web-mvc -am jetty:run`: start monitor on port `7653`.
- `mvn -pl koala-businesslog/koala-businesslog-web -am jetty:run`: start the business log web module.

Use module paths with `-pl`; artifact-only names often fail in this reactor.

## Coding Style & Naming Conventions

Keep source compatible with the existing Java 6-era style even when compiling on JDK 17. Use four-space indentation, K&R braces, `camelCase` methods and fields, `PascalCase` classes, and packages under `org.openkoala`. Preserve local layering suffixes such as `*Controller`, `*Application`, `*Facade`, `*DTO`, `*Command`, `*Service`, and `*Impl`.

There is no central formatter. Follow nearby code and avoid broad XML/JSP reformatting.

## Testing Guidelines

Tests use JUnit 4, with Mockito and PowerMock in several modules. Place tests under the module they cover. Name unit tests `*Test` and integration tests `*IntegrationTest`. For persistence or Spring context changes, add test resources under `src/test/resources` and run the affected module plus dependents.

## Commit & Pull Request Guidelines

History uses short English or Chinese subjects, such as `add README.md.` and `升级dddlib依赖。`. Keep commits focused by subsystem.

Pull requests should list affected modules, verification commands, database/configuration impact, and screenshots for JSP or static UI changes.

## Security & Configuration Tips

Do not commit local credentials, private Maven settings, or real external-tool tokens. Review `META-INF/props/*.properties`, `.mvn/*`, and test database files carefully because many modules load Spring and persistence configuration from those paths.
