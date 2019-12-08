# android-json-view
Andorid JSON Viewer

With this library you can display JSON in an android view, you can click on an item to hide and show the sub tree.

[![](https://img.shields.io/badge/dynamic/json.svg?label=bintray&query=name&style=for-the-badge&url=https%3A%2F%2Fapi.bintray.com%2Fpackages%2Fvextil%2Fmaven%2FJSON-Viewer%2Fversions%2F_latest)](https://bintray.com/vextil/maven/JSON-Viewer)

Forked from: [Android JSON Viewer](https://github.com/pvarry/android-json-viewer)

## Usage

**Gradle**

```gradle
dependencies {
    implementation 'sh.vex:jsonviewer:1.0.0'
}
```

**Maven**

```xml
<!-- <dependencies> section of pom.xml -->
<dependency>
    <groupId>sh.vex</groupId>
    <artifactId>jsonviewer</artifactId>
    <version>1.0.0</version>
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

## Examples

**Default view**

![alt tag](https://raw.github.com/vextil/json-viewer/master/screenshots/screenshot.jpg)
