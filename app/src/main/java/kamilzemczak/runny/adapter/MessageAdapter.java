package kamilzemczak.runny.adapter;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import kamilzemczak.runny.R;
import kamilzemczak.runny.model.Message;

public class MessageAdapter extends BaseAdapter {

    private final List<Message> chatMessages;
    private Activity context;

    public MessageAdapter(Activity context, List<Message> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @Override
    public int getCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public Message getItem(int position) {
        if (chatMessages != null) {
            return chatMessages.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Message chatMessage = getItem(position);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = vi.inflate(R.layout.form_message, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        boolean myMsg = chatMessage.isMe() ;
        setAlignment(holder, myMsg);
        holder.textMessage.setText(chatMessage.getContents());
        holder.textInfo.setText(chatMessage.getDateTime());
        return convertView;
    }

    public void add(Message message) {
        chatMessages.add(message);
    }

    private void setAlignment(ViewHolder holder, boolean isMe) {
        if (!isMe) {
            holder.contentWithBG.setBackgroundResource(R.drawable.in_message_bg);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.textMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.textMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.textInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.textInfo.setLayoutParams(layoutParams);
        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.out_message_bg);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.textMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.textMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.textInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.textInfo.setLayoutParams(layoutParams);
        }
    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.textMessage = (TextView) v.findViewById(R.id.messageLayout_textMessage);
        holder.content = (LinearLayout) v.findViewById(R.id.messageLayout_content);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.messageLayout_contentWithBackground);
        holder.textInfo = (TextView) v.findViewById(R.id.messageLayout_textInfo);
        return holder;
    }

    private static class ViewHolder {
        public TextView textMessage;
        public TextView textInfo;
        public LinearLayout content;
        public LinearLayout contentWithBG;
    }
}
