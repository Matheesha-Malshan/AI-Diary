package org.AiDiary.dbQuery.impl;

import org.AiDiary.dbQuery.DbStrategy;
import org.AiDiary.enums.Query;
import org.springframework.stereotype.Component;

@Component
public class SqlStrategyImpl implements DbStrategy {

    @Override
    public boolean checkType(Query query) {
        return query==Query.Sql;
    }

    public void processQuery(String query){

    }
}
