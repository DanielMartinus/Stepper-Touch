# ⚠️ This repository is no longer maintained 


# Stepper-Touch


[![Twitter](https://img.shields.io/badge/Twitter-@dionsegijn-blue.svg?style=flat)](http://twitter.com/dionsegijn) ![API](https://img.shields.io/badge/API-16%2B-blue.svg?style=flat) [![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin) [![CircleCI](https://circleci.com/gh/DanielMartinus/Stepper-Touch/tree/master.svg?style=svg)](https://circleci.com/gh/DanielMartinus/Stepper-Touch/tree/master)

**For more updates** on this and other open-source projects, follow me on twitter 👉 [here](https://twitter.com/DionSegijn)

---

Stepper Touch for Android based on a Material Up showcase designed by [Oleg Frolov](https://material.uplabs.com/posts/stepper-touch-interface)

In the latest version of the support library (25.3.0) a new class SpringAnimation was made available. I wanted to test this out and not long after that I found Stepper Touch, a concept made in FramerJS, on Material Up. I took this oppertunity to play with SpringAnimations.

[<img src="media/demo.gif" width="300" />]()

Try it yourself:

[<img src="media/google-play-badge.png" width="250" />](https://play.google.com/store/apps/details?id=nl.dionsegijn.steppertouchdemo)

## Gradle

* Step 1. Add the JitPack repository to your build file

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

* Step 2. Add the dependency (only for androidx projects)

```gradle
dependencies {
    implementation 'com.github.DanielMartinus:Stepper-Touch:1.0.1'
}
```

If you haven't migrated your project to **AndroidX** use:
```gradle
dependencies {
    implementation 'com.github.DanielMartinus:Stepper-Touch:0.6'
}
```


More info about it here: [#24](https://github.com/DanielMartinus/Stepper-Touch/issues/24)

## Implement

```XML
<nl.dionsegijn.steppertouch.StepperTouch
        android:id="@+id/stepperTouch"
        android:layout_width="100dp"
        android:layout_height="40dp" />
```

### Kotlin

```Kotlin
val stepperTouch = findViewById<StepperTouch>(R.id.stepperTouch)
stepperTouch.minValue = 0
stepperTouch.minValue = 10
stepperTouch.sideTapEnabled = true
stepperTouch.addStepCallback(object : OnStepCallback {
	override fun onStep(value: Int, positive: Boolean) {
    		Toast.makeText(applicationContext, value.toString(), Toast.LENGTH_SHORT).show()
	}
})
```

### Java

```Java
StepperTouch stepperTouch = findViewById(R.id.stepperTouch);
stepperTouch.setMinValue(0);
stepperTouch.setMaxValue(3);
stepperTouch.setSideTapEnabled(true);
stepperTouch.addStepCallback(new OnStepCallback() {
    @Override
    public void onStep(int value, boolean positive) {
        Toast.makeText(getApplicationContext(), value + "", Toast.LENGTH_SHORT).show();
    }
});
```

You're able to further customize or set initial values with styled attributes:

1) Add res-auto to your xml layout if you haven't yet

```XML
xmlns:app="http://schemas.android.com/apk/res-auto"
```

2) After that the following attributes will become available:

```XML
app:stepperBackgroundColor=""
app:stepperButtonColor=""
app:stepperActionsColor=""
app:stepperActionsDisabledColor=""
app:stepperTextColor=""
app:stepperTextSize=""
app:app:stepperAllowNegative=""
app:app:stepperAllowPositive=""
```
