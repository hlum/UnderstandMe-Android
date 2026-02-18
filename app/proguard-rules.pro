# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Preserve line number information for debugging stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep generic signature for reflection
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod

# ============================================================================
# Retrofit
# ============================================================================
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

# Keep API service interfaces
-keep,allowobfuscation interface jp.ac.jec.cm0138.understandme.Retrofit.Services.** { *; }

# ============================================================================
# Kotlinx Serialization
# ============================================================================
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class jp.ac.jec.cm0138.understandme.**$$serializer { *; }
-keepclassmembers class jp.ac.jec.cm0138.understandme.** {
    *** Companion;
}
-keepclasseswithmembers class jp.ac.jec.cm0138.understandme.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep data classes used for serialization
-keep @kotlinx.serialization.Serializable class ** { *; }

# ============================================================================
# OkHttp
# ============================================================================
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Keep interceptors
-keep class jp.ac.jec.cm0138.understandme.Retrofit.APIKeyInterceptor { *; }
-keep class jp.ac.jec.cm0138.understandme.Retrofit.BaseUrlInterceptor { *; }

# ============================================================================
# Firebase
# ============================================================================
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# Firebase Auth
-keepclassmembers class com.google.firebase.auth.** { *; }

# Firebase Messaging
-keep class com.google.firebase.messaging.** { *; }
-keep class jp.ac.jec.cm0138.understandme.Service.UnderstandMeFirebaseMessagingService { *; }

# Firebase Remote Config
-keep class com.google.firebase.remoteconfig.** { *; }
-keep class jp.ac.jec.cm0138.understandme.Helper.RemoteConfigManager { *; }

# ============================================================================
# Hilt/Dagger
# ============================================================================
-dontwarn com.google.errorprone.annotations.**
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }

-keepclasseswithmembernames class * {
    @dagger.* <fields>;
}

-keepclasseswithmembernames class * {
    @dagger.* <methods>;
}

-keepclasseswithmembernames class * {
    @javax.inject.* <fields>;
}

-keepclasseswithmembernames class * {
    @javax.inject.* <methods>;
}

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent
-keep class **_HiltModules { *; }
-keep class **_HiltModules$** { *; }
-keep class **_Factory { *; }
-keep class **_MembersInjector { *; }
-keep class **_Impl { *; }

# Keep application and activity classes that use Hilt
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }

# Keep modules and provides methods
-keep @dagger.Module class * { *; }
-keep class jp.ac.jec.cm0138.understandme.di.** { *; }

# ============================================================================
# Compose
# ============================================================================
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }

# Keep ViewModel classes
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keep class * extends androidx.lifecycle.AndroidViewModel { *; }

# ============================================================================
# Gson (used by Retrofit converter)
# ============================================================================
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Keep data model classes
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# ============================================================================
# Keep project-specific classes
# ============================================================================
# Keep all repositories and their implementations
-keep class jp.ac.jec.cm0138.understandme.Repository.** { *; }
-keep interface jp.ac.jec.cm0138.understandme.Repository.** { *; }

# Keep ViewModels
-keep class jp.ac.jec.cm0138.understandme.Presentation.ViewModels.** { *; }

# Keep data models
-keep class jp.ac.jec.cm0138.understandme.Model.** { *; }

# Keep MainActivity and Application
-keep class jp.ac.jec.cm0138.understandme.MainActivity { *; }
-keep class jp.ac.jec.cm0138.understandme.MainApplication { *; }

# ============================================================================
# Coroutines
# ============================================================================
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.** {
    volatile <fields>;
}

# ============================================================================
# Coil
# ============================================================================
-keep class coil3.** { *; }
-keep interface coil3.** { *; }

# ============================================================================
# Vico (Charts)
# ============================================================================
-keep class com.patrykandpatrick.vico.** { *; }

# ============================================================================
# Lottie
# ============================================================================
-keep class com.airbnb.lottie.** { *; }