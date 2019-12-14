	'use-strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendNotification = functions.firestore.document("NotifyUsers/{user_id}/Notification/{notification_id}").onWrite((change, context) => {

    const user_id = context.params.user_id;
	const notification_id = context.params.notification_id;
	
	
	return admin.firestore().collection("NotifyUsers").doc(user_id).collection("Notification").doc(notification_id).get().then(queryResult => {
		
		const {from:from_user_id, message:from_message} = queryResult.data();
		
		
		const from_data = admin.firestore().collection("NotifyUsers").doc(from_user_id).get();
		const to_data = admin.firestore().collection("NotifyUsers").doc(user_id).get();
		
		
		return Promise.all([from_data, to_data, from_message , from_user_id]); // added from_message so it's available in the next .then
    })
    .then(([from_data, to_data, from_message, from_user_id]) => { // added from_message
        
		const from_name = from_data.data().name;
		
        const {name:to_name, token_id} = to_data.data();

        console.log("From: " + from_name + " | To : " + to_name +"from:" +from_user_id);

        const payload = {
            "notification" : {
                "title" : "Notification From : " + from_name,
                "body" : from_message,
                "icon" : 'default',
				"click_action": 'return.to.NotificationActivity'
            },
			"data": {
				"from_user_id" : from_user_id, 
				"message" : from_message
			}
        };

        return admin.messaging().sendToDevice(token_id, payload);
    })
    .then(result => {
        return console.log("Notification Sent");
    });
});