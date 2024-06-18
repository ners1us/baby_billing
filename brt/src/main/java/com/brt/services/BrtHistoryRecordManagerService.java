package com.brt.services;

import com.brt.dto.CdrHistory;
import com.brt.entities.BrtHistory;

public interface BrtHistoryRecordManagerService {

    void enrichHistory(BrtHistory brtHistory);

    BrtHistory convertToNewHistory(CdrHistory oldHistory);
}
