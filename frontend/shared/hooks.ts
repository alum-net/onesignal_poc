import { useState } from "react";
import { Alert } from "react-native";
import axios from "axios";

const api = axios.create({
  baseURL: process.env.EXPO_PUBLIC_API_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

export const useUserState = () => {
  const [email, setEmail] = useState("");
  const [name, setName] = useState("");
  const [lastName, setLastName] = useState("");
  const [selectedDate] = useState(new Date(Date.now() + 5 * 60 * 1000));

  return {
    email,
    setEmail,
    name,
    setName,
    lastName,
    setLastName,
    selectedDate,
  };
};
// Check if user exists
export const checkUserExists = async (userEmail: string) => {
  try {
    const response = await api.get(`/users/exists/${userEmail}`);
    return response.data.exists;
  } catch (error) {
    console.error("Error checking user existence:", error);
    return false;
  }
};

// Create or update user
export const createOrUpdateUser = async (
  email: string,
  name: string,
  lastName: string
) => {
  try {
    const response = await api.post("/users", { email, name, lastName });

    if (response.status === 201) {
      Alert.alert("Success", "User created successfully!");
    } else if (response.status === 409) {
      Alert.alert("Info", "User already exists.");
    } else {
      Alert.alert("Error", "Failed to create user.");
    }
  } catch (error: any) {
    console.error(error);
    Alert.alert(
      "Error",
      error.response?.data?.message || "Backend server error."
    );
  }
};

// Send instant notification
export const handleSendInstantNotification = async (
  loginCallback: (email: string) => Promise<void>,
  email: string
) => {
  if (!(await checkUserExists(email))) {
    Alert.alert("Error", "User not found.");
    return;
  }
  await loginCallback(email);

  try {
    await api.post(`/users/${email}/send-notification`, {
      heading: "Instant Notification",
      content: "Hello from Native!",
    });

    console.log("Notification sent!");
  } catch (error) {
    console.error("Failed to send notification:", error);
  }
};

// Schedule notification
export const handleScheduledNotification = async (
  loginCallback: (email: string) => Promise<void>,
  email: string,
  selectedDate: Date
) => {
  if (!(await checkUserExists(email))) {
    Alert.alert("Error", "User not found.");
    return;
  }
  await loginCallback(email);

  const formattedDate = selectedDate.toISOString();

  try {
    await api.post(`/users/${email}/schedule-notification`, {
      heading: "Scheduled Notification",
      content: "This is a scheduled push notification!",
      scheduledAt: formattedDate,
    });

    console.log("Scheduled notification sent!");
  } catch (error) {
    console.error("Failed to schedule notification:", error);
  }
};
