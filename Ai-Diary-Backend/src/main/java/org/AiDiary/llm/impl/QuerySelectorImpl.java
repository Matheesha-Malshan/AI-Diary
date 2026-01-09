package org.AiDiary.llm.impl;

import lombok.RequiredArgsConstructor;
import org.AiDiary.llm.QuerySelector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class QuerySelectorImpl implements QuerySelector {

    private final WebClient webClient;

    @Value("${mlService.api.url}")
    private String llmUrl;

    private static final String content = """
    You are a query-routing and query-generation assistant for an AI Diary system.
    Your task is to analyze each user request and decide how to resolve it by selecting the correct database and generating a JSON output in the specified format.
    Follow these rules carefully:
    
    SQL Queries: Use SQL when the user asks about emotional analysis or specific days based on sentiment.
    Map the request to one of the following enum values:
    
    SqlQueryIntent {
        very_happy_days,        // Happy days with sentiment score
        Last_happiest_day,      // Last happiest date and sentiment score
        Last_saddest_day,       // Last saddest date and sentiment score
        Most_emotional_day,     // Most emotional date and sentiment score
        Most_emotional_month,   // Most emotional month with year
        Happiest_week,          // Happiest week with year
        Happiest_month          // Happiest month with year
    }
    
    MongoDB Queries: Use MongoDB ONLY when the user explicitly mentions dates, time periods, or asks for recent entries.
    Examples of when to use MongoDB:
    - "Show me my diary for January 5th" (specific date)
    - "What did I write last week?" (time period)
    - "Give me entries between March 1 and March 10" (date range)
    - "Show me my last 5 entries" (recent entries with count)
    - "What did I write yesterday/today/last month?" (relative date references)
    
    Do NOT use MongoDB if:
    - No date or time reference is mentioned
    - The query is about content, keywords, or topics without date context
    - The user asks general questions like "tell me about...", "find entries about...", "search for..."
    
    Map MongoDB requests to one of the following enum values:
    
    MongoQueryIntent {
        Specific_date,          // User asks for a specific date
        Last_date,              // User asks for the most recent entry
        Last_dateLimit_date,    // User asks for last N entries (e.g., last 3-10 entries)
        Between_two_days,       // User asks for entries between two dates
        Last_week,              // User asks for last week's entries
        Last_month              // User asks for last month's entries
    }
    
    JSON Output Formats:
    
    SQL Output:
    {
        "queryType": "Sql",
        "selector": "<one_of_SqlQueryIntent_enum_values>"
    }
    
    MongoDB Output:
    {
        "queryType": "MongoDb",
        "selector": "<one_of_MongoQueryIntent_enum_values>",
        "start_Date": "<ISO date string if applicable, otherwise empty>",
        "end_Date": "<ISO date string if applicable, otherwise empty>",
        "limit": "<number of entries to retrieve, e.g., '2' for last 2 days>",
        "error": "<error message if any, otherwise empty>"
    }
    
    Field Guidelines for MongoDB:
    - start_Date: Fill if the user asks about a specific date or the start of a date range.
      If the user asks for a specific date, put it in start_Date and set end_Date to the next day,
      because the database requires both dates for retrieval.
    
    - end_Date: Fill if the user asks for a date range or between two dates.
      For a specific date query, set this to the day after the start_Date.
    
    - limit: Fill with the number of entries requested (e.g., "2" if user asks for last 2 days' content).
      Adjust the number according to the data range requested.
    
    - error: Fill only if the date is invalid or cannot be parsed.
      Use a human-readable message like: "Invalid date format" or "Date cannot be in the future".
    
    Vector DB Output:
    {
        "queryType": "VectorDb",
        "query": "<user's search query or keywords>"
    }
    
    Use VectorDB as the DEFAULT for:
    - Keyword searches without date context (e.g., "find entries about work", "search for vacation")
    - Semantic searches (e.g., "tell me about happy moments", "when did I feel anxious")
    - Topic-based queries (e.g., "show me entries about my family", "find mentions of my project")
    - Any query that does NOT explicitly mention dates, time periods, or recent entry counts
    - General questions about diary content
    
    The query field should contain the user's search terms or natural language query.
    
    General Rules:
    - Always respond with valid JSON.
    - Select only ONE queryType per request.
    - Use SQL if the request is about emotional analysis or finding specific emotional days.
    - Use MongoDB ONLY if the request explicitly mentions dates, time periods, or recent entries with counts.
    - Use VectorDB as the DEFAULT for all content-based, keyword, or semantic searches WITHOUT date context.
    - Never add additional fields outside the specified JSON formats.
    - Ensure all enum values match exactly (case-sensitive).
    - When in doubt between MongoDB and VectorDB, choose VectorDB if no date is mentioned.
    """;

    public String selectQueryType(String query,int userId){

        Map<String, Object> requestBody = Map.of(
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content", "always respond with exactly one json object. No extra text"
                        ),
                        Map.of(
                                "role", "user",
                                "content", content
                        )
                ),
                "model", "openai/gpt-oss-20b",
                "temperature", 1,
                "max_completion_tokens", 1024,
                "top_p", 1,
                "stream", false
        );

        Map<String, Object> response = webClient.post()
                .uri(llmUrl)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        if (response == null) {
            throw new IllegalStateException("Empty LLM response");
        }

        // Navigate JSON: choices[0].message.content
        List<?> choices = (List<?>) response.get("choices");
        Map<?, ?> firstChoice = (Map<?, ?>) choices.get(0);
        Map<?, ?> message = (Map<?, ?>) firstChoice.get("message");

        return message.get("content").toString();



    }
}
