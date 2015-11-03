package rongim.gdut.com.rongim;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConversionSettingsFragment extends Fragment implements MemberAdapter.OnItemClickListener{

    private RecyclerView membersView;
    private List<Member> Data;
    private MemberAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Data = new ArrayList<Member>();
        Data.add(new Member(getResources().getDrawable(R.drawable.member_icon),"li lei",0));
        Data.add(new Member(getResources().getDrawable(R.drawable.member_icon),"tomy",0));
        Data.add(new Member(getResources().getDrawable(R.drawable.member_icon),"lily",0));
        Data.add(new Member(getResources().getDrawable(R.drawable.member_icon),"ashely",0));
        Data.add(new Member(getResources().getDrawable(R.drawable.member_icon),"han meimei",0));
        Data.add(new Member(getResources().getDrawable(R.drawable.add_member),"",1));
        Data.add(new Member(getResources().getDrawable(R.drawable.delet_member),"",2));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversion_settings, container, false);
        membersView = (RecyclerView) view.findViewById(R.id.im_member_list);
        membersView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        adapter = new MemberAdapter(getActivity(),Data);
        adapter.setOnItemClickListener(this);
        membersView.setAdapter(adapter);
        membersView.setItemAnimator(new DefaultItemAnimator());//配置动画
        return view;
    }

    @Override
    public void OnItemClick(int adapterPosition, View itemView, Member member) {
        Log.d("zhiqiang","pos:"+adapterPosition+"  name:"+member.name+"  type:"+member.type);
        if(member.type == 1){
            adapter.AddMember(new Member(getResources().getDrawable(R.drawable.member_icon),"new meimei",0));
        }else if(member.type == 2){
            adapter.deletedMember(1);

        }
    }
}
