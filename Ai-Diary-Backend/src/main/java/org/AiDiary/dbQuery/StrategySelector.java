package org.AiDiary.dbQuery;

import org.AiDiary.enums.Query;

public interface StrategySelector {

    DbStrategy selectStrategy(Query query);
}
