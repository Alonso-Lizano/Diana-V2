package com.ren.dianav2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ren.dianav2.R;
import com.ren.dianav2.models.ChatItem;

import java.util.List;

/**
 * Adaptador para mostrar una lista de chats guardados en un RecyclerView.
 */
public class SavedChatAdapter extends RecyclerView.Adapter<SavedChatViewHolder> {

    private Context context;
    private List<ChatItem> chatItems;

    /**
     * Constructor para SavedChatAdapter.
     *
     * @param context el contexto en el cual el adaptador está operando
     * @param chatItems la lista de elementos de chat guardados a mostrar
     */
    public SavedChatAdapter(Context context, List<ChatItem> chatItems) {
        this.context = context;
        this.chatItems = chatItems;
    }

    /**
     * Llamado cuando RecyclerView necesita un nuevo ViewHolder del tipo dado para representar un elemento.
     *
     * @param parent el ViewGroup en el que la nueva Vista será añadida después de ser enlazada a
     *               una posición del adaptador
     * @param viewType el tipo de vista del nuevo View
     * @return un nuevo SavedChatViewHolder que contiene una Vista del tipo dado
     */
    @NonNull
    @Override
    public SavedChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SavedChatViewHolder(LayoutInflater.from(context).inflate(R.layout.item_saved_chat,
                parent, false));
    }

    /**
     * Llamado por RecyclerView para mostrar los datos en la posición especificada.
     *
     * @param holder el ViewHolder que debe ser actualizado para representar los contenidos del
     *               elemento en la posición dada en el conjunto de datos
     * @param position la posición del elemento dentro del conjunto de datos del adaptador
     */
    @Override
    public void onBindViewHolder(@NonNull SavedChatViewHolder holder, int position) {
        holder.getTvTitle().setText(chatItems.get(position).getTitle());
        holder.getIvChat().setImageResource(chatItems.get(position).getIvIcon());
    }

    /**
     * Retorna el número total de elementos en el conjunto de datos que posee el adaptador.
     *
     * @return el número total de elementos en este adaptador
     */
    @Override
    public int getItemCount() {
        return chatItems.size();
    }
}

/**
 * ViewHolder para mostrar un chat guardado en el SavedChatAdapter.
 */
class SavedChatViewHolder extends RecyclerView.ViewHolder {

    private ImageView ivChat;
    private TextView tvTitle;

    /**
     * Constructor para SavedChatViewHolder.
     *
     * @param itemView la vista del elemento
     */
    public SavedChatViewHolder(@NonNull View itemView) {
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
