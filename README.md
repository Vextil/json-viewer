# android-json-view
Andorid JSON Viewer

With this library you can display JSON in an android view, you can click on an item to hide and show the sub tree.

[![](https://jitpack.io/v/vextil/json-viewer.svg)](https://jitpack.io/#vextil/json-viewer)

Forked from: [Android JSON Viewer](https://github.com/pvarry/android-json-viewer)

## Usage

**Gradle**

- **Project level `build.gradle`**
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
- **App level `build.gradle`**
```gradle
dependencies {
    implementation 'com.github.vextil:json-viewer:v1.0'
}
```

**Maven**

```xml
<!-- <repositories> section of pom.xml -->
<repository>
    <id>jitpack.io</id>
   <url>https://jitpack.io</url>
</repository>


<!-- <dependencies> section of pom.xml -->
<dependency>
    <groupId>com.github.vextil</groupId>
    <artifactId>json-viewer</artifactId>
    <version>v1.0</version>
</dependency>
```

## Documentation :book:

```XML
<sh.vex.jsonviewer.JsonViewer
    android:id="@+id/jsonViewer"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

`void setJson(Object json)`

> Set the JSON, must be `org.json.JSONObject` or `org.json.JSONArray`

`void collapseJson()`

> It will collapse all nodes, except the main one.

`void expandJson()`

> It will expands all the json nodes.

`void setTextColorString(@ColorInt int color)`

`void setTextColorNumber(@ColorInt int color)`

`void setTextColorBool(@ColorInt int color)`

`void setTextColorNull(@ColorInt int color)`

## Examples :+1:

**Default view**

![alt tag](https://raw.github.com/vextil/json-viewer/master/screenshots/screenshot.jpg)
