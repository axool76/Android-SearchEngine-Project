package com.tp.testapp;

import android.net.Uri;

public class Contact {
	private String nom;
	private String id;
	private String numero;
	private Uri photoUri;
	
	public Contact(String id, String nom, String numero, Uri photoUri) {
		this.nom = nom;
		this.id = id;
		this.numero = numero;
		this.photoUri = photoUri;
	}
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Uri getPhotoUri() {
		return photoUri;
	}
	public void setPhotoUri(Uri photoUri) {
		this.photoUri = photoUri;
	}
}
