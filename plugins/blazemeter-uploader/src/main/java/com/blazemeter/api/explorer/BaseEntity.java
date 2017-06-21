package com.blazemeter.api.explorer;

import com.blazemeter.api.entity.BlazemeterReport;
import com.blazemeter.jmeter.StatusNotifierCallback;

public class BaseEntity extends AbstractHttpEntity {

    private final String id;
    private final String name;

    public BaseEntity(StatusNotifierCallback notifier, String address, String dataAddress, BlazemeterReport report, String id, String name) {
        super(notifier, address, dataAddress, report);
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
