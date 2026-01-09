import { useState } from 'react'
import './DiaryEntry.css'

const DiaryEntry = ({ userId, onEntryCreated, onCancel }) => {
  const [entryData, setEntryData] = useState({
    text: '',
    image: null,
    imageDescription: ''
  })
  const [loading, setLoading] = useState(false)
  const [imagePreview, setImagePreview] = useState(null)

  const handleImageChange = (e) => {
    const file = e.target.files[0]
    if (file) {
      setEntryData(prev => ({ ...prev, image: file }))
      
      // Create preview
      const reader = new FileReader()
      reader.onload = (e) => setImagePreview(e.target.result)
      reader.readAsDataURL(file)
    }
  }

  const removeImage = () => {
    setEntryData(prev => ({ ...prev, image: null, imageDescription: '' }))
    setImagePreview(null)
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)

    try {
      // First create text entry
      const textResponse = await fetch('http://localhost:8080/text/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          user_id: userId,
          promt: entryData.text,
          createDate: new Date().toISOString().split('T')[0]
        })
      })

      if (!textResponse.ok) {
        throw new Error('Failed to create text entry')
      }

      const textResult = await textResponse.json()

      // If there's an image, create image entry
      if (entryData.image) {
        const imageFormData = new FormData()
        imageFormData.append('image', entryData.image)
        imageFormData.append('user_id', userId)
        imageFormData.append('createDate', new Date().toISOString().split('T')[0])
        imageFormData.append('description', entryData.imageDescription)
        imageFormData.append('entryId', Math.floor(Math.random() * 2147483647))

        try {
          const imageResponse = await fetch('http://localhost:8080/image/create', {
            method: 'POST',
            body: imageFormData
          })

          if (!imageResponse.ok) {
            console.warn('Failed to upload image, but text entry was created')
          }
        } catch (error) {
          console.warn('Image upload failed:', error)
        }
      }

      // Create entry object for local state
      const newEntry = {
        id: Math.floor(Math.random() * 2147483647), // Generate valid int ID
        text: entryData.text,
        image: imagePreview,
        imageDescription: entryData.imageDescription,
        date: new Date().toISOString().split('T')[0],
        aiResponse: textResult
      }

      onEntryCreated(newEntry)
      
      // Reset form
      setEntryData({ text: '', image: null, imageDescription: '' })
      setImagePreview(null)
      
    } catch (error) {
      console.error('Error creating entry:', error)
      alert('Error creating diary entry')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="diary-entry-container">
      <div className="diary-entry-card">
        <h3>✏️ New Diary Entry</h3>
        
        <form onSubmit={handleSubmit} className="entry-form">
          <div className="form-group">
            <label htmlFor="text">What's on your mind?</label>
            <textarea
              id="text"
              value={entryData.text}
              onChange={(e) => setEntryData(prev => ({ ...prev, text: e.target.value }))}
              required
              placeholder="Write about your day, thoughts, feelings..."
              rows={6}
            />
          </div>

          <div className="image-section">
            <label className="image-upload-label">
              📷 Add an image (optional)
              <input
                type="file"
                accept="image/*"
                onChange={handleImageChange}
                className="image-input"
              />
            </label>

            {imagePreview && (
              <div className="image-preview">
                <img src={imagePreview} alt="Preview" />
                <button type="button" onClick={removeImage} className="remove-image-btn">
                  ✕ Remove
                </button>
                
                <div className="form-group">
                  <label htmlFor="imageDescription">Describe this image</label>
                  <input
                    type="text"
                    id="imageDescription"
                    value={entryData.imageDescription}
                    onChange={(e) => setEntryData(prev => ({ ...prev, imageDescription: e.target.value }))}
                    placeholder="What's in this image?"
                  />
                </div>
              </div>
            )}
          </div>

          <div className="form-actions">
            <button type="button" onClick={onCancel} className="cancel-btn">
              Cancel
            </button>
            <button type="submit" disabled={loading} className="save-btn">
              {loading ? 'Saving...' : '💾 Save Entry'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default DiaryEntry