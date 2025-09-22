import React, { useState } from "react";
import { StyleSheet, Text, View, TextInput, Button } from "react-native"; // works for web too
import OneSignal from "react-onesignal";

export default function Index() {
  const [email, setEmail] = useState("");
  const [name, setName] = useState("");
  const [lastName, setLastName] = useState("");
  const [selectedDate] = useState(new Date(Date.now() + 5 * 60 * 1000));

  const loginUser = async (userEmail: string) => {
    try {
      await OneSignal.login(userEmail);
      await OneSignal.User.PushSubscription.optIn();
    } catch (error) {
      console.error("OneSignal login error (web):", error);
    }
  };

  const checkUserExists = async (userEmail: string) => {
    try {
      const response = await fetch(
        `${process.env.EXPO_PUBLIC_API_URL}/users/exists/${userEmail}`
      );
      const data = await response.json();
      return data.exists;
    } catch (error) {
      console.error("Error checking user existence:", error);
      alert("Error: Could not check user existence.");
      return false;
    }
  };

  const handleCreateOrUpdateUser = async () => {
    try {
      const response = await fetch(`${process.env.EXPO_PUBLIC_API_URL}/users`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, name, lastName }),
      });

      if (response.status === 201) {
        alert("User created successfully!");
      } else if (response.status === 409) {
        alert("User already exists.");
      } else {
        alert("Failed to create user.");
      }
    } catch (error) {
      console.error(error);
      alert("Backend server error.");
    }
  };

  const handleSendInstantNotification = async () => {
    if (!(await checkUserExists(email))) {
      alert("User not found.");
      return;
    }
    await loginUser(email);

    try {
      const response = await fetch(
        `${process.env.EXPO_PUBLIC_API_URL}/users/${email}/send-notification`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            heading: "Instant Notification",
            content: "Hello from Web!",
          }),
        }
      );

      response.ok
        ? console.log("Notification sent!")
        : console.log("Failed to send notification.");
    } catch (error) {
      console.error(error);
    }
  };

  const handleScheduledNotification = async () => {
    if (!(await checkUserExists(email))) {
      alert("User not found.");
      return;
    }
    await loginUser(email);

    const formattedDate = selectedDate.toISOString();

    try {
      const response = await fetch(
        `${process.env.EXPO_PUBLIC_API_URL}/users/${email}/schedule-notification`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            heading: "Scheduled Notification",
            content: "This is a scheduled push notification!",
            scheduledAt: formattedDate,
          }),
        }
      );

      response.ok
        ? console.log("Scheduled notification sent!")
        : console.log("Failed to schedule notification.");
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>OneSignal PoC (Web)</Text>

      <View style={styles.formSection}>
        <Text style={styles.sectionTitle}>Create/Update User</Text>
        <TextInput
          style={styles.input}
          placeholder="Email"
          value={email}
          onChangeText={setEmail}
        />
        <TextInput
          style={styles.input}
          placeholder="Name"
          value={name}
          onChangeText={setName}
        />
        <TextInput
          style={styles.input}
          placeholder="Last Name"
          value={lastName}
          onChangeText={setLastName}
        />
        <Button title="Create/Update User" onPress={handleCreateOrUpdateUser} />
      </View>

      <View style={styles.formSection}>
        <Text style={styles.sectionTitle}>Send Notifications</Text>
        <Button
          title="Send Instant Notification"
          onPress={handleSendInstantNotification}
        />
        <View style={styles.scheduleSection}>
          <Text>{selectedDate.toLocaleString()}</Text>
          <Button
            title="Send Scheduled Notification"
            onPress={handleScheduledNotification}
          />
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: "center", padding: 20 },
  title: { fontSize: 22, fontWeight: "bold", marginBottom: 20 },
  formSection: { marginBottom: 30 },
  sectionTitle: { fontSize: 18, fontWeight: "bold", marginBottom: 10 },
  input: {
    borderWidth: 1,
    borderColor: "#ccc",
    marginBottom: 10,
    padding: 8,
    borderRadius: 5,
  },
  scheduleSection: { marginTop: 20, alignItems: "center" },
});
