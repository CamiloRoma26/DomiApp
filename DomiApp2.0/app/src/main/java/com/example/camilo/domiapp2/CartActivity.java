package com.example.camilo.domiapp2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camilo.domiapp2.Model.Carrito;
import com.example.camilo.domiapp2.Prevalent.Prevalent;
import com.example.camilo.domiapp2.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartActivity extends AppCompatActivity
{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessBtn = (Button) findViewById(R.id.next_btn);
        txtTotalAmount = (TextView) findViewById(R.id.total_price);

    }

    @Override
    protected void onStart()
    {
        super.onStart();


        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Lista del carrito");

        FirebaseRecyclerOptions<Carrito> options =
                new FirebaseRecyclerOptions.Builder<Carrito>()
                        .setQuery(cartListRef.child("Vista de Usuario")
                        .child(Prevalent.currentOnlineUser.getTelefono())
                          .child("Productos"), Carrito.class)
                        .build();

        FirebaseRecyclerAdapter<Carrito, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Carrito, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Carrito model)
            {

                holder.txtProductQuantity.setText("Cantidad = " + model.getCantidad());
                holder.txtProductPrice.setText("Precio = " + model.getPrecio());
                holder.txtProductName.setText(model.getNombreProducto());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        CharSequence options [] = new CharSequence[]
                            {
                                    "Editar",
                                    "Remover"

                            };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Opciones Carrito: ");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if (i == 0)
                                {
                                    Intent intent = new Intent (CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                if (i == 1)
                                {
                                    cartListRef.child("Vista de Usuario")
                                            .child(Prevalent.currentOnlineUser.getTelefono())
                                            .child("Productos")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(CartActivity.this, "Articulo Removido exitosamente", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent (CartActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });


                                }

                            }
                        });
                        builder.show();


                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
