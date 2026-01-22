# 🌟 AI Diary

An intelligent diary application that combines the power of AI with personal journaling. Write your thoughts, upload images, and let AI help you understand your emotions and memories better.

## ✨ Features

- **📝 Text Entries**: Write diary entries with AI-powered sentiment analysis
- **🖼️ Image Support**: Upload images with descriptions to your diary entries
- **🎤 Voice Notes**: Record voice notes that get transcribed to text
- **🔍 Smart Search**: Search through your entries using natural language
- **🧠 AI Insights**: Get AI-powered analysis of your thoughts and emotions
- **📊 Sentiment Tracking**: Track your emotional patterns over time
- **🔒 User Profiles**: Personal user accounts with secure data storage

## 🏗️ Architecture

This project consists of two main components:

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.4.10 with Java 22
- **Database**: MySQL for structured data, MongoDB for content storage
- **AI Integration**: Groq API for text processing and Ollama for local AI
- **Vector Search**: Qdrant for semantic search capabilities
- **API Documentation**: OpenAPI/Swagger integration

### Frontend (React + Vite)
- **Framework**: React 19.2.0 with Vite
- **Styling**: CSS modules with responsive design
- **Features**: Real-time search, image preview, voice recording
- **Build Tool**: Vite with React Compiler enabled

## 🚀 Getting Started

### Prerequisites

- Java 22
- Node.js 18+
- MySQL 8.0+
- MongoDB 4.4+
- Qdrant vector database

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Ai-Diary-Backend
   ```

2. **Configure databases**
   - Create MySQL database named `diary`
   - Ensure MongoDB is running on localhost:27017
   - Set up Qdrant on localhost:6334

3. **Update configuration**
   Edit `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       username: your_mysql_username
       password: your_mysql_password
     data:
       mongodb:
         database: diaryContent
   
   mlService:
     api:
       key: your_groq_api_key
   ```

4. **Run the backend**
   ```bash
   mvn spring-boot:run
   ```

The backend will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd reatfrontend/reat-frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start development server**
   ```bash
   npm run dev
   ```

The frontend will start on `http://localhost:5173`

## 📚 API Documentation

Once the backend is running, visit `http://localhost:8080/swagger-ui.html` for interactive API documentation.

### Key Endpoints

- `POST /user/create` - Create a new user
- `POST /text/create` - Create a text diary entry
- `POST /image/create` - Upload an image entry
- `POST /voice/create` - Upload a voice note
- `GET /text/find-by-text/{userId}/{query}` - Search text entries
- `GET /image/search-by-text/{userId}/{text}` - Search images by description

## 🛠️ Technology Stack

### Backend Technologies
- **Spring Boot** - Main framework
- **Spring Data JPA** - Database abstraction
- **Spring WebFlux** - Reactive programming
- **Liquibase** - Database migration
- **ModelMapper** - Object mapping
- **Lombok** - Code generation
- **Caffeine** - Caching
- **Qdrant Client** - Vector database integration

### Frontend Technologies
- **React 19** - UI framework
- **Vite** - Build tool and dev server
- **ESLint** - Code linting
- **React Compiler** - Performance optimization

### Databases & Storage
- **MySQL** - User data and metadata
- **MongoDB** - Diary content storage
- **Qdrant** - Vector embeddings for semantic search

### AI & ML Services
- **Groq API** - Text processing and analysis
- **Ollama** - Local AI model inference
- **JEmoji** - Emoji processing

## 🔧 Development

### Backend Development
```bash
# Run tests
mvn test

# Build the project
mvn clean package

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Frontend Development
```bash
# Run development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview

# Lint code
npm run lint
```

## 📁 Project Structure

```
├── Ai-Diary-Backend/          # Spring Boot backend
│   ├── src/main/java/         # Java source code
│   ├── src/main/resources/    # Configuration files
│   └── pom.xml               # Maven dependencies
├── reatfrontend/reat-frontend/ # React frontend
│   ├── src/                  # React source code
│   ├── public/               # Static assets
│   └── package.json          # NPM dependencies
└── README.md                 # This file
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

If you encounter any issues or have questions:

1. Check the API documentation at `http://localhost:8080/swagger-ui.html`
2. Review the application logs for error details
3. Ensure all required services (MySQL, MongoDB, Qdrant) are running
4. Verify your API keys are correctly configured

## 🔮 Future Enhancements

- [ ] Mobile app development
- [ ] Advanced analytics dashboard
- [ ] Social sharing features
- [ ] Export functionality (PDF, JSON)
- [ ] Multi-language support
- [ ] Dark mode theme
- [ ] Calendar integration
- [ ] Mood tracking visualizations

---

Made with ❤️ for better journaling and self-reflection
