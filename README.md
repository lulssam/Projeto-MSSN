# RoloApp 📷

A native **Android** app for analogue film photographers — guiding them through the entire film development process, from film selection to the final wash.

---

## 📖 About

RoloApp was built as the final project for the **Mobile Application Development** course at ISEL. It solves a real problem for analogue photography enthusiasts: keeping track of film rolls and knowing exactly which chemicals to use, at what dilution, and for how long.

---

## ✨ Features

- 🎞️ **Film library** — browse and search a catalogue of analogue films
- 🧪 **Chemical pairing** — find the right developer and fixer combinations for each film
- 🪜 **Step-by-step development guide** — follow the full development process with timers and instructions
- 🔐 **Authentication** — secure login via Firebase Authentication
- 💾 **Local storage** — film roll history saved locally with Room Database
- 👤 **User profiles** — personalised experience per user

---

## 🛠️ Tech Stack

| Technology | Usage |
|---|---|
| Kotlin | Primary language |
| Jetpack Compose | UI framework |
| Room Database | Local data persistence |
| Firebase Authentication | User authentication |
| MVVM Architecture | App architecture pattern |

---

## 🏗️ Project Structure
```
app/
├── data/          # Room database, Firebase, repositories
├── domain/        # Business logic, models
├── ui/            # Compose screens and components
└── viewmodel/     # ViewModels
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- A Firebase project with Authentication enabled

### Setup

1. Clone the repository
```bash
git clone https://github.com/lulssam/RoloApp-DAM.git
```

2. Add your `google-services.json` to the `app/` folder

3. Build and run
```bash
./gradlew :app:assembleDebug
```

---

## 📸 Screenshots

*Coming soon*

---

## 👩‍💻 Author

**Luísa Sampaio** — [@lulssam](https://github.com/lulssam)

---

## 📚 Academic Context

Final project for **Desenvolvimento de Aplicações Móveis (DAM)**
BSc in Informatics and Multimedia Engineering — ISEL, 2025
