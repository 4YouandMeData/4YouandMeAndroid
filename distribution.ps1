{\rtf1\ansi\ansicpg1252\cocoartf2513
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx566\tx1133\tx1700\tx2267\tx2834\tx3401\tx3968\tx4535\tx5102\tx5669\tx6236\tx6803\pardirnatural\partightenfactor0

\f0\fs24 \cf0 npm i -g firebase-tools\
write-host "starting deploy...";\
firebase --version;\
firebase appdistribution:distribute d:\\a\\r1\\a\\_4youandme_Android\\drop\\app\\build\\outputs\\apk\\debug\\app-debug.apk --app 1:1077181274205:android:6620faa1b5eb57f165d5b4 --release-notes "deployment test" --testers iacopocheccacci@gmail.com --token 1//09a9InvmuhnP9CgYIARAAGAkSNwF-L9IrCGsZqJvkp6db3D-SpvIpmg0PX_5zIyHLVOH3kEGcOMV1zmw-wCdDevxDYnVil06LYxo --project youandme-7e492\
write-host "deployment completed";}