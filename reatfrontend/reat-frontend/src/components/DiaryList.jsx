import { useState, useEffect } from 'react'
import DiaryEntryCard from './DiaryEntryCard'
import SearchBar from './SearchBar'
import './DiaryList.css'

const DiaryList = ({ userId, entries, setEntries }) => {
  const [searchResults, setSearchResults] = useState(null)
  const [searchType, setSearchType] = useState('text')
  const [loading, setLoading] = useState(false)

  const handleSearch = async (query) => {
    if (!query.trim()) {
      setSearchResults(null)
      return
    }

    setLoading(true)
    try {
      const response = await fetch(`http://localhost:8080/text/find-by-text/${userId}/${encodeURIComponent(query)}`)
      
      if (response.ok) {
        const results = await response.json()
        setSearchResults({ ...results, type: 'text' })
        setSearchType('text')
      } else {
        console.error('Search failed')
        setSearchResults({ error: 'Search failed', type: 'text' })
      }
    } catch (error) {
      console.error('Search error:', error)
      setSearchResults({ error: 'Search error', type: 'text' })
    } finally {
      setLoading(false)
    }
  }

  const handleImageSearch = async (query) => {
    if (!query.trim()) {
      setSearchResults(null)
      return
    }

    setLoading(true)
    try {
      const response = await fetch(`http://localhost:8080/image/search-by-text/${userId}/${encodeURIComponent(query)}`)
      
      if (response.ok) {
        const results = await response.json()
        console.log('Image search results:', results)
        setSearchResults({ ...results, type: 'image' })
        setSearchType('image')
      } else {
        console.error('Image search failed')
        setSearchResults({ error: 'Image search failed', type: 'image' })
      }
    } catch (error) {
      console.error('Image search error:', error)
      setSearchResults({ error: 'Image search error', type: 'image' })
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="diary-list">
      <SearchBar 
        onTextSearch={handleSearch}
        onImageSearch={handleImageSearch}
        loading={loading}
      />

      {searchResults && (
        <div className="search-results">
          <h3>🔍 {searchResults.type === 'image' ? 'Image' : 'Text'} Search Results</h3>
          {searchResults.error ? (
            <p className="error">Error: {searchResults.error}</p>
          ) : searchResults.presentError ? (
            <p className="error">{searchResults.message}</p>
          ) : (
            <div className="search-content">
              {searchResults.type === 'image' ? (
                // Display image search results
                <div className="image-search-results">
                  {searchResults.image && searchResults.image.length > 0 ? (
                    <div className="image-results-grid">
                      {searchResults.image.map((imageData, index) => (
                        <div key={index} className="image-result-item">
                          <img 
                            src={`data:image/jpeg;base64,${imageData}`} 
                            alt={`Search result ${index + 1}`}
                            onError={(e) => {
                              // If base64 doesn't work, try as direct URL
                              e.target.src = imageData;
                            }}
                          />
                          {searchResults.dateList && searchResults.dateList[index] && (
                            <small>Date: {searchResults.dateList[index]}</small>
                          )}
                          {searchResults.chunkList && searchResults.chunkList[index] && (
                            <p className="image-description">{searchResults.chunkList[index]}</p>
                          )}
                        </div>
                      ))}
                    </div>
                  ) : (
                    <p>No images found</p>
                  )}
                </div>
              ) : (
                // Display text search results
                searchResults.contentList && searchResults.contentList.length > 0 ? (
                  <div className="search-items">
                    {searchResults.contentList.map((content, index) => (
                      <div key={index} className="search-item">
                        <p>{content}</p>
                        {searchResults.dateList && searchResults.dateList[index] && (
                          <small>Date: {searchResults.dateList[index]}</small>
                        )}
                        {searchResults.sentimentList && searchResults.sentimentList[index] !== undefined && (
                          <small>Sentiment: {searchResults.sentimentList[index].toFixed(2)}</small>
                        )}
                      </div>
                    ))}
                  </div>
                ) : (
                  <p>No results found</p>
                )
              )}
            </div>
          )}
        </div>
      )}

      <div className="entries-section">
        <h3>📖 Your Diary Entries</h3>
        {entries.length === 0 ? (
          <div className="no-entries">
            <p>No entries yet. Start writing your first diary entry!</p>
          </div>
        ) : (
          <div className="entries-grid">
            {entries.map(entry => (
              <DiaryEntryCard key={entry.id} entry={entry} />
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

export default DiaryList