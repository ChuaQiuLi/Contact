package sg.edu.rp.c346.id20007649.contacts;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import sg.edu.rp.c346.id20007649.contact.R;

public class ContactDetail extends AppCompatActivity {

    // creating variables for our image view and text view and string. .
    private String contactName, contactNumber;
    private TextView contactTV, nameTV;
    private ImageView contactIV, callIV, messageIV;
    private CustomAdapter customAdapter;
    FloatingActionButton fabDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);


        // on below line we are getting data which
        // we passed in our adapter class with intent.
        contactName = getIntent().getStringExtra("name");
        contactNumber = getIntent().getStringExtra("contact");

        // initializing our views.
        nameTV = findViewById(R.id.idTVName);
        contactIV = findViewById(R.id.idIVContact);
        contactTV = findViewById(R.id.idTVPhone);
        nameTV.setText(contactName);
        contactTV.setText(contactNumber);
        callIV = findViewById(R.id.idIVCall);
        messageIV = findViewById(R.id.idIVMessage);
        fabDelete = findViewById(R.id.fabDelete);

        // on below line adding click listener for our calling image view.
        callIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method to make a call.
                makeCall(contactNumber);
            }
        });

        // on below line adding on click listener for our message image view.
        messageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method to send message
                sendMessage(contactNumber);
            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder myBuilder = new AlertDialog.Builder(ContactDetail.this);

                myBuilder.setTitle("Delete Contact");
                myBuilder.setMessage("Do you want to delete this contact? ");
                myBuilder.setCancelable(false);

                myBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteContact(getContentResolver(),contactNumber);
                        dialog.dismiss();
                        customAdapter.notifyDataSetChanged();
                        finish();





                    }
                });

                myBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });



                AlertDialog myDialog = myBuilder.create();

                myDialog.show();


            }

        });


    }


    private void sendMessage(String contactNumber) {

        // in this method we are calling an intent to send sms.
        // on below line we are passing our contact number.
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + contactNumber));
        intent.putExtra("sms_body", "Enter your messsage");
        startActivity(intent);

    }

    private void makeCall(String contactNumber) {

        // this method is called for making a call.
        // on below line we are calling an intent to make a call.
        Intent callIntent = new Intent(Intent.ACTION_CALL);

        // on below line we are setting data to it.
        callIntent.setData(Uri.parse("tel:" + contactNumber));

        // on below line we are checking if the calling permissions are granted not.
        if (ActivityCompat.checkSelfPermission(ContactDetail.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        // at last we are starting activity.
        startActivity(callIntent);
    }

    public static void deleteContact(ContentResolver contactHelper, String contactNumber) {

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        String[] args = new String[]{String.valueOf(getContactID(contactHelper, contactNumber))};
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection(ContactsContract.RawContacts.CONTACT_ID + "=?", args).build());


        try {
            contactHelper.applyBatch(ContactsContract.AUTHORITY, ops);



        }

        catch (RemoteException e) {
            e.printStackTrace();

        }

        catch (OperationApplicationException e) {
            e.printStackTrace();


        }

    }


    private static long getContactID(ContentResolver newContact, String contactNumber) {

        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(contactNumber));
        String[] projection = {ContactsContract.PhoneLookup._ID};
        Cursor cursor = null;
        try {
            cursor = newContact.query(contactUri, projection, null, null, null);
            if (cursor.moveToFirst()) {
                int personID = cursor.getColumnIndex(ContactsContract.PhoneLookup._ID);
                return cursor.getLong(personID);


            }
            return -1;

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;

            }
        }
        return -1;


    }




}




