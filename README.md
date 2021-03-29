<strong>Project Name:</strong>
<br>
Quiet Zones

<strong>Summary:</strong>
<br>
This project was created for my Android app development class. Quiet Zones is an Android application that allows users to create geographical areas that will alter the phone's ringer volume and vibration upon the user entering and leaving these areas.

<strong>How To Use It:</strong>
Users start by allowing permissions and selecting the "Add" button.
<br>
<p align="center">
  <img src="https://user-images.githubusercontent.com/43380978/112775151-342e2480-8ff9-11eb-9383-aee7762158a3.png" alt="Quiet Zones Menu" width="240" height="400">
</p>

Next they create a circular area on a map that will alter the ringer of the phone when entered. They give the circle a radius and assign it a name, and just like that, the app will start working automatically in the background.
<br>
<p align="center">
  <img src="https://user-images.githubusercontent.com/43380978/112775152-34c6bb00-8ff9-11eb-8e89-742894fc414f.png" alt="Quiet Zones Add" width="240" height="400">
</p>

If a user wants to remove one of these zones, it is as simple as clicking the "Remove" button and then clicking on the name of the zone that the user wants removed. 

If the user wants to alter what happens upon entering one of these zones, they can change the ringer volume and vibration by clicking the "Settings" button.
<br>
<p align="center">
  <img src="https://user-images.githubusercontent.com/43380978/112775153-34c6bb00-8ff9-11eb-9586-7bd2ccf05d72.png" alt="Quiet Zones Settings" width="240" height="400">
</p>

<strong>How It Works:</strong>
<br>
The app uses the Google Maps API to allow users to specify locations to place the geofences. After the locations are selected, the app uses an Android feature called geofences. These geofences call specified functions when the user enters or leaves a geofence. Inside these functions, the ringer and vibration settings are altered. The application gets the user's location every sixty seconds, and the geofences determine whether or not the user has entered or left an area. 

<strong>Contributors:</strong>
<br>
Zachary Klingler
<br>
Britton Baird
