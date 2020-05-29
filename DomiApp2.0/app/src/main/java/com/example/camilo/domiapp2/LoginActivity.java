package com.example.camilo.domiapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilo.domiapp2.Model.Usuarios;
import com.example.camilo.domiapp2.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity
{

    private EditText InputPhoneNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminLink;

    private String parentDbName = "Usuarios";
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = (Button) findViewById(R.id.login_btn);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);
        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        loadingBar = new ProgressDialog(this);

        chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginButton.setText("Inicio de sesión de administrador");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Administradores";

            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Inicio de sesión");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Usuarios";
            }
        });
    }

    private void LoginUser()
    {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
             Toast.makeText(this,"Por favor escribe tu numero", Toast.LENGTH_SHORT).show();
        }
         else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Por favor escribe tu contraseña", Toast.LENGTH_SHORT).show();
         }

         else
        {
            loadingBar.setTitle("Cuenta de inicio de sesión");
            loadingBar.setMessage("Por favor espera, mientras estamos comprobando los credenciales.  ");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


                    AllowAccessToAccount(phone, password);
        }

    }

    private void AllowAccessToAccount(final String phone, final String password)
    {
        if (chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot)
            {
               if(dataSnapshot.child(parentDbName).child(phone).exists())
               {
                   Usuarios datosUsuarios = dataSnapshot.child(parentDbName).child(phone).getValue(Usuarios.class);

                   if (datosUsuarios.getTelefono().equals(phone))
                   {
                       if (datosUsuarios.getContraseña().equals(password))
                       {
                           if(parentDbName.equals("Administradores"))
                           {
                               Toast.makeText(LoginActivity.this, "Bienvenido administrador, has iniciado sesión exitosamente..", Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                               startActivity(intent);
                           }
                           else if (parentDbName.equals("Usuarios"))
                             {
                                 Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso..", Toast.LENGTH_SHORT).show();
                                 loadingBar.dismiss();

                                 Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                 Prevalent.currentOnlineUser = datosUsuarios;
                                 startActivity(intent);

                             }
                       }
                       else
                       {
                           loadingBar.dismiss();
                           Toast.makeText(LoginActivity.this, "Contraseña es incorrecto", Toast.LENGTH_SHORT).show();
                       }
                   }

               }
               else
               {
                   Toast.makeText(LoginActivity.this, "Esta Cuenta ya existe " +phone+" el numero no existe ", Toast.LENGTH_SHORT).show();
                   loadingBar.dismiss();

               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
