package com.find.my.doctorapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AdapterDoctorList extends BaseAdapter {


    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    private Context context;

    LayoutInflater inflater;

    private String fragment;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    private String str_currency = "";

    public AdapterDoctorList(Context context, ArrayList<HashMap<String, String>> hashMapArrayList, String fragment) {
        this.hashMapArrayList = hashMapArrayList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return hashMapArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return hashMapArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return hashMapArrayList.size();
    }

    @Override
    public View getView(final int posistion, View convertView, ViewGroup viewGroup) {

        MyViewHolder mViewHolder;
//
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.activity_doctor_list, viewGroup, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        mViewHolder.txt_rate.setText(hashMapArrayList.get(posistion).get("first_name"));
        mViewHolder.txt_place.setText(hashMapArrayList.get(posistion).get("specialist"));
        mViewHolder.txt_rated_by.setText(hashMapArrayList.get(posistion).get("hospital"));
        mViewHolder.txt_dated.setText(hashMapArrayList.get(posistion).get("mobile"));
//        mViewHolder.btn_appointment.setText(hashMapArrayList.get(posistion).get("appointment"));
//        mViewHolder.btn_availablity.setText(hashMapArrayList.get(posistion).get("availability"));
        mViewHolder.btn_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Appointment.class);
                intent.putExtra("patient_id", (((Activity) context).getIntent().getStringExtra("patient_id")));
                intent.putExtra("patient_name", (((Activity) context).getIntent().getStringExtra("patient_name")));
//                intent.putExtra("patient_contact", (((Activity) context).getIntent().getStringExtra("patient_contact")));

                intent.putExtra("sid", hashMapArrayList.get(posistion).get("sid"));
                intent.putExtra("hospital", hashMapArrayList.get(posistion).get("hospital"));
                intent.putExtra("doctor_name", hashMapArrayList.get(posistion).get("doctor_name"));
                intent.putExtra("doctor_contact", hashMapArrayList.get(posistion).get("doctor_contact"));

                if (hashMapArrayList.get(posistion).get("in_out").equals("1"))
                context.startActivity(intent);
                else
                    Toast.makeText(context,"Doctor Out",Toast.LENGTH_SHORT).show();
            }
        });
        mViewHolder.btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + hashMapArrayList.get(posistion).get("landline")));
                context.startActivity(intent);
            }
        });

        if (hashMapArrayList.get(posistion).get("in_out").equals("1")){
            mViewHolder.btn_availablity.setText("IN");
        }
        else {
            mViewHolder.btn_availablity.setText("OUT");

        }

//        }
        return convertView;
    }

    private class MyViewHolder {
        private final TextView txt_rate;
        private final TextView txt_place;
        private final TextView txt_rated_by;
        private final TextView txt_dated;
        private Button btn_appointment, btn_availablity,btn_call;

        public MyViewHolder(View item) {
            txt_rate = (TextView) item.findViewById(R.id.txt_doctor_name);
            txt_place = (TextView) item.findViewById(R.id.txt_doctor_dep);
            txt_rated_by = (TextView) item.findViewById(R.id.txt_hospital);
            txt_dated = (TextView) item.findViewById(R.id.txt_contact);
            btn_appointment = (Button) item.findViewById(R.id.btn_appointment);
            btn_availablity = (Button) item.findViewById(R.id.btn_availablity);
            btn_call=(Button)item.findViewById(R.id.btn_call);


        }
    }

//    private void volley_post(final String conf, final String details, final String company_name,
//                             final String held_on, final String number, final String title, final String posted_by,
//                             final Dialog dialog) {
//
//        RequestQueue queue = Volley.newRequestQueue(context);
//
//        final ProgressDialog pDialog = new ProgressDialog(context);
//        pDialog.setMessage("Loading...");
//        pDialog.show();
//        pDialog.setCancelable(false);
//
//        StringRequest sr = new StringRequest(Request.Method.POST, "http://kpfba.info/ack.php", new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
////                mPostCommentResponse.requestCompleted();
//
//                System.out.println("pan_response" + response);
//
//                pDialog.dismiss();
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.getString("success").equals("1")) {
//                        Toast.makeText(context.getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
////                mPostCommentResponse.requestEndedWithError(error);
//                error.printStackTrace();
//                pDialog.dismiss();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("confirmation", conf);
//                params.put("contact", details);
//                params.put("company_name", company_name);
//                params.put("held_on", held_on.substring(0, 10));
//                params.put("number", number);
//                params.put("title", title);
//                params.put("posted_by", posted_by);
//
//
//                System.out.println("[pan_params" + params);
//
//
//                return params;
//            }
//
////            @Override
////            public Map<String, String> getHeaders() throws AuthFailureError {
////                Map<String, String> params = new HashMap<String, String>();
////                params.put("Content-Type", "application/x-www-form-urlencoded");
////                return params;
////            }
//        };
//        queue.add(sr);
//
//        sr.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
////        int socketTimeout = 30000;//30 seconds - change to what you want
////        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
////        sr.setRetryPolicy(policy);
////        queue.add(sr);
//
//
//    }
}
