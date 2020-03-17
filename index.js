/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

// [START all]
// [START import]
// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();
// [END import]

exports.sendMessage = functions.https.onRequest(async (req, res) => {
  const msg = req.query.msg;
  const sender = req.query.sender;
  const topic = req.query.group;

  var message = {
    notification: {
      title: sender,
      body: msg
    },
    data: {
      sender: sender,
      msg: msg,
      group: topic
    },
    topic: topic
  }

  console.log('request recieved');

  admin.messaging().send(message)
    .then((response) => {
      console.log('Successfully sent message:', response);
      return response;
    }).catch((error) => {
      console.log('Error sending message:', error);
    });

    res.send('request recieved');
  
});