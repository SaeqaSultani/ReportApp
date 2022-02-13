package com.example.reportapp.network_getreport;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reportapp.R;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.aakira.expandablelayout.Utils;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<ClassGetReport> getReport;
    private Context context;
    private SparseBooleanArray expandState = new SparseBooleanArray();



    public RecyclerViewAdapter(List<ClassGetReport> getdata, Context context) {
        this.getReport = getdata;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);

        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        holder.report.setText(getReport.get(position).getReport());
        holder.date.setText(getReport.get(position).getDate());
        holder.expandableLayout.setExpanded(expandState.get(position));
        holder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {

            @Override
            public void onPreOpen() {
                createRotateAnimator(holder.relativeLayout, 0f, 180f).start();
                expandState.put(position, true);

            }

            @Override
            public void onPreClose() {
                createRotateAnimator(holder.relativeLayout, 180f, 0f).start();
                expandState.put(position, false);
            }
        });

        holder.relativeLayout.setRotation(expandState.get(position) ? 180f : 0f);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(holder.expandableLayout);
            }
        });

//        holder.btnmor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                PopupMenu popupMenu = new PopupMenu(context,holder.btnmor);
//                //inflating menu from xml resource
//                popupMenu.inflate(R.menu.more_option_menu);
//               //adding click listener
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//
//                        switch (item.getItemId()){
//
//                            case R.id.action_delete:
//                                Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
//                                return true;
//                            case R.id.action_edit:
//                                Toast.makeText(context, "edit", Toast.LENGTH_SHORT).show();
//
//                            default:
//                                return false;
//                        }
//                    }
//                });
//                //displaying the popup
//                popupMenu.show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return getReport.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date,report;
       ImageButton btnmor;
        ExpandableLinearLayout expandableLayout;
        RelativeLayout relativeLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.item_maine_TextView_date);
            report = itemView.findViewById(R.id.item_main_TextView_report);
//            btnmor = itemView.findViewById(R.id.more);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            relativeLayout = itemView.findViewById(R.id.button);

        }
    }

    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

        public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
            animator.setDuration(300);
            animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
            return animator;

        }

    }

