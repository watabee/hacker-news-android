# https://firebase.google.com/docs/crashlytics/get-deobfuscated-reports

-keepattributes *Annotation*  # Keep Crashlytics annotations
-keepattributes SourceFile,LineNumberTable  # Keep file names/line numbers
-keep public class * extends java.lang.Exception  # Keep custom exceptions

# For faster builds with ProGuard
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**