package com.example.pm01e3p;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pm01e3p.DetalleRegistroActivity;
import com.example.pm01e3p.ModelRecord;

import java.util.ArrayList;

public class AdapterRecord extends RecyclerView.Adapter<AdapterRecord.HolderRecord>{

    //Variables
    private Context context;
    private ArrayList<ModelRecord> recordsList;
    //Constructor
    public AdapterRecord(Context context, ArrayList<ModelRecord> recordsList){
        this.context = context;
        this.recordsList = recordsList;
    }

    @NonNull
    @Override
    public HolderRecord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_record, parent, false);

        return new HolderRecord(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecord holder, int position) {

        // obtener datos, establecer datos, ver clics en el método

        //Obtener datos
        ModelRecord model = recordsList.get(position);
        final String id = model.getId();
        String name = model.getName();
        String image = model.getImage();
        String bio = model.getBio();
        String phone = model.getPhone();
        String email = model.getEmail();
        String dob = model.getDob();
        String addedTime = model.getAddedTime();
        String updatedTime = model.getUpdatedTime();

        //Establecer Datos
        holder.nameTv.setText(name);
        holder.phoneTv.setText(phone);
        holder.emailTv.setText(email);
        holder.dobTv.setText(dob);

        // si el usuario no adjunta la imagen, imageUri será nulo, por lo tanto,
        // configure una imagen predeterminada en ese caso
        if (image.equals("null")){
            // no hay imagen en el registro, establecer predeterminado
            holder.profileIv.setImageResource(R.drawable.ic_person_black);
        }
        else {
            // tener imagen en el registro
            holder.profileIv.setImageURI(Uri.parse(image));
        }


        // manejar clicks de elementos (ir a la actividad de registro de detalles)

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pass record id to next activity to show details of thet record
                Intent intent = new Intent(context, DetalleRegistroActivity.class);
                intent.putExtra("RECORD_ID", id);
                context.startActivity(intent);
            }
        });


        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return recordsList.size();// devuelve el tamaño de la lista / número o registros
    }

    class HolderRecord extends RecyclerView.ViewHolder{
        //vistas
        ImageView profileIv;
        TextView nameTv, phoneTv, emailTv, dobTv;
        ImageButton moreBtn;
        public HolderRecord(@NonNull View itemView){
            super(itemView);

            //Inicializamos la vistas
            profileIv = itemView.findViewById(R.id.profileIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            phoneTv = itemView.findViewById(R.id.phoneTv);
            emailTv = itemView.findViewById(R.id.emailTv);
            dobTv = itemView.findViewById(R.id.dobTv);
            moreBtn = itemView.findViewById(R.id.moreBtn);

        }
    }
}
