package org.AiDiary.dbQuery.impl;

import org.AiDiary.dbQuery.DbStrategy;
import org.AiDiary.dbQuery.StrategySelector;
import org.AiDiary.enums.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StrategySelectorImpl implements StrategySelector {

    private final Map<Query,DbStrategy> strategyMap=new HashMap<>();

    public StrategySelectorImpl(List<DbStrategy> strategyList){

        for (DbStrategy strategy:strategyList){

            for (Query query:Query.values()){
                if (strategy.checkType(query)){
                    strategyMap.put(query,strategy);
                }
            }
        }
    }

    @Override
    public DbStrategy selectStrategy(Query query) {
        return strategyMap.get(query);
    }
}
