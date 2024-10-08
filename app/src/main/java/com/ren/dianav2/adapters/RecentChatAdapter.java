package com.ren.dianav2.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ren.dianav2.R;
import com.ren.dianav2.assistants.models.Conversation;
import com.ren.dianav2.listener.IChatClickListener;
import com.ren.dianav2.models.ChatItem;

import java.util.List;

/**
 * Adaptador para mostrar una lista de chats recientes en un RecyclerView.
 */
public class RecentChatAdapter extends RecyclerView.Adapter<RecentChatViewHolder> {

    private Context context;
    private List<Conversation> items;
    private IChatClickListener listener;

    /**
     * Constructor para RecentChatAdapter.
     *
     * @param context el contexto en el cual el adaptador está operando
     * @param items la lista de elementos de chat a mostrar
     */
    public RecentChatAdapter(Context context, List<Conversation> items, IChatClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    /**
     * Llamado cuando RecyclerView necesita un nuevo ViewHolder del tipo dado para representar un elemento.
     *
     * @param parent el ViewGroup en el que la nueva Vista será añadida después de ser enlazada a
     *               una posición del adaptador
     * @param viewType el tipo de vista del nuevo View
     * @return un nuevo RecentChatViewHolder que contiene una Vista del tipo dado
     */
    @NonNull
    @Override
    public RecentChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecentChatViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chats, parent, false));
    }

    /**
     * Llamado por RecyclerView para mostrar los datos en la posición especificada.
     *
     * @param holder el ViewHolder que debe ser actualizado para representar los contenidos del
     *               elemento en la posición dada en el conjunto de datos
     * @param position la posición del elemento dentro del conjunto de datos del adaptador
     */
    @Override
    public void onBindViewHolder(@NonNull RecentChatViewHolder holder, int position) {
        //holder.getIvChat().setImageResource(items.get(position).getIvIcon());
        holder.getTvTitle().setText(items.get(position).getTitle());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecentChatClicked(items.get(position).getId());
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            deleteChatPosition(items.get(position).getId());
            return true;
        });

    }

    /**
     * Retorna el número total de elementos en el conjunto de datos que posee el adaptador.
     *
     * @return el número total de elementos en este adaptador
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    private void deleteChatPosition(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Chat");
        builder.setMessage("Are you sure you want to delete the chat?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            if (listener != null) {
                listener.onDeleteChatClicked(id);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}

/**
 * ViewHolder para mostrar un chat reciente en el RecentChatAdapter.
 */
class RecentChatViewHolder extends RecyclerView.ViewHolder {
    private ImageView ivChat;
    private TextView tvTitle;

    /**
     * Constructor para RecentChatViewHolder.
     *
     * @param itemView la vista del elemento
     */
    public RecentChatViewHolder(@NonNull View itemView) {
        super(itemView);
        ivChat = itemView.findViewById(R.id.iv_chat);
        tvTitle = itemView.findViewById(R.id.tv_title);
    }

    /**
     * Obtiene el ImageView del chat.
     *
     * @return el ImageView del chat
     */
    public ImageView getIvChat() {
        return ivChat;
    }

    /**
     * Establece el ImageView del chat.
     *
     * @param ivChat el nuevo ImageView del chat
     */
    public void setIvChat(ImageView ivChat) {
        this.ivChat = ivChat;
    }

    /**
     * Obtiene el TextView del título.
     *
     * @return el TextView del título
     */
    public TextView getTvTitle() {
        return tvTitle;
    }

    /**
     * Establece el TextView del título.
     *
     * @param tvTitle el nuevo TextView del título
     */
    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }
}
