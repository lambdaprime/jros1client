Application which demonstrates **jros1client** usage under Android.

Additionally it is used for **jros1client** Android integration tests.

# Prereq

- Android 14

# Build

```
gradle assembleDebug installDebug
```

# Import to Eclipse

```
gradle eclipse
```

Make sure to remove JRE from the dependencies because all Java classes will be available from android.jar (from Android SDK).

# Logs

```
adb shell cat /storage/emulated/0/Android/data/id.jros1droid/files/jrosclient-debug.log
```
