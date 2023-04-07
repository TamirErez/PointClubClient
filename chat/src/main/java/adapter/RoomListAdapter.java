package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import activity.ChatActivity;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import pointclub.shared.model.chat.Room;
import pointclub.chat.R;

public class RoomListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Room> roomList;
    Context context;

    public RoomListAdapter(List<Room> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_preview, parent, false);
        return new RoomListAdapter.RoomHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((RoomHolder) holder).bind(roomList.get(position));
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        View newRoomView = holder.itemView.findViewById(R.id.room_preview_card);
        newRoomView.setOnClickListener(v -> {
            Room room = getRoomFromView((CardView) v);
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("room", room);
            context.startActivity(intent);
        });
    }

    private Room getRoomFromView(CardView roomView) {
        String roomName = ((TextView) roomView.findViewById(R.id.room_name_preview)).getText().toString();
        return Room.find(Room.class, "name=?",roomName).listIterator().next();
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    private static class RoomHolder extends RecyclerView.ViewHolder {

        public RoomHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Room room) {
            ((TextView) itemView.findViewById(R.id.room_name_preview)).setText(room.getName());
        }
    }
}