import { Stack } from "expo-router";
import { useEffect } from "react";
import { Platform } from "react-native";
import { LogLevel, OneSignal as OneSignalNative } from "react-native-onesignal";
import OneSignal from "react-onesignal";

export default function RootLayout() {
  useEffect(() => {
    if (Platform.OS === "web") {
      if (typeof window !== "undefined") {
        OneSignal.init({
          appId: process.env.EXPO_PUBLIC_ONE_SIGNAL_ID as string,
          allowLocalhostAsSecureOrigin: true,
          autoRegister: true,
          autoResubscribe: true,
          autoPrompt: true,
        });
      }
    } else {
      // Enable verbose logging for debugging (remove in production)
      OneSignalNative.Debug.setLogLevel(LogLevel.Verbose);
      // Initialize with your OneSignalNative App ID
      OneSignalNative.initialize(
        process.env.EXPO_PUBLIC_ONE_SIGNAL_ID as string
      );
      // Use this method to prompt for push notifications.
      // We recommend removing this method after testing and instead use In-App Messages to prompt for notification permission.
      OneSignalNative.Notifications.requestPermission(false);
    }
  }, []); // Ensure this only runs once on app mount
  return <Stack />;
}
