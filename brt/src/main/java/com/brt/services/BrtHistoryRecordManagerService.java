package com.brt.services;

import com.brt.dto.CdrHistoryDto;
import com.brt.entities.BrtHistory;

public interface BrtHistoryRecordManagerService {

    void enrichHistory(BrtHistory brtHistory);

    BrtHistory convertToNewHistory(CdrHistoryDto oldHistory);
}
