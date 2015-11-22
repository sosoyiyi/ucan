package com.ucan.app.common.core;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.ucan.app.base.manager.UCAppManager;
import com.ucan.app.common.contacts.ContactLogic;
import com.ucan.app.common.contacts.UCContacts;
import com.ucan.app.common.model.Phone;
import com.ucan.app.common.utils.LogUtil;

public class ContactsCache {
    // 初始化联系人
    public static final String ACTION_ACCOUT_INIT_CONTACTS = "com.ucan.app.intent.ACCOUT_INIT_CONTACTS";

    private static ContactsCache instance;

    private UCArrayLists<UCContacts> contacts;

    private LoadingTask asyncTask;

    private ContactsCache(){

    }

    public static ContactsCache getInstance() {
        if (instance == null) {
            instance = new ContactsCache();
        }

        return instance;
    }

    private class LoadingTask extends AsyncTask<Intent, Void, Long> {
        UCArrayLists<UCContacts> contactList = null;

        public LoadingTask() {
        }

        @Override
        protected Long doInBackground(Intent... intents) {
            try {
                LogUtil.d("contatsCache:开始加载联系人");
                //contactList = ContactLogic.getPhoneContacts(CCPAppManager.getContext());
                contactList = ContactLogic.getContractList(true);
                ContactLogic.getMobileContactPhoto(contactList);
            } catch (Exception ce) {
                ce.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long result) {
            if (contactList != null) {
                //PinyinHelper.release();
                UCArrayLists<UCContacts> oldContacts = contacts;
                contacts = contactList;
                //added
                ArrayList<String> phones = new ArrayList<String>();
                for (UCContacts o : contacts) {
                    List<Phone> phoneList = o.getPhoneList();
                    if (phoneList == null) {
                        continue;
                    }
                    for (Phone phone : phoneList) {
                        if (!TextUtils.isEmpty(phone.getPhoneNum()))
                            phones.add(phone.getPhoneNum());
                    }
                }
                String[] array = phones.toArray(new String[]{});
                Intent intent = new Intent(ACTION_ACCOUT_INIT_CONTACTS);
                intent.putExtra("array", array);

                UCAppManager.getContext().sendBroadcast(intent);
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

    public synchronized void load() {
        try {
            if (asyncTask == null) {
                asyncTask = new LoadingTask();
            }
            asyncTask.execute();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        try {
            stop();
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            if (asyncTask != null && !asyncTask.isCancelled()) {
                asyncTask.cancel(true);
                asyncTask = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the contacts
     */
    public synchronized UCArrayLists<UCContacts> getContacts() {
        return contacts;
    }

}
