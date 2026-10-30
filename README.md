All are Earn - Android app (Java + Firebase + AdMob)

SETUP
1. Open this folder in Android Studio.
2. Firebase console -> create project -> add Android app with package com.surya.allareearn
   -> download google-services.json -> put it in the app/ folder (NOT included in this zip).
   Enable Firebase Auth (Email/Password) and Firestore.
3. Build: Build -> Build Bundle(s)/APK -> Build APK(s). Install app-debug.apk on your phone.

POINTS SYSTEM
1 point = Rs 1. Each rewarded ad = +0.1 point (Rs 0.10). Minimum withdrawal 500 points = Rs 50.
All settings in AppConfig.java (one file).

SURVEYS
Surveys need your own API key: sign up at bitlabs.ai (or pollfish.com), copy the key into
AppConfig.SURVEY_API_KEY. Until then the Surveys screen explains setup.

ADMIN
No password is stored in the app on purpose: APK files can be decompiled and any password inside
would leak. Instead, log in with suryak0411@gmail.com and the Admin Dashboard appears on Home.
To make it stronger, also enable Firestore security rules so only your email can read/write requests.

AD WARNING
Unlimited rewarded ads is kept as requested, but heavy single-user ad watching can be flagged by
AdMob as invalid traffic and get the account banned. A daily cap (10-15) is safer. Change anytime.

SECURITY RULES (Firestore console, paste these)
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /Users/{userId} {
      allow read, write: if request.auth != null;
    }
    match /WithdrawalRequests/{reqId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
      allow update: if request.auth.token.email == 'suryak0411@gmail.com';
    }
  }
}
