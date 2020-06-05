package com.tencent.qcloud.logutils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class LogActivity extends AppCompatActivity {

    private static String TAG = LogActivity.class.getSimpleName();

    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private ListView listView;
    private static CustomerAdapter customerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // requestWindowFeature(Window.FEATURE_NO_TITLE); 对 appCompatActivity失效
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        findViewById(R.id.backId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = findViewById(R.id.item_list);
        customerAdapter = new CustomerAdapter();
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = getIntent().getExtras();
                if(bundle != null){
                    String parentPath = bundle.getString(LogServer.FILE_PARENT_PATH_KEY, null);
                    List<String> fileNameList = bundle.getStringArrayList(LogServer.FILE_NAME_KEY);
                    initItems(parentPath, fileNameList);
                }
            }
        }, 30);
        listView.setAdapter(customerAdapter);
    }


        private void initItems(String parentPath, List<String> fileNameList){
        if(fileNameList != null && parentPath != null){
            customerAdapter.fileParentPath = parentPath;
            customerAdapter.items.addAll(fileNameList);
            customerAdapter.notifyDataSetChanged();
        }
    }

    private class CustomerAdapter extends BaseAdapter{

        private List<String> items = new ArrayList<>(20);
        private String fileParentPath;

        public CustomerAdapter(){
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(LogActivity.this).inflate(R.layout.item_log, null, false);
                viewHolder.filePathTextView = convertView.findViewById(R.id.pathId);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.filePathTextView.setText(items.get(position));
            viewHolder.setViewOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //弹出share dialog
                    TextView textView = (TextView) v;
                    String filePath = fileParentPath + File.separator + textView.getText().toString().trim();
//                    Log.d(TAG, filePath);
                    share(filePath);
                }
            });
//            ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,70);//设置宽度和高度
//            convertView.setLayoutParams(params);
            return convertView;
        }

        private void share(String filePath){
            //调用分享
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("*/*");
            fileIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath));
            LogActivity.this.startActivity(Intent.createChooser(fileIntent, "分享"));
        }
    }

    private static class ViewHolder{

        private TextView filePathTextView;

        public void setViewOnClick(View.OnClickListener onClickListener){
            filePathTextView.setOnClickListener(onClickListener);
        }
    }


//    private SwipeRefreshLayout swipeRefreshLayout;
//    private RecyclerView recyclerView;
//    private CustomerAdapter customerAdapter;
//    private OnScrollListener onScrollListener;
//    private Handler mainHandler = new Handler(Looper.getMainLooper());
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // requestWindowFeature(Window.FEATURE_NO_TITLE); 对 appCompatActivity失效
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_log);
//        findViewById(R.id.backId).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        swipeRefreshLayout = findViewById(R.id.swipeRefreshId);
//        swipeRefreshLayout.setEnabled(false); //屏蔽下拉动作
//        recyclerView = findViewById(R.id.item_list);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); //添加分割线
//        customerAdapter = new CustomerAdapter(this);
//        recyclerView.setAdapter(customerAdapter);
//        mainHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Bundle bundle = getIntent().getExtras();
//                if(bundle != null){
//                    String parentPath = bundle.getString(LogServer.FILE_PARENT_PATH_KEY, null);
//                    List<String> fileNameList = bundle.getStringArrayList(LogServer.FILE_NAME_KEY);
//                    initItems(parentPath, fileNameList);
//                }
//            }
//        }, 30);
//
////        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
////            @Override
////            public void onRefresh() {
////                dropDownRefresh();
////                swipeRefreshLayout.setRefreshing(false);
////            }
////        });
//
////        onScrollListener = new OnScrollListener() {
////            @Override
////            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//////            super.onScrollStateChanged(recyclerView, newState);
////                if(newState == RecyclerView.SCROLL_STATE_IDLE){
////                    //上拉刷新
////                    pullUpRefresh();
////                }
////            }
////
////            @Override
////            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
////                super.onScrolled(recyclerView, dx, dy);
////            }
////        };
////        recyclerView.addOnScrollListener(onScrollListener);
//
//    }
//
//    private void initItems(String parentPath, List<String> fileNameList){
//        if(fileNameList != null && parentPath != null){
//            customerAdapter.fileParentPath = parentPath;
//            customerAdapter.items.addAll(fileNameList);
//            customerAdapter.notifyDataSetChanged();
//        }
//    }
//
//    public void dropDownRefresh(){
//
//    }
//
//    public void pullUpRefresh(){
//        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//        int lastPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
//        if(lastPosition == customerAdapter.items.size() - 1){
////            Log.d(TAG, "last position");
//        }
//    }
//
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(recyclerView != null && onScrollListener != null){
//            recyclerView.removeOnScrollListener(onScrollListener);
//        }
//        if(customerAdapter != null){
//            customerAdapter.clear();
//        }
//    }
//
//    private static class CustomerAdapter extends RecyclerView.Adapter<CustomerViewHolder>{
//        private List<String> items = new ArrayList<>(30);
//        private String fileParentPath;
//        private Context context;
//
//        public CustomerAdapter(Context context){
//            this.context = context;
//        }
//
//        private void share(String filePath){
//            //调用分享
//            Intent fileIntent = new Intent(Intent.ACTION_SEND);
//            fileIntent.setType("*/*");
//            fileIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath));
//            context.startActivity(Intent.createChooser(fileIntent, "分享"));
//        }
//
//        public void clear(){
//            items.clear();
//        }
//
//
//        @NonNull
//        @Override
//        public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            //创建 viewHolder
//            View view = LayoutInflater.from(context).inflate(R.layout.item_log, parent, false);
//            final CustomerViewHolder customerViewHolder = new CustomerViewHolder(view);
//            customerViewHolder.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //弹出share dialog
//                    String filePath = fileParentPath + File.separator + customerViewHolder.filePathTextView.getText().toString().trim();
//                    Log.d(TAG, filePath);
//                    share(filePath);
//                }
//            });
//            return customerViewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
//            //绑定数据
//            holder.filePathTextView.setText(items.get(position));
//        }
//
//        @Override
//        public int getItemCount() {
//            return items.size();
//        }
//    }
//
//    private static class CustomerViewHolder extends RecyclerView.ViewHolder{
//
//        private TextView filePathTextView;
//        private ImageView actionImageView;
//
//        public CustomerViewHolder(View itemView) {
//            super(itemView);
//            filePathTextView = itemView.findViewById(R.id.pathId);
//            actionImageView = itemView.findViewById(R.id.actionId);
//        }
//
//        public void setOnClickListener(View.OnClickListener onClickListener){
////            actionImageView.setOnClickListener(onClickListener);
//            filePathTextView.setOnClickListener(onClickListener);
//        }
//    }
}
