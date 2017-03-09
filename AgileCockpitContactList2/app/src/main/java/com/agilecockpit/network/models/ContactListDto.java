package com.agilecockpit.network.models;


import java.util.ArrayList;

public class ContactListDto extends DataModel {
    String message;
    String status;
    ArrayList<ContactDto> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<ContactDto> getResult() {
        return result;
    }

    public void setResult(ArrayList<ContactDto> result) {
        this.result = result;
    }





    public class ContactDto{
        String name;
        String uid;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }


    }



}
