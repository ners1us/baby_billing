package com.baby_billing.brt.services;

import com.baby_billing.brt.entities.BrtHistory;
import com.baby_billing.cdr_generator.entities.History;

public interface IBrtHistoryRecordManagerService {

    void enrichHistory(BrtHistory brtHistory);

    BrtHistory convertToNewHistory(History oldHistory);
}
