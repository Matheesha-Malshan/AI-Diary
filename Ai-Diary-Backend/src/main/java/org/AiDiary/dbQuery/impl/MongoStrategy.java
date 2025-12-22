package org.AiDiary.dbQuery.impl;

import org.AiDiary.dbQuery.DbStrategy;
import org.AiDiary.enums.Query;
import org.springframework.stereotype.Component;

@Component
public class MongoStrategy implements DbStrategy {


    @Override
    public boolean checkType(Query query) {
        return Query.MongoDb==query;
    }

    public void processQuery(String query){

    }


}
