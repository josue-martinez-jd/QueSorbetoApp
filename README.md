# First Steps to clone the repo

> You should already downloaded the API for Android 8.1, make sure you already downloaded 

**Preconditions :**
- You should have the JDK and Android Studio already installed 
- You should have already created a test project to make sure the Gradle Build is working 
- You need access to the repo: https://github.com/josue-martinez-jd/QueSorbetoApp

**Steps to clone**
 1. Download and install Git Bash on your PC ( Windows ), Mac have's it integrated . Use this link: https://github.com/git-for-windows/git/releases/download/v2.18.0.windows.1/Git-2.18.0-32-bit.exe
 2. Then this is installed, use the git bash console to navigate to the folder where you want to clone this repo to 
 3. Then on the folder use this command: `git clone https://github.com/josue-martinez-jd/QueSorbetoApp.git`
 4. It will ask for your Git account
 5. Then when the repo is cloned you can open the Folder in Android Studio and if everything went well it should build and open the application normally. 

# Errors that could happen on build

## The API Version is not supported by your phone or the Virtual Device

> Make sure you download the latest API version for Android 8.1 

## The Application is not installing automatically on the Virtual/Physical device 

 1. Open the  **Settings**  or  **Preferences**  dialog. (For Mac,  **Android Studio**  ->  **Preferences**) 
 2. Navigate to  **Build, Execution, Deployment**  >  **Instant Run**. 
 3.  Uncheck the box next to **Enable Instant Run**.
