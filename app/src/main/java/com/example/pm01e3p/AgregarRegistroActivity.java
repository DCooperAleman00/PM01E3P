package com.example.pm01e3p;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AgregarRegistroActivity extends AppCompatActivity {

    //View
    private ImageView profileIv2;
    private EditText nameEt,phoneEt,emailEt,dobEt,bioEt;
    private FloatingActionButton saveBtn;

    //Actionbar
    private ActionBar actionBar;
    //Permiso de la clase Constants
    private  static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    //selección de imagen Constants
    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;
    // matrices de permisos
    private String[] cameraPermissions; // cámara y almacenamiento
    private String [] storagePermissions;// solo almacenamiento
    // variables (constain datos para guardar)
    private Uri imageUri;
    private String name, phone, email, dob, bio;

    //db helper
    private MyDbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_registro);

        //Inicializacion
        actionBar = getSupportActionBar();
        //Titulo
        actionBar.setTitle("Agregar Registro");
        //Boton Negro
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        profileIv2 = findViewById(R.id.profileIv2);
        nameEt = findViewById(R.id.nameEt);
        phoneEt = findViewById(R.id.phoneEt);
        emailEt = findViewById(R.id.emailEt);
        dobEt = findViewById(R.id.dobEt);
        bioEt = findViewById(R.id.bioEt);
        saveBtn = findViewById(R.id.saveBtn);

        //Inicializar BD Helper
        dbHelper = new MyDbHelper(this);

        //Inicializamos Permisos arrays
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        profileIv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // muestra el cuadro de diálogo de selección de imagen
                imagePickDialog();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });
    }

    private void inputData(){
        //get data
        name = ""+nameEt.getText().toString().trim();
        phone = ""+phoneEt.getText().toString().trim();
        email = ""+emailEt.getText().toString().trim();
        dob = ""+dobEt.getText().toString().trim();
        bio = ""+bioEt.getText().toString().trim();

        //guarda en la base de datos
        String timestamp = ""+System.currentTimeMillis();
        long id = dbHelper.insertRecord(
                ""+name,
                ""+imageUri,
                ""+bio,
                ""+phone,
                ""+email,
                ""+dob,
                ""+timestamp,
                ""+timestamp
        );

        Toast.makeText(this, "Registro agregado contra ID: "+id, Toast.LENGTH_SHORT).show();
    }
    private void imagePickDialog(){
        String[] options = {"Camara", "Galeria"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar imagen");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which==0){
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else{
                        PickFromCamera();
                    }

                }
                else if (which==1){
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else{
                        PickFromGallery();
                    }
                }
            }
        });

        builder.create().show();
    }

    private void PickFromGallery() {
        // intento de elegir la imagen de la galería, la imagen se devolverá en el método onActivityResult
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void PickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Titulo de la Imagen");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descripción de la imagen");
        //put image Uri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // Intento de abrir la cámara para la imagen
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission(){
        //comprobar si el permiso de almacenamiento está habilitado o no
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private  void requestStoragePermission(){
        // solicita el permiso de almacenamiento
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        // verifica si el permiso de la cámara está habilitado o no
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission(){
        // solicita el permiso de la cámara
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed(); //regrese haciendo clic en el botón de barra de acción
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // resultado del permiso permitido / denegado

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted){
                        // ambos permisos permitidos
                        PickFromCamera();
                    }
                    else{
                        Toast.makeText(this, "Se requieren permisos de cámara y almacenamiento", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){

                    // si se permite devolver verdadero de lo contrario falso
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted){
                        // permiso de almacenamiento permitido
                        PickFromGallery();
                    }
                    else{
                        Toast.makeText(this, "Se requiere permiso de almacenamiento", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
        }
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //image picked from camera or gallery will be received hare
        if (resultCode == RESULT_OK){
            //Image is picked
            if(requestCode == IMAGE_PICK_GALLERY_CODE){

                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);

            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //Picked from camera
                //crop Image
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);

            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                //Croped image received
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK){
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    //set Image
                    profileIv2.setImageURI(resultUri);
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    //ERROR
                    Exception error = result.getError();
                    Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
                }

            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
