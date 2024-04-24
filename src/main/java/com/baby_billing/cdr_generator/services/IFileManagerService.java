package com.baby_billing.cdr_generator.services;

import java.io.IOException;

public interface IFileManagerService {

    void checkAndCleanDataFolder() throws IOException;
}
