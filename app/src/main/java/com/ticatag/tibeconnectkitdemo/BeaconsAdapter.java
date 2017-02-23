package com.ticatag.tibeconnectkitdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ticatag.tibeconnectkit.Beacon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fvisticot on 27/02/15.
 */
public class BeaconsAdapter extends RecyclerView.Adapter<BeaconsAdapter.BeaconHolder> {
    private static final String TAG = "BeaconsAdapter";
    private Context _context = null;
    private List<Beacon> mBeacons;

    public BeaconsAdapter(ArrayList<Beacon> beacons) {
        mBeacons=beacons;
    }

    @Override
    public BeaconHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_row, parent, false);
        return new BeaconHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(BeaconHolder holder, int position) {
        Beacon beacon = mBeacons.get(position);
        holder.bindBeacon(beacon);
    }

    @Override
    public int getItemCount() {
        return mBeacons.size();
    }

    public static class BeaconHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mItemImage;
        private TextView mDescriptionTextField;
        private TextView mDetailedTextField;
        private Beacon mBeacon;

        private static final String BEACON_KEY = "BEACON";

        public BeaconHolder(View v) {
            super(v);

            mItemImage = (ImageView) v.findViewById(R.id.icon);
            mDescriptionTextField = (TextView) v.findViewById(R.id.firstLine);
            mDetailedTextField = (TextView) v.findViewById(R.id.secondLine);
            v.setOnClickListener(this);
        }

        public void bindBeacon(Beacon beacon) {
            mBeacon = beacon;
            mDescriptionTextField.setText(mBeacon.getMajor() + " " + mBeacon.getMinor() + " (" + mBeacon.getAddress() + ")");
            mDetailedTextField.setText(mBeacon.getRSSI() + "dB");
        }

        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();
            Intent intent = new Intent(context, BeaconActivity.class);
            intent.putExtra(BEACON_KEY, mBeacon);
            context.startActivity(intent);
            //((Activity) context).startActivityForResult(intent, 1);
        }
    }


    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MyAdapter", "onActivityResult");
    }

}



