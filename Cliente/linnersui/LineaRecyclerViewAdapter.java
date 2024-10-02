package com.example.linnersui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LineaRecyclerViewAdapter extends RecyclerView.Adapter<LineaRecyclerViewAdapter.MyViewHolder> {
    ArrayList<ModeloLinea> modeloLinea;
    public LineaRecyclerViewAdapter(ArrayList<ModeloLinea> modeloLinea) {
        this.modeloLinea = modeloLinea;
    }

    @NonNull
    @Override
    public LineaRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_linea, parent, false);
        return new LineaRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LineaRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.origen.setText(modeloLinea.get(position).getOrigen());
        holder.destino.setText(modeloLinea.get(position).getDestino());
        holder.precio.setText(modeloLinea.get(position).getPrecio());
        holder.LogoLinea.setImageResource(modeloLinea.get(position).getLogoLinea());
        holder.logoEmpresa.setImageResource(modeloLinea.get(position).getLogoEmpresa());
    }

    @Override
    public int getItemCount() {
        return modeloLinea.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView logoEmpresa, LogoLinea;
        TextView origen, destino, precio;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            logoEmpresa = itemView.findViewById(R.id.logoEmpresa);
            LogoLinea = itemView.findViewById(R.id.logoLinea);
            origen = itemView.findViewById(R.id.tvOrigen);
            destino = itemView.findViewById(R.id.tvDestino);
            precio = itemView.findViewById(R.id.tvPrecio);
        }
    }
}
