package rongim.gdut.com.rongim;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Zhiqiang on 2015/11/3.
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberHolder> {

    private Activity app;
    private List<Member> members;
    public OnItemClickListener Listener;

    public MemberAdapter(Activity app,List<Member> members) {
        this.app = app;
        this.members = members;
    }

    @Override
    public MemberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MemberHolder(LayoutInflater.from(app).inflate(R.layout.im_member_item,null));
    }

    @Override
    public void onBindViewHolder(final MemberHolder holder, final int position) {
        holder.name.setText(members.get(position).name);
        holder.icon.setImageDrawable(members.get(position).icon);
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Listener != null)Listener.OnItemClick(holder.getAdapterPosition(),holder.itemView,members.get(position));
            }
        });
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        Listener = listener;
    }
    @Override
    public int getItemCount() {
        return members.size();
    }

    public void AddMember(Member member){
        int pos = members.size()-3;
        members.add(pos,member);
        this.notifyItemInserted(pos+1);
    }

    public void deletedMember(int position){
        members.remove(position);
        this.notifyItemRemoved(position-1);
    }

    public interface OnItemClickListener{
        public void OnItemClick(int adapterPosition, View itemView, Member member);
    }
    public class MemberHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView icon;

        public MemberHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.im_member_name);
            icon = (ImageView) itemView.findViewById(R.id.im_member_icon);

        }
    }
}
