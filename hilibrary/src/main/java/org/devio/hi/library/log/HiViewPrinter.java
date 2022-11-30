package org.devio.hi.library.log;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.devio.hi.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author yanyonghua
 * @Date 2022/10/14-12:24
 * @Des $.将log显示在界面上
 */
public class HiViewPrinter implements HiLogPrinter{
    private RecyclerView recyclerView;
    private LogAdapter adapter;
    private HiViewPrinterProvider viewProvider;
    public HiViewPrinter(Activity activity){
      FrameLayout rootView = activity.findViewById(android.R.id.content);
      this.recyclerView =new RecyclerView(activity);
      adapter = new LogAdapter(LayoutInflater.from(recyclerView.getContext()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        viewProvider = new HiViewPrinterProvider(rootView,recyclerView);
    }

    /**
     * 获取viewProvider,通过viewProvider可以控制log视图的展示与隐藏
     * @return viewProvider
     */
    @NonNull
    public HiViewPrinterProvider getViewProvider() {
        return viewProvider;
    }

    @Override
    public void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String printString) {
        //将log展示添加到recyclerView
        adapter.addItem(new HiLogMo(System.currentTimeMillis(),level,tag,printString));
        //滚动到对应的位置
        recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
    }
    private static class LogAdapter extends RecyclerView.Adapter<LogViewHolder>{
        private LayoutInflater inflater;
        private List<HiLogMo> logs= new ArrayList<>();
        public LogAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }
       void addItem(HiLogMo logItem){
            logs.add(logItem);
            notifyItemChanged(logs.size() -1);
        }

        @NonNull
        @Override
        public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.hilog_item, parent, false);
            return new LogViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
            HiLogMo logItem = logs.get(position);
            int color =getHighlightColor(logItem.level);
            holder.tagView.setTextColor(color);
            holder.massageView.setTextColor(color);

            holder.tagView.setText(logItem.getFlattened());
            holder.massageView.setText(logItem.log);

        }
        private int getHighlightColor(int level){
            int highlight;
            switch (level){
                case HiLogType.V:
                    highlight = 0xffbbbbbb;
                    break;
                case HiLogType.D:
                    highlight = 0xffffffff;
                    break;
                case HiLogType.I:
                    highlight = 0xff6a8759;
                    break;
                case HiLogType.W:
                    highlight = 0xffbbbbbb;
                    break;
                case HiLogType.E:
                    highlight = 0xffbbbbbb;
                    break;
                default:
                    highlight = 0xffffff00;
                    break;
            }
            return highlight;
        }
        @Override
        public int getItemCount() {
            return logs.size();
        }
    }
    private static class LogViewHolder extends RecyclerView.ViewHolder{
        TextView tagView;
        TextView massageView;
        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            tagView = itemView.findViewById(R.id.tag);
            massageView = itemView.findViewById(R.id.massage);
        }
    }
}
