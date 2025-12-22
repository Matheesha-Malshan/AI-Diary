package org.AiDiary.dbQuery;

import org.AiDiary.enums.Query;

public interface DbStrategy {

    boolean checkType(Query query);
    public void processQuery(String query);
}
