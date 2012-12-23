package com.tp.testapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ResultatRecherche extends Activity {
	
	private LinearLayout l;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		
		setContentView(R.layout.activity_resultats);
				
		if(extras != null)
		{
			l = ((LinearLayout)(findViewById(R.id.layout)));
			recherche(extras.getString("research"));
		}
		else
			Toast.makeText(getApplicationContext(), "Une erreur est survenue !", Toast.LENGTH_LONG).show();
	}
	
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slid_back_out, R.anim.slid_back_in);
		
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
		
		imm.toggleSoftInput(imm.SHOW_FORCED, 0);
	}

	public void recherche(String query)
	{
		Contact contact = rechercheContact(query);
		
		if(contact != null)
		{			
			ImageView photoProfil = new ImageView(this);
			
			photoProfil.setImageURI(contact.getPhotoUri());
			l.addView(photoProfil);
			
		} else {
			Toast.makeText(getApplicationContext(), "Pas trouv�", Toast.LENGTH_LONG).show();
		}
	}
	
	public Contact rechercheContact(String query)
	{     
		Contact contact = null;
		
		Cursor people = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

		while(people.moveToNext()) 
		{
		   int nameFieldColumnIndex = people.getColumnIndex(PhoneLookup.DISPLAY_NAME);
		   String contactName = people.getString(nameFieldColumnIndex);
		   String contactId = people.getString(people.getColumnIndex(ContactsContract.Contacts._ID));
		   String contactPhoto = people.getString(people.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
		   
		   // Si c'est exactement la personne qu'on cherche 
		   if(contactName.equals(query))
		   {
			   String hasPhone = people.getString(people.getColumnIndex(PhoneLookup.HAS_PHONE_NUMBER));

			   // Si au moins un numéro
			   if(hasPhone.equals("1"))
			   {
				   Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
			                null,
			                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
			                new String[]{contactId},
			                null);

				   // Cherche le numéro du contact
				   if(phones.moveToNext())
				   {
					   // Récupère le numéro
					   String contactNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					   
					   // Création du contact en fonction des élèments donnés
					   contact = new Contact(contactId, contactName, contactNumber, Uri.parse(contactPhoto));
				   }

				   phones.close();
			   }
					   			   
		   }
		}

		people.close();
		
		return contact;
	}
}
