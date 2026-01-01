import { useState } from 'react'
import './SearchBar.css'

const SearchBar = ({ onTextSearch, onImageSearch, loading }) => {
  const [query, setQuery] = useState('')
  const [searchType, setSearchType] = useState('text')

  const handleSubmit = (e) => {
    e.preventDefault()
    if (searchType === 'text') {
      onTextSearch(query)
    } else {
      onImageSearch(query)
    }
  }

  const handleClear = () => {
    setQuery('')
    onTextSearch('')
  }

  return (
    <div className="search-bar">
      <form onSubmit={handleSubmit} className="search-form">
        <div className="search-input-group">
          <input
            type="text"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder={searchType === 'text' ? 'Search your diary entries...' : 'Search images by description...'}
            className="search-input"
          />
          
          <div className="search-type-toggle">
            <button
              type="button"
              className={searchType === 'text' ? 'active' : ''}
              onClick={() => setSearchType('text')}
            >
              <span>📝 Text</span>
            </button>
            <button
              type="button"
              className={searchType === 'image' ? 'active' : ''}
              onClick={() => setSearchType('image')}
            >
              <span>🖼️ Images</span>
            </button>
          </div>
        </div>

        <div className="search-actions">
          <button type="submit" disabled={loading} className="search-btn">
            <span>{loading ? '⏳' : '🔍'}</span>
            <span>Search</span>
          </button>
          {query && (
            <button type="button" onClick={handleClear} className="clear-btn">
              <span>✕</span>
              <span>Clear</span>
            </button>
          )}
        </div>
      </form>
    </div>
  )
}

export default SearchBar