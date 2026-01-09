import { useState, useEffect } from 'react'
import DiaryEntry from './components/DiaryEntry'
import DiaryList from './components/DiaryList'
import UserProfile from './components/UserProfile'
import './App.css'

function App() {
  const [user, setUser] = useState(null)
  const [entries, setEntries] = useState([])
  const [showNewEntry, setShowNewEntry] = useState(false)

  useEffect(() => {
    // Check if user exists in localStorage
    const savedUser = localStorage.getItem('diaryUser')
    if (savedUser) {
      setUser(JSON.parse(savedUser))
    }
  }, [])

  const handleUserCreated = (userData) => {
    setUser(userData)
    localStorage.setItem('diaryUser', JSON.stringify(userData))
  }

  const handleEntryCreated = (entry) => {
    setEntries(prev => [entry, ...prev])
    setShowNewEntry(false)
  }

  if (!user) {
    return <UserProfile onUserCreated={handleUserCreated} />
  }

  return (
    <div className="app">
      <header className="app-header">
        <h1>🌟 AI Diary</h1>
        <div className="user-info">
          <span>Welcome, {user.name}</span>
          <div className="user-actions">
            <button 
              className="new-entry-btn"
              onClick={() => setShowNewEntry(!showNewEntry)}
            >
              {showNewEntry ? '✕ Cancel' : '✏️ New Entry'}
            </button>
            <button 
              className="logout-btn"
              onClick={() => {
                localStorage.removeItem('diaryUser')
                setUser(null)
                setEntries([])
              }}
            >
              🚪 Logout
            </button>
          </div>
        </div>
      </header>

      <main className="app-main">
        {showNewEntry && (
          <DiaryEntry 
            userId={user.userId} 
            onEntryCreated={handleEntryCreated}
            onCancel={() => setShowNewEntry(false)}
          />
        )}
        
        <DiaryList 
          userId={user.userId} 
          entries={entries}
          setEntries={setEntries}
        />
      </main>
    </div>
  )
}

export default App
