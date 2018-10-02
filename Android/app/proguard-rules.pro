# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/swati/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*

-keep class android.support.v7.widget.** { *; }

# Partially ensure compatibility over time for serializable classes
# http://proguard.sourceforge.net/manual/examples.html#serializable
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# removes log for info, debug and versbose tags
-assumenosideeffects class android.util.Log {
	public static *** i(...);
    public static *** d(...);
    public static *** v(...);
}

# keep the class and specified members from being removed or renamed
-keep class objects.** { *; }

# Gson specific classes
-keep class sun.misc.Unsafe { *; }