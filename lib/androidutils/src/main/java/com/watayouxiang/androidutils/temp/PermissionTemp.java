package com.watayouxiang.androidutils.temp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/24
 *     desc   :
 * </pre>
 */
public class PermissionTemp extends Activity {

    private static final int CONTACTS_REQUEST_CODE = 1001;

    private final String[] contactsPermissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
    private ContentResolver resolver;
    private List<ContactsBean> mContacts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resolver = getContentResolver();
        mContacts = new ArrayList<>();

        boolean isGranted = checkSelfPermission(this);
        if (!isGranted) {
            requestPermissions(this);
        } else {
            getContactsData();
        }
    }

    private void getContactsData() {
        Cursor cursor = resolver.query(ContactsContract.RawContacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            ContactsBean bean = new ContactsBean();
            bean.id = cursor.getInt(cursor.getColumnIndex(ContactsContract.RawContacts._ID));
            bean.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));

            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.Data.RAW_CONTACT_ID + "=?",
                    new String[]{String.valueOf(bean.id)},
                    null);
            if (phoneCursor.moveToFirst()) {
                bean.phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phoneCursor.close();

            mContacts.add(bean);
        }
        cursor.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CONTACTS_REQUEST_CODE) {
            boolean result = true;
            int length = grantResults.length;
            if (length == contactsPermissions.length) {
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        result = false;
                        break;
                    }
                }
            }
            if (result) {
                notifyReqPermissionsSuccess();
            }
        }
    }

    private void notifyReqPermissionsSuccess() {
        // 通知请求权限成功
        getContactsData();
    }

    public boolean checkSelfPermission(Context context) {
        int readContacts = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS);
        int writeContacts = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS);
        return readContacts == PackageManager.PERMISSION_GRANTED
                && writeContacts == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, contactsPermissions, CONTACTS_REQUEST_CODE);
    }

    public static class ContactsBean {
        public int id;
        public String name;
        public String phone;
    }
}
