package com.tp.testapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ResultatRecherche extends Activity {
	
	private LinearLayout l;
	private LinearLayout lTop;
	private Contact contact;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		
		setContentView(R.layout.activity_resultats);
				
		if(extras != null)
		{
			l = ((LinearLayout)(findViewById(R.id.layout)));
			lTop = ((LinearLayout)(findViewById(R.id.layoutTop)));
			
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
		contact = rechercheContact(query);
		
		if(contact != null)
		{			
			if(contact.getPhotoUri() != null)
			{
				// -- Photo de profil
				ImageView topLeft = ((ImageView)(findViewById(R.id.topLeft)));
				// Création de l'image à partir de l'uri
				topLeft.setImageURI(contact.getPhotoUri());
			} else {
				// -- Photo de profil
				ImageView topLeft = ((ImageView)(findViewById(R.id.topLeft)));
				// Création de l'image à partir de l'uri
				topLeft.setImageResource(R.drawable.default_user);
			}
			
			// -- Nom / Prénom du contact
			TextView topRight = ((TextView)(findViewById(R.id.topRight)));
			// Paramètres du texte
			topRight.setText(contact.getNom());
		} else {
			Toast.makeText(getApplicationContext(), "Pas trouvé", Toast.LENGTH_LONG).show();
		}
	}
	
	public Contact rechercheContact(String query)
	{     
		contact = null;
		
		Cursor people = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ('%' || ? || '%')", new String[]{query}, null);

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

			   // Si au moins un num√©ro
			   if(hasPhone.equals("1"))
			   {
				   Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
			                null,
			                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
			                new String[]{contactId},
			                null);

				   // Cherche le num√©ro du contact
				   if(phones.moveToNext())
				   {
					   // Récupère le num√©ro
					   String contactNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					   
					   if(contactPhoto == null)
						   contact = new Contact(contactId, contactName, contactNumber, null);
					   else
						   contact = new Contact(contactId, contactName, contactNumber, Uri.parse(contactPhoto));
				   }

				   phones.close();
			   }
					   			   
		   }
		}

		people.close();
		
		return contact;
	}
	
	public void appeler(View v)
	{
		try {
	        Intent callIntent = new Intent(Intent.ACTION_CALL);
	        callIntent.setData(Uri.parse("tel:" + contact.getNumero()));
	        startActivity(callIntent);
	    } catch (ActivityNotFoundException activityException) {
	    }
	}
	
	public void envoyermessage(View v)
	{
		try {
	        Intent messageIntent = new Intent(Intent.ACTION_VIEW);
	        messageIntent.setData(Uri.parse("sms:" + contact.getNumero()));
	        startActivity(messageIntent);
	    } catch (ActivityNotFoundException activityException) {
	    }
	}
}
