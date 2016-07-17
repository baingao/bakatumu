package com.bakatumu.bakatumu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bakatumu.bakatumu.R;
import com.bakatumu.bakatumu.model.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Order> orderArrayList;
    private static String today;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView id, idAset, alamat;
        public TextView pesan, antar, terima, jumlah, harga;


        public ViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.value_id);
            idAset = (TextView) view.findViewById(R.id.value_idAset);
            alamat = (TextView) view.findViewById(R.id.value_alamat);
            pesan = (TextView) view.findViewById(R.id.value_pesan);
            antar = (TextView) view.findViewById(R.id.value_antar);
            terima = (TextView) view.findViewById(R.id.value_terima);
            jumlah = (TextView) view.findViewById(R.id.value_jumlah);
            harga = (TextView) view.findViewById(R.id.value_harga);
        }
    }


    public OrdersAdapter(Context mContext, ArrayList<Order> orderArrayList) {
        this.mContext = mContext;
        this.orderArrayList = orderArrayList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    // ini yang menghubungkan dengan orders_list_row.xml
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orders_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = orderArrayList.get(position);

        holder.id.setText(order.getId());
        holder.idAset.setText(order.getIdAset());
        holder.alamat.setText(order.getAlamat());
        holder.pesan.setText(order.getPesan());
        holder.antar.setText(order.getAntar());
        holder.terima.setText(order.getTerima());
        holder.jumlah.setText(order.getJumlah());
        holder.harga.setText(order.getHarga());
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private OrdersAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final OrdersAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
