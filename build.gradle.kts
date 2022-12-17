buildscript {
    extra.apply {
        set("compose_version", "1.3.2")
    }
}
plugins {
    id("com.android.application").version("7.3.1").apply(false)
    id("com.android.library").version("7.3.1").apply(false)
    id("org.jetbrains.kotlin.android").version("1.7.20").apply(false)
}