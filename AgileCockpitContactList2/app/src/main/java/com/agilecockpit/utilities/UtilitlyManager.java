
package com.agilecockpit.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.agilecockpit.network.models.ContactListDto;

import java.util.ArrayList;


public class UtilitlyManager {
    
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager ConnectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (ConnectMgr == null)
            return false;
        NetworkInfo NetInfo = ConnectMgr.getActiveNetworkInfo();
        if (NetInfo == null)
            return false;

        return NetInfo.isConnected();
    }

    public static void showPopUp(Context context, String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int id) {dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ProgressDialog showProgressDialog(String message,Activity activity) {
        return ProgressDialog.show(activity, null, message, true, false);
    }
    public static void dismissProgressDialog(ProgressDialog mProgressDialog) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public static ArrayList<ContactListDto.ContactDto> filterContactList(ArrayList<ContactListDto.ContactDto> serverContactList,ArrayList<Integer> deleteContactIds) {
        try {
            if (deleteContactIds == null || deleteContactIds.size() == 0) {
                return serverContactList;
            } else {
                for (int caseId : deleteContactIds) {
                    boolean flag = false;
                    int idx = 0;
                    for (; idx < serverContactList.size(); idx++) {
                        ContactListDto.ContactDto contactDto = serverContactList.get(idx);
                        if(contactDto == null){
                            continue;
                        }
                        if (caseId == Integer.parseInt(contactDto.getUid())) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        serverContactList.remove(idx);
                    }
                }
            }

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return serverContactList;
    }






}



