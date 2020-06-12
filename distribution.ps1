npm i -g firebase-tools\
write-host "starting deploy...";
firebase --version;
firebase appdistribution:distribute d:\a\r1\a\_4youandme_Android\drop\app\build\outputs\apk\debug\app-debug.apk --app 1:1077181274205:android:6620faa1b5eb57f165d5b4 --release-notes "deployment test" --testers iacopocheccacci@gmail.com --token 1//09a9InvmuhnP9CgYIARAAGAkSNwF-L9IrCGsZqJvkp6db3D-SpvIpmg0PX_5zIyHLVOH3kEGcOMV1zmw-wCdDevxDYnVil06LYxo --project youandme-7e492;
write-host "deployment completed";