package modelos;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class UsuariosSchema2pojo {

@SerializedName("Id")
@Expose
private String id;
@SerializedName("Username")
@Expose
private String username;
@SerializedName("Password")
@Expose
private String password;
@SerializedName("Email")
@Expose
private String email;
@SerializedName("ID_Android")
@Expose
private Object iDAndroid;
@SerializedName("Telefono")
@Expose
private String telefono;
@SerializedName("FechaCreacion")
@Expose
private String fechaCreacion;

/**
* 
* @return
* The id
*/
public String getId() {
return id;
}

/**
* 
* @param id
* The Id
*/
public void setId(String id) {
this.id = id;
}

/**
* 
* @return
* The username
*/
public String getUsername() {
return username;
}

/**
* 
* @param username
* The Username
*/
public void setUsername(String username) {
this.username = username;
}

/**
* 
* @return
* The password
*/
public String getPassword() {
return password;
}

/**
* 
* @param password
* The Password
*/
public void setPassword(String password) {
this.password = password;
}

/**
* 
* @return
* The email
*/
public String getEmail() {
return email;
}

/**
* 
* @param email
* The Email
*/
public void setEmail(String email) {
this.email = email;
}

/**
* 
* @return
* The iDAndroid
*/
public Object getIDAndroid() {
return iDAndroid;
}

/**
* 
* @param iDAndroid
* The ID_Android
*/
public void setIDAndroid(Object iDAndroid) {
this.iDAndroid = iDAndroid;
}

/**
* 
* @return
* The telefono
*/
public String getTelefono() {
return telefono;
}

/**
* 
* @param telefono
* The Telefono
*/
public void setTelefono(String telefono) {
this.telefono = telefono;
}

/**
* 
* @return
* The fechaCreacion
*/
public String getFechaCreacion() {
return fechaCreacion;
}

/**
* 
* @param fechaCreacion
* The FechaCreacion
*/
public void setFechaCreacion(String fechaCreacion) {
this.fechaCreacion = fechaCreacion;
}

}