package com.example.linnersui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PfRecyclerViewAdapter extends RecyclerView.Adapter<PfRecyclerViewAdapter.MyViewHolderPf> {
    Context context;
    ArrayList<ModeloParadaFavorita> modeloParadaFavoritas;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onImageClick(int position);
    }

    public PfRecyclerViewAdapter(Context context, ArrayList<ModeloParadaFavorita> modeloParadaFavoritas, OnItemClickListener listener) {
        this.context = context;
        this.modeloParadaFavoritas = modeloParadaFavoritas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PfRecyclerViewAdapter.MyViewHolderPf onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_parada_favorita, parent, false);
        return new PfRecyclerViewAdapter.MyViewHolderPf(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PfRecyclerViewAdapter.MyViewHolderPf holder, int position) {

        holder.direccion.setText(modeloParadaFavoritas.get(position).getDireccion());
        holder.localidadProvincia.setText(modeloParadaFavoritas.get(position).getLocalidadProvincia());
        holder.imgAccion.setOnClickListener(v -> listener.onImageClick(position));
    }

    @Override
    public int getItemCount() {
        return modeloParadaFavoritas.size();
    }

    public static class MyViewHolderPf extends RecyclerView.ViewHolder {
        TextView direccion, localidadProvincia;
        ImageView imgAccion;

        public MyViewHolderPf(@NonNull View itemView) {
            super(itemView);

            direccion = itemView.findViewById(R.id.pfDireccion);
            localidadProvincia = itemView.findViewById(R.id.pfLocaliProvincia);
            imgAccion = itemView.findViewById(R.id.eliminarPf);
        }
    }
}
