# Setup Instructions

+ [Checkout App from Play Store](#checkout-app-from-play-store)
+ [Remote Configuration](#remote-configuration)
+ [Raising a Pull Request (Feature Development / Fixing Issue)](#raising-a-pull-request)
+ [Dependencies](#dependencies)

## Checkout App from Play Store
Before getting into the app, you should first familarise yourself with the app. Get the app from Google Play Store.

*Don't forget to rate the app 5 stars there ;)*

[<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png"
     alt="Get it on Google Play"
     height="80">](https://play.google.com/store/apps/details?id=io.github.project_travel_mate)

## Remote Configuration
+ Fork the project to your account and clone it to your local system.
```
git clone https://github.com/YOUR_USERNAME/Travel-Mate.git & cd Travel-Mate
```
+ Add upstream remote to your git
```
git remote add upstream https://github.com/project-travel-mate/Travel-Mate.git
```
At this point your the output to `git remote -v` should be something like - 
```
   origin  https://github.com/YOUR_USERNAME/Travel-Mate.git (fetch)
   origin  https://github.com/YOUR_USERNAME/Travel-Mate.git (push) 
   upstream        https://github.com/project-travel-mate/Travel-Mate.git (fetch)
   upstream        https://github.com/project-travel-mate/Travel-Mate.git (push)
```
+ To now update your local copy
```
git fetch upstream
git merge upstream/master
git push
```

## Raising a Pull Request
+ Update the master branch to latest version.
```
git checkout master
git pull upstream master
```
+ Start a new branch with a suitable name
```
git checkout -b awesome-branch-name
```
+ Import the `Android` directory in [Android Studio](http://developer.android.com/sdk/). Build & run the project in emulator or any real android device.

+ **Develop a new feature or solve an existing issue. Read [CONTRIBUTING](CONTRIBUTING.md) guidelines carefully**

+ Run checkstyle and fix any issues
```
./gradlew checkstyle
```
Checkout the file at `Android/app/build/reports/checkstyle/checkstyle.html` to get a detailed report of where the checkstyle is failing.

+ Check for build failures
```
./gradlew assembleDebug assembleRelease
```
+ Stage changed files
```
git add <file names>
```
+ Make a commit with a suitable message
```
git commit -m "Awesome changes"
```
Verify your commit by typing `git log`
+ Push the branch to your forked repository
```
git push origin awesome-branch-name
```
+ Go to the Github Repository and create a pull request according to the [PULL_REQUEST_TEMPLATE](https://github.com/project-travel-mate/Travel-Mate/blob/master/.github/pull_request_template.md).


## Dependencies
+ [okhttp](https://github.com/square/okhttp)
+ [android-pathview](https://github.com/geftimov/android-pathview)
+ [picasso](https://github.com/square/picasso)
+ [RippleEffect](https://github.com/patrickpissurno/RippleEffect)
+ [ViewPagerTransforms](https://github.com/ToxicBakery/ViewPagerTransforms)
+ [datetimepicker](https://github.com/flavienlaurent/datetimepicker)
+ [MaterialSearchView](https://github.com/MiguelCatalan/MaterialSearchView)
+ [android-process-button](https://github.com/dmytrodanylyk/android-process-button)
+ [butterknife](https://github.com/JakeWharton/butterknife)
+ [Zxing](https://github.com/zxing/zxing)
+ [What's new](https://github.com/TonnyL/WhatsNew)
+ [Notification Badge](https://github.com/nex3z/NotificationBadge)
+ [Timeline View](https://github.com/vipulasri/Timeline-View)
+ [Spotlight](https://github.com/TakuSemba/Spotlight)
