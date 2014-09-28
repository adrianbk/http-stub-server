# Performing a Release

Follow these simple steps to cut a release of the HTTP stub server.

### 1. Change the version from snapshot to release

`build.gradle`

```groovy
allprojects {
  group = 'com.github.adrianbk'
  version = '1.0-SNAPSHOT'
}
```

### 2. Publish

```bash
./gradlew publish -Pbintray_username=<username> -Pbintray_password=<apiKey> -i
```

