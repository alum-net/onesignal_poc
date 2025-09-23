import { Stack } from "expo-router";
import { useEffect } from "react";
import { Platform } from "react-native";

export default function RootLayout() {
  useEffect(() => {
    if (Platform.OS === "web") {
      if (typeof window !== "undefined") {
        import("react-onesignal").then(({ default: OneSignal }) => {
          OneSignal.init({
            appId: process.env.EXPO_PUBLIC_ONE_SIGNAL_ID as string,
            allowLocalhostAsSecureOrigin: true,
            autoRegister: true,
            autoResubscribe: true,
            autoPrompt: true,
          });
        });
      }
    } else {
      import("react-native-onesignal").then(({ OneSignal, LogLevel }) => {
        OneSignal.Debug.setLogLevel(LogLevel.Verbose);
        OneSignal.initialize(process.env.EXPO_PUBLIC_ONE_SIGNAL_ID as string);
        OneSignal.Notifications.requestPermission(true);
        OneSignal.Notifications.addEventListener(
          "foregroundWillDisplay",
          (event) => {
            console.log("Notification received in foreground:", event);
            // Show notification
            event.getNotification().display();
          }
        );

        // When user taps a notification
        OneSignal.Notifications.addEventListener("click", (event) => {
          console.log("Notification clicked:", event);
        });
      });
    }
  }, []);
  return <Stack />;
}
