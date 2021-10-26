package com.romantulchak.clouddisk.service;

import java.util.UUID;

public interface ElementService {

    void restoreElement(UUID elementLink);

    void preRemoveElement(UUID elementLink, String driveName);

}
