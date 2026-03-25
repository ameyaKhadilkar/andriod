# SugarSaathi 🌟

**Your loving companion for diabetic wellness.**

SugarSaathi is an Android app designed to help diabetic patients — and their caregivers — build healthy habits through daily check-ins, meal logging, blood sugar tracking, and gentle encouragement.

---

## Features

- **Daily Check-in** — Log whether you followed a good diet today. Reflect when it was difficult, celebrate when it was great.
- **Meal Log** — Track breakfast, lunch, snacks, and dinner with quick-select options and custom food entries.
- **Blood Sugar & HbA1c** — Record fasting, post-meal, and random blood sugar readings, plus HbA1c results over time.
- **Diet Calendar** — Visual monthly calendar showing good days and difficult days at a glance.
- **Weekly Report** — Summary of your week with motivational messages. Share progress directly on WhatsApp.
- **Doctor Visits** — Log doctor appointments, prescriptions, and upcoming visit dates.
- **Festival Mode** — Contextual diet tips during festivals.
- **Daily Reminders** — Customizable notification time for your check-in reminder.
- **Data Backup** — Export your data to JSON and import it anytime to restore history.
- **Multi-language Support** — Available in English and Hindi (हिन्दी).

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 35 (Android 15) |
| Architecture | MVVM (ViewModel + LiveData) |
| Database | Room 2.6 |
| Background Work | WorkManager |
| UI | Material Design 3, ViewBinding |
| Serialization | Gson |
| Build | Gradle with Version Catalog |

---

## Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11+

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/ameyaKhadilkar/andriod.git
   cd andriod
   ```

2. Open the project in Android Studio.

3. Sync Gradle and run on a device or emulator (API 24+).

> `local.properties` is not included in version control. Android Studio will generate it automatically with your local SDK path.

---

## Project Structure

```
app/src/main/java/com/example/sugarsaathi/
├── data/
│   ├── db/             # Room database, DAOs, entities
│   ├── repository/     # Data repository layer
│   └── DataExportManager.kt
├── notification/       # Notification helper & broadcast receiver
├── ui/
│   ├── bloodsugar/     # Blood sugar & HbA1c screens
│   ├── calendar/       # Diet calendar
│   ├── checkin/        # Daily check-in & reflection
│   ├── doctor/         # Doctor visit log
│   ├── home/           # Home screen
│   ├── meal/           # Meal log
│   ├── onboarding/     # First-time setup
│   ├── report/         # Weekly report
│   ├── settings/       # App settings
│   └── SplashActivity.kt
├── util/               # Date utils, locale helper, prefs, quotes
└── worker/             # Missed check-in background worker
```

---

## License

This project is open source. Feel free to use and adapt it for personal or educational purposes.