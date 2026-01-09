import './DiaryEntryCard.css'

const DiaryEntryCard = ({ entry }) => {
  const formatDate = (dateString) => {
    const date = new Date(dateString)
    return date.toLocaleDateString('en-US', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    })
  }

  const getSentimentEmoji = (sentiment) => {
    if (sentiment >= 0.5) return '😊'
    if (sentiment >= 0) return '😐'
    return '😔'
  }

  return (
    <div className="diary-entry-card">
      <div className="entry-header">
        <span className="entry-date">{formatDate(entry.date)}</span>
        {entry.aiResponse && entry.aiResponse.sentimentList && entry.aiResponse.sentimentList[0] !== undefined && (
          <span className="sentiment">
            {getSentimentEmoji(entry.aiResponse.sentimentList[0])}
          </span>
        )}
      </div>

      <div className="entry-content">
        <p className="entry-text">{entry.text}</p>
        
        {entry.image && (
          <div className="entry-image">
            <img src={entry.image} alt={entry.imageDescription || 'Diary image'} />
            {entry.imageDescription && (
              <p className="image-description">{entry.imageDescription}</p>
            )}
          </div>
        )}
      </div>

      {entry.aiResponse && (
        <div className="ai-response">
          <h4>🤖 AI Analysis</h4>
          {entry.aiResponse.proccesedQuery && (
            <p><strong>Processed:</strong> {entry.aiResponse.proccesedQuery}</p>
          )}
          {entry.aiResponse.errorType && (
            <p className="error"><strong>Note:</strong> {entry.aiResponse.errorType}</p>
          )}
        </div>
      )}
    </div>
  )
}

export default DiaryEntryCard