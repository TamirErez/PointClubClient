package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import pointclub.shared.model.User;
import pointclub.shared.model.chat.Message;
import pointclub.chat.R;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private enum messageType {
        sender(0), receiver(1);

        private final int value;

        messageType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private final List<Message> mMessageList;

    public MessageListAdapter(List<Message> messageList) {
        mMessageList = messageList;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mMessageList.get(position);
        if (message.getSenderId() == User.getCurrentUser().getServerId()) {
            return messageType.sender.getValue();
        } else {
            return messageType.receiver.getValue();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == messageType.sender.getValue()) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sender_message, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == messageType.receiver.getValue()) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recieve_message, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = mMessageList.get(position);

        if (holder.getItemViewType() == messageType.sender.getValue()) {
            ((SentMessageHolder) holder).bind(message);
        } else if (holder.getItemViewType() == messageType.receiver.getValue()) {
            ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_gchat_message_other);
            timeText = itemView.findViewById(R.id.text_gchat_timestamp_other);
            nameText = itemView.findViewById(R.id.text_gchat_user_other);
        }

        void bind(Message message) {
            messageText.setText(message.getContent());

            timeText.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH).format(message.getSendTime()));
            nameText.setText(message.getSender().getName());
        }
    }

    private static class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_gchat_message_me);
            timeText = itemView.findViewById(R.id.text_gchat_timestamp_me);
        }

        void bind(Message message) {
            messageText.setText(message.getContent());
            messageText.post(() ->
                    itemView.findViewById(R.id.text_gchat_timestamp_me).setTextDirection(
                            messageText.getLineCount() > 1 ? View.TEXT_DIRECTION_RTL : View.TEXT_DIRECTION_LTR
                    )
            );
            timeText.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH).format(message.getSendTime()));
        }
    }
}