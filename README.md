AI‑Diary

A personal AI‑powered journaling application that lets users create, store, and retrieve diary entries using text, images, and voice — backed by a modern Java Spring Boot API and a web frontend.

🚀 Overview

AI‑Diary is built to help users capture life moments — whether written, spoken, or visual — and later search by date or keywords. It’s structured to be extendable with AI features like semantic search and conversational recall in future versions.

📌 Key Features
📝 Diary Management

Create text‑based journal entries.

Store and retrieve entries by date or keywords.

📸 Media Support

Attach images to entries.

Search or filter images by date or keywords.

🎙️ Voice Entries

Record and save voice diary entries.

Listen back to recordings.

🔍 Search

Search diary contents using keywords.

Filter entries by date and keyword combinations.


🧠 AI-Powered Diary Extended Sample Use Case

AI‑Diary allows users to record thoughts, feelings, events, and media in multiple formats. Users can later retrieve content using intent-based queries, such as “last happy day,” “full content from a specific date,” or “show images taken near the beach.”

1️⃣ User Entries

Text Entries:

Date	Content
2026-01-03	"Felt excited about starting my new project at work."
2026-01-05	"I felt very happy today because I went to the beach and watched the sunset."
2026-01-07	"Had a stressful day at the office, but yoga helped."

Voice Entries:

Date	File	Description
2026-01-05	voice_2026-01-05.mp3	"Talking about beach trip happiness."
2026-01-07	voice_2026-01-07.mp3	"Reflections after stressful day at office."

Image Uploads:

Date	Filename	Description
2026-01-05	beach_sunset.jpg	"Sunset at the beach"
2026-01-05	sand_castle.jpg	"Built a sand castle"
2026-01-03	office_desk.jpg	"My new workspace"
2️⃣ Intent-Based Queries
User Query	Explanation	Expected Result
"What is the last day I was happy?"	Finds the most recent diary entry marked or interpreted as "happy."	Entry from 2026-01-05, text + voice + images.
"Give me full content from 2026-01-03"	Retrieves everything recorded on a specific date.	Text: "Felt excited about starting my new project at work."
Image: office_desk.jpg
"Show me images I took near the beach"	Filters images based on description containing "beach"	beach_sunset.jpg, sand_castle.jpg
"What did I do last weekend?"	Interprets natural language for a date range	Returns entries from 2026-01-03 to 2026-01-05, including text, voice, and images
"Play my voice note about happiness"	Finds the latest voice entry related to "happy"	voice_2026-01-05.mp3

🛠️ Tech Stack
Backend

Java + Spring Boot — Handles REST APIs and core business logic.

PostgreSQL — Relational data storage.

Clean Architecture + Design Patterns — Clean, maintainable service layers.

Frontend

Likely React or similar (based on folder name reatfrontend).

Handles UI for journal creation, media uploads, and search.
