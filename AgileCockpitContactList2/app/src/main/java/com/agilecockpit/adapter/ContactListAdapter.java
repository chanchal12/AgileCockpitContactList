package com.agilecockpit.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.agilecockpit.R;
import com.agilecockpit.network.models.ContactListDto;

import java.util.List;

import com.agilecockpit.db.ContactListDBHandler;
import com.agilecockpit.db.DeleteContactListDBHandler;

/**
 * Created by chanchal.sharma on 3/9/2017.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {

    private List<ContactListDto.ContactDto> mContactList;
     Context mContext = null;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTVName;
        public ImageView mIVDelete;


        public MyViewHolder(View view) {
            super(view);
            mTVName = (TextView) view.findViewById(R.id.tv_name);
            mIVDelete = (ImageView) view.findViewById(R.id.iv_delete);

        }
    }


    public ContactListAdapter(Context mContext,List<ContactListDto.ContactDto> mContactList) {
        this.mContactList = mContactList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ContactListDto.ContactDto contactDto = mContactList.get(position);
         holder.mTVName.setText(contactDto.getName());
        holder.mIVDelete.setTag(contactDto);
        holder.mIVDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! (v.getTag() instanceof  ContactListDto.ContactDto)) {
                    return;
                }
                    ContactListDto.ContactDto contactDto = (ContactListDto.ContactDto) v.getTag();
                        confirmDelete(mContext,mContext.getString(R.string.confirm_msg)+" " + contactDto.getName() +" ?" ,contactDto,position);
                }

        });

    }

    private void confirmDelete(final Context context,String message,final ContactListDto.ContactDto contactDto,final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle("Delete " + contactDto.getName()+"?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mContactList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mContactList.size());
                DeleteContactListDBHandler.insert(mContext,contactDto);
                ContactListDBHandler.deleteContact(mContext,Integer.parseInt(contactDto.getUid()));
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }


}
