# Metro Hilt interop: duplicate `metro.hints` classes

## Versions

| Tool | Version |
| --- | --- |
| Metro | 1.4.5 |
| Kotlin | 2.4.20 |
| Android Gradle plugin | 9.3.1, with built-in Kotlin |
| Gradle | 9.7.1 |
| `hilt-android` | 2.59.2, as a runtime dependency only. Neither Hilt's Gradle plugin nor its KSP processor is applied. |

The build fails the same way with `hilt-android` 2.60.1.

## Steps to reproduce

Build the app:

```shell
./gradlew :app:assembleDebug
```

## Expected

The build succeeds.

## Actual

`:app:mergeLibDexDebug` fails:

```
* What went wrong:
Execution failed for task ':app:mergeLibDexDebug' (registered by plugin 'com.android.internal.application').
> A failure occurred while executing com.android.build.gradle.internal.tasks.DexMergingTaskDelegate
   > There was a failure while executing work items
      > A failure occurred while executing com.android.build.gradle.internal.tasks.DexMergingWorkAction
         > com.android.builder.dexing.DexArchiveMergerException: Error while merging dex archives:
           Learn how to resolve the issue at https://developer.android.com/studio/build/dependencies#duplicate_classes.
           Type metro.hints.DaggerHiltAndroidFlagsHiltWrapper_FragmentGetContextFix_FragmentGetContextFixModuleJavax_inject_SingletonKt is defined multiple times: lib-a/build/.transforms/<hash>/transformed/bundleLibRuntimeToDirDebug/bundleLibRuntimeToDirDebug_dex/metro/hints/DaggerHiltAndroidFlagsHiltWrapper_FragmentGetContextFix_FragmentGetContextFixModuleJavax_inject_SingletonKt.dex, lib-b/build/.transforms/<hash>/transformed/bundleLibRuntimeToDirDebug/bundleLibRuntimeToDirDebug_dex/metro/hints/DaggerHiltAndroidFlagsHiltWrapper_FragmentGetContextFix_FragmentGetContextFixModuleJavax_inject_SingletonKt.dex
```

After the failed build, list the hint classes that each module generated:

```shell
for m in lib-a lib-b app; do
  echo "== $m"
  find "$m/build/intermediates" -path '*compile*Kotlin/classes/metro/hints/*.class' | sed 's#.*/hints/##' | sort
done
```

All three modules contain the same seven classes, one for each of `hilt-android`'s internal
modules:

```
DaggerHiltAndroidFlagsHiltWrapper_FragmentGetContextFix_FragmentGetContextFixModuleJavax_inject_SingletonKt.class
DaggerHiltAndroidInternalLifecycleHiltWrapper_DefaultViewModelFactories_ActivityModuleDagger_hilt_android_scopes_ActivityScopedKt.class
DaggerHiltAndroidInternalLifecycleHiltWrapper_HiltViewModelFactory_ViewModelModuleDagger_hilt_android_scopes_ViewModelScopedKt.class
DaggerHiltAndroidInternalManagersHiltWrapper_ActivityRetainedComponentManager_LifecycleModuleDagger_hilt_android_scopes_ActivityRetainedScopedKt.class
DaggerHiltAndroidInternalManagersHiltWrapper_ActivitySavedStateHandleModuleDagger_hilt_android_scopes_ActivityRetainedScopedKt.class
DaggerHiltAndroidInternalModulesApplicationContextModuleJavax_inject_SingletonKt.class
DaggerHiltAndroidInternalModulesHiltWrapper_ActivityModuleDagger_hilt_android_scopes_ActivityScopedKt.class
```

In a large project, the same hints are duplicated across every module. In ours, each of these
hints was generated in 388 modules. AutoDagger's `HiltWrapper_AutoDaggerModule` and
`androidx.hilt.work`'s `HiltWrapper_WorkerFactoryModule` are affected the same way.
