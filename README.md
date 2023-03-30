# DigitalEnvoyTestApplication

Sr - Android Application Coding Test
Requirements
## Task #1 - Triggering action when user taps a single word
Your goal is to implement the logic necessary to toast a message when the user touches the word "Hello" in "Hello World!"
You are only permitted to add 'android:id="@+id/id_text_view"' to the existing TextView
No other additions/modifications to the activity_main.xml layout are allowed
## Task #2 - Actions: Scheduling work
Implement logic required to schedule work (Task #3) in the background approximately every hour, even when the app is closed and the
phone is restarted
Your implementation does not have to execute background work at exactly 1-hour intervals
Your debug build variant should change the interval from 1-hour to 20-minutes
Explain in 1-2 lines why you selected your method of doing background work
## Task #3 - Recurring work implementation
The recurring work done in the background will be to:
Collect 3 location updates using a coroutine flow
Toast all 3 location objects at once
Explain in 1-2 lines why you selected your method of collecting location data
## Task #4 - Local storage options
Add a TODO comment after toasting the last known location that describes at least 2 options you’d have for persisting the locations to
storage and when it is best to use each one
## Final Instructions:
Please provide a link to an APK of the DEBUG variant of your application
Provide the GitHub publicly accessible url
Thanks! We appreciate your time!
