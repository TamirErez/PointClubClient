package pointclub.pointclubclient.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pointclub.pointclubclient.model.Room;

public class RoomListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Room> roomList;

    public RoomListAdapter(List<Room> roomList) {
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoomListAdapter.RoomHolder(new TextView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((RoomHolder) holder).bind(roomList.get(position));
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
            ((TextView) itemView).setText(room.getName());
            ((TextView) itemView).setTextSize(20);
        }
    }
}