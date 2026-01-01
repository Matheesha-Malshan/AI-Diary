import { useState } from 'react'
import './UserProfile.css'

const UserProfile = ({ onUserCreated }) => {
  const [formData, setFormData] = useState({
    name: '',
    email: ''
  })
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)

    try {
      const response = await fetch('http://localhost:8080/user/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          ...formData,
          createdDate: new Date().toISOString().split('T')[0]
        })
      })

      if (response.ok) {
        const userData = await response.json()
        onUserCreated(userData)
      } else {
        alert('Failed to create user profile')
      }
    } catch (error) {
      console.error('Error creating user:', error)
      alert('Error creating user profile')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="user-profile-container">
      <div className="user-profile-card">
        <h2>🌟 Welcome to AI Diary</h2>
        <p>Create your profile to start journaling</p>
        
        <form onSubmit={handleSubmit} className="user-form">
          <div className="form-group">
            <label htmlFor="name">Name</label>
            <input
              type="text"
              id="name"
              value={formData.name}
              onChange={(e) => setFormData(prev => ({ ...prev, name: e.target.value }))}
              required
              placeholder="Enter your name"
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              value={formData.email}
              onChange={(e) => setFormData(prev => ({ ...prev, email: e.target.value }))}
              required
              placeholder="Enter your email"
            />
          </div>
          
          <button type="submit" disabled={loading} className="create-profile-btn">
            {loading ? 'Creating...' : 'Create Profile'}
          </button>
        </form>
      </div>
    </div>
  )
}

export default UserProfile