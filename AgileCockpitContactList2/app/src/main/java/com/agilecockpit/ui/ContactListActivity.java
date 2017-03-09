package com.agilecockpit.ui;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.agilecockpit.R;
import com.agilecockpit.adapter.ContactListAdapter;
import com.agilecockpit.network.InitializeVolley;
import com.agilecockpit.network.InterceptorRequest;
import com.agilecockpit.network.RequestCreator;
import com.agilecockpit.network.models.ContactListDto;
import com.agilecockpit.network.models.DataModel;
import com.agilecockpit.recycleview.animation.DividerItemDecoration;
import com.agilecockpit.recycleview.animation.RecyclerTouchListener;
import com.agilecockpit.utilities.HTTPProtocolConstants;
import com.agilecockpit.utilities.UtilitlyManager;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;


import com.agilecockpit.db.ContactListDBHandler;
import com.agilecockpit.db.DeleteContactListDBHandler;

public class ContactListActivity extends AppCompatActivity{
    private ArrayList<ContactListDto.ContactDto> mContactList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ContactListAdapter mAdapter;
    private EventBus mEventBus;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        mEventBus = EventBus.getDefault();
        showUI();
    }



    public void showUI() {
        try {
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

            mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    ContactListDto.ContactDto movie = mContactList.get(position);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
        getContactList();

    }

    private void getContactList(){
        if (!UtilitlyManager.isNetworkAvailable(this)) {
            UtilitlyManager.showPopUp(this, getString(R.string.response_msg));
            return;
        }
        mProgressDialog = UtilitlyManager.showProgressDialog(getString(R.string.auth_msg), this);
        InterceptorRequest request = RequestCreator.getInstance().getContactList(this) ;
        InitializeVolley.getRequestQueue(this).add(request);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (mEventBus != null && !mEventBus.isRegistered(this))
            mEventBus.register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                getContactList();
                // Complete with your code
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEventBus != null)
            mEventBus.unregister(this);
    }

    @Subscribe
    public void OnEvent(DataModel model) {
        UtilitlyManager.dismissProgressDialog(mProgressDialog);
        if (model == null || model.volleyError != null || !(model instanceof ContactListDto)) {
            return;
        }
        ContactListDto contactListDto = (ContactListDto) model;
        if(contactListDto.httpStatusCode != HTTPProtocolConstants.SUCCESS){
            Toast.makeText(ContactListActivity.this,getString(R.string.response_msg), Toast.LENGTH_SHORT).show();
            return;
        }
        mContactList = contactListDto.getResult();
        if (mContactList != null && mContactList.size() > 0) {
            mContactList =  updateContactList(this,mContactList);
        }
        mAdapter = new ContactListAdapter(this,mContactList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }



    public static ArrayList<ContactListDto.ContactDto> updateContactList(Context context, ArrayList<ContactListDto.ContactDto> contactListFromServer) {
                ArrayList<Integer> deleteContactList = DeleteContactListDBHandler.getDeleteContactIds(context);
                if (contactListFromServer != null && contactListFromServer.size() > 0) {
                    contactListFromServer = UtilitlyManager.filterContactList(contactListFromServer, deleteContactList);
                    for( ContactListDto.ContactDto contactDto : contactListFromServer) {
                        ContactListDBHandler.saveOrUpdate(context, contactDto);
                    }
                }
        return contactListFromServer;
    }
}

