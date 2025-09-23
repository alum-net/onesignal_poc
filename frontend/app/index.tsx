import React from "react";
import { Text, View, TextInput, Button } from "react-native";
import {
  useUserState,
  createOrUpdateUser,
  handleScheduledNotification,
  handleSendInstantNotification,
} from "../shared/hooks";
import { styles as sharedStyles } from "../shared/styles";
import { OneSignal } from "react-native-onesignal";

export default function Index() {
  const {
    email,
    setEmail,
    name,
    setName,
    lastName,
    setLastName,
    selectedDate,
  } = useUserState();

  const loginUser = async (userEmail: string) => {
    try {
      OneSignal.login(userEmail);
    } catch (error) {
      console.error("OneSignal login error (native):", error);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>OneSignal PoC (Native)</Text>

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
        <Button
          title="Create/Update User"
          onPress={() => createOrUpdateUser(email, name, lastName)}
        />
      </View>

      <View style={styles.formSection}>
        <Text style={styles.sectionTitle}>Send Notifications</Text>
        <Button
          title="Send Instant Notification"
          onPress={() => handleSendInstantNotification(loginUser, email)}
        />
        <View style={styles.scheduleSection}>
          <Text>{selectedDate.toLocaleString()}</Text>
          <Button
            title="Send Scheduled Notification"
            onPress={() =>
              handleScheduledNotification(loginUser, email, selectedDate)
            }
          />
        </View>
      </View>
    </View>
  );
}

const styles = sharedStyles; // Ensure no conflicts with local styles
