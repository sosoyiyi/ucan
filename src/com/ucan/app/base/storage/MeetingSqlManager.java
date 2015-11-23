package com.ucan.app.base.storage;
public class MeetingSqlManager extends AbstractSQLManager {

    private static MeetingSqlManager sInstance = new MeetingSqlManager();

    private static MeetingSqlManager getInstance() {
        return sInstance;
    }


}
