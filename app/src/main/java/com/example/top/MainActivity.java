package com.example.top;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final int JIMUTOCHANGE_ACTIVITY = 1001;
    private static final int EVENT_REFRESH_ACTIVITY = 1002;
    String jimuto_room = "";
    String jimuto_name = "";
    String jimuto_id = null;
    String qr_uuid = "";

    final String[] from = {"id", "text"};
    final int[] to = {android.R.id.text2, android.R.id.text1};

    private TouchSound touchsound;
    private DatabaseHelper _helper;
    private SQLiteDatabase db;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _helper = new com.example.top.DatabaseHelper(this);
        db = _helper.getWritableDatabase();
        touchsound = new TouchSound(this);

        ImageButton image_button_touroku = findViewById(R.id.image_button_touroku);
        DoubleTourokuListener listener3 = new DoubleTourokuListener();
        image_button_touroku.setOnClickListener(listener3);

        ImageButton jimutou_change = findViewById(R.id.jimuto_change_button);
        DoubleJimutoChangeListener listener4 = new DoubleJimutoChangeListener();
        jimutou_change.setOnClickListener(listener4);

        ImageButton image_button_uketori = findViewById(R.id.image_button_uketori);
        DoubleUketoriListener listener5 = new DoubleUketoriListener();
        image_button_uketori.setOnClickListener(listener5);

        eventLogshow();
        ListView eventLogshower = findViewById(R.id.event_show);
        EventShowListener showListener = new EventShowListener();
        eventLogshower.setOnItemClickListener(showListener);
        /*????????????

        Button parcel_update_button = findViewById(R.id.parcel_update_button);
        SendRequestListener parcels_update_listener = new SendRequestListener("parcels", "update");
        parcel_update_button.setOnClickListener(parcels_update_listener);

        Button parcel_insert_button = findViewById(R.id.parcel_insert_button);
        SendRequestListener parcel_insert_listener = new SendRequestListener("parcels", "create");
        parcel_insert_button.setOnClickListener(parcel_insert_listener);

        Button parcel_debug_button = findViewById(R.id.parcel_debug_button);
        ShowRecordsListener parcels_debug_listener = new ShowRecordsListener("parcels");
        parcel_debug_button.setOnClickListener(parcels_debug_listener);

        Button ryosei_update_button = findViewById(R.id.ryosei_update_button);
        SendRequestListener ryosei_update_listener = new SendRequestListener("ryosei", "update");
        ryosei_update_button.setOnClickListener(ryosei_update_listener);

        Button ryosei_insert_button = findViewById(R.id.ryosei_insert_button);
        SendRequestListener ryosei_insert_listener = new SendRequestListener("ryosei", "create");
        ryosei_insert_button.setOnClickListener(ryosei_insert_listener);

        Button ryosei_debug_button = findViewById(R.id.ryosei_debug_button);
        ShowRecordsListener ryosei_debug_listener = new ShowRecordsListener("ryosei");
        ryosei_debug_button.setOnClickListener(ryosei_debug_listener);

        Button parcel_event_update_button = findViewById(R.id.parcel_event_update_button);
        SendRequestListener parcel_event_update_listener = new SendRequestListener("parcel_event", "update");
        parcel_event_update_button.setOnClickListener(parcel_event_update_listener);

        Button parcel_event_insert_button = findViewById(R.id.parcel_event_insert_button);
        SendRequestListener parcel_event_insert_listener = new SendRequestListener("parcel_event", "create");
        parcel_event_insert_button.setOnClickListener(parcel_event_insert_listener);

        Button parcel_event_debug_button = findViewById(R.id.parcel_event_debug_button);
        ShowRecordsListener parcel_event_debug_listener = new ShowRecordsListener("parcel_event");
        parcel_event_debug_button.setOnClickListener(parcel_event_debug_listener);
*/


        ImageButton duty_night = findViewById(R.id.duty_night_button);
        duty_night_listener listener6 = new duty_night_listener();
        duty_night.setOnClickListener(listener6);

        ImageButton qr_scanner = findViewById(R.id.qr_scanner);
        QRScanListener qr_Listener = new QRScanListener();
        qr_scanner.setOnClickListener(qr_Listener);

        ImageButton nimotsufuda = findViewById(R.id.nimotsufuda_Button);
        RefreshListener listenerNimotsufuda = new RefreshListener();
        nimotsufuda.setOnClickListener(listenerNimotsufuda);

        //??????????????????
        new HttpTask(null,"parcels","create").execute();
        new HttpTask(null,"ryosei","create").execute();
        new HttpTask(null,"parcel_event","create").execute();

        //??????????????????????????????
    }

    class buttonClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //if (view.getId() == R.id.jimuto_change_button || view.getId() == R.id.image_button_touroku || view.getId() == R.id.image_button_uketori || view.getId() == R.id.event_show || view.getId() == R.id.ryosei_insert_button || view.getId() == R.id.parcel_insert_button || view.getId() == R.id.parcel_event_insert_button || view.getId() == R.id.duty_night_button) {
            if (view.getId() == R.id.jimuto_change_button || view.getId() == R.id.image_button_touroku || view.getId() == R.id.image_button_uketori || view.getId() == R.id.event_show ||  view.getId() == R.id.duty_night_button) {
                final Button button = (Button) findViewById(view.getId());
                button.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        button.setEnabled(true);
                    }
                }, 3000L);
            }

        }
    }

    public void eventLogshow() {
        List<Map<String, String>> show_eventlist = new ArrayList<>();
        String sql = "SELECT uid, created_at, event_type, parcel_uid, room_name, ryosei_name, target_event_uid,is_deleted FROM parcel_event where is_deleted = 0 order by created_at desc limit 100";
        Cursor cursor = db.rawQuery(sql, null);
        show_eventlist.clear();
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("is_deleted")).equals("1")) {//????????????????????????????????????????????????
                continue;
            }
            if( cursor.getInt(cursor.getColumnIndex("event_type"))==3){//??????????????????????????????????????????????????????
                continue;
            }
            Map<String, String> event_raw = new HashMap<>();
            String text = "";

            text += "" + cursor.getString(cursor.getColumnIndex("created_at")).substring(5).replace('-','/');
            text += "   ";
            switch ( cursor.getInt(cursor.getColumnIndex("event_type"))) {
                case 1://????????????
                    text += "????????????       ";
                    break;
                case 2://????????????
                    text += "????????????       ";

                    break;
                case 3://??????????????????????????????????????????????????????????????????????????????
                    //text="????????????????????????????????????";
                    break;
                case 10:
                    text += "???????????????     ";

            }
            text += cursor.getString(cursor.getColumnIndex("room_name"));
            text += "    ";
            text += cursor.getString(cursor.getColumnIndex("ryosei_name"));

            String event_id = cursor.getString(cursor.getColumnIndex("uid"));
            event_raw.put("id", event_id);
            event_raw.put("text", text);
            show_eventlist.add(event_raw);

        }
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                show_eventlist,
                android.R.layout.simple_list_item_1,
                from,
                to
        );
        ListView listView = (ListView) findViewById(R.id.event_show);
        listView.setAdapter(adapter);
        ListView listListener = findViewById(R.id.event_show);
        listListener.setOnItemClickListener(new EventShowListener());
    }
    private class QRScanListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (jimuto_id == null) {
                this.showMyDialog(null,getString(R.string.main_not_selected_staff),"",getString(R.string.ok),"");
                touchsound.playsoundTwo();
            } else {
                //????????????????????????
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.initiateScan();
            }
        }
        public  void showMyDialog(View view,String title,String mainText,String positiveButton,String negativeButton) {
            DialogFragment dialogFragment = new myDialog();
            Bundle args = new Bundle();
            args.putString("positivebutton",positiveButton);
            args.putString("negativebutton",negativeButton);
            args.putString("title",title);
            args.putString("maintext",mainText);
            dialogFragment.setArguments(args);
            dialogFragment.show(getSupportFragmentManager(), "myDialog");
        }

    }

    private class DoubleTourokuListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //??????????????????
            new HttpTask(null,"parcels","create").execute();
            new HttpTask(null,"ryosei","create").execute();
            new HttpTask(null,"parcel_event","create").execute();

            //??????????????????????????????
            if (jimuto_id == null) {
                this.showMyDialog(null,getString(R.string.main_not_selected_staff),"",getString(R.string.ok),"");
                touchsound.playsoundTwo();
            } else {
                Intent intent = new Intent(MainActivity.this, Double_Buttoned_Touroku.class);
                intent.putExtra("Jimuto_id", jimuto_id);
                intent.putExtra("Jimuto_room", jimuto_room);
                //intent.putExtra("Jimuto_name", jimuto_name);
                startActivityForResult(intent, EVENT_REFRESH_ACTIVITY);
                touchsound.playsoundTwo();
            }
        }
        public  void showMyDialog(View view,String title,String mainText,String positiveButton,String negativeButton) {
            DialogFragment dialogFragment = new myDialog();
            Bundle args = new Bundle();
            args.putString("positivebutton",positiveButton);
            args.putString("negativebutton",negativeButton);
            args.putString("title",title);
            args.putString("maintext",mainText);
            dialogFragment.setArguments(args);
            dialogFragment.show(getSupportFragmentManager(), "myDialog");
        }
    }



    private class DoubleUketoriListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //??????????????????
            new HttpTask(null,"parcels","create").execute();
            new HttpTask(null,"ryosei","create").execute();
            new HttpTask(null,"parcel_event","create").execute();

            //??????????????????????????????
            if (jimuto_id == null) {
                this.showMyDialog(null,getString(R.string.main_not_selected_staff),"",getString(R.string.ok),"");
                touchsound.playsoundTwo();
            } else {
                Intent intent = new Intent(MainActivity.this, Double_Buttoned_Uketori.class);
                intent.putExtra("Jimuto_id", jimuto_id);
                intent.putExtra("Jimuto_room", jimuto_room);
                //intent.putExtra("Jimuto_name", jimuto_name);
                startActivityForResult(intent, EVENT_REFRESH_ACTIVITY);
                touchsound.playsoundTwo();
            }

        }
        public  void showMyDialog(View view,String title,String mainText,String positiveButton,String negativeButton) {
            DialogFragment dialogFragment = new myDialog();
            Bundle args = new Bundle();
            args.putString("positivebutton",positiveButton);
            args.putString("negativebutton",negativeButton);
            args.putString("title",title);
            args.putString("maintext",mainText);
            dialogFragment.setArguments(args);
            dialogFragment.show(getSupportFragmentManager(), "myDialog");
        }

    }


    private class DoubleJimutoChangeListener implements AdapterView.OnClickListener {
        @Override
        public void onClick(View view) {
            //??????????????????
            new HttpTask(null,"parcels","create").execute();
            new HttpTask(null,"ryosei","create").execute();
            new HttpTask(null,"parcel_event","create").execute();

            //??????????????????????????????
            Intent jimuto_intent = new Intent(MainActivity.this, Double_Jimuto_Change.class);
            jimuto_intent.putExtra("Jimuto_name", jimuto_room);
            jimuto_intent.putExtra("Jimuto_id", jimuto_id);
            startActivityForResult(jimuto_intent, JIMUTOCHANGE_ACTIVITY);
            touchsound.playsoundTwo();
        }
    }

    private class NightDutyNimotsufudaListener implements AdapterView.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent nimotsufuda_intent = new Intent(MainActivity.this, Night_Duty_NimotsuFuda.class);
            startActivity(nimotsufuda_intent);
            touchsound.playsoundTwo();
        }
    }
    private class RefreshListener implements AdapterView.OnClickListener {
        @Override
        public void onClick(View view) {

            eventLogshow();

            //??????????????????
            new HttpTask(null,"parcels","create").execute();
            new HttpTask(null,"ryosei","create").execute();
            new HttpTask(null,"parcel_event","create").execute();

            //??????????????????????????????
        }
    }


    public class duty_night_listener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (jimuto_id == null) {
                this.showMyDialog(null,getString(R.string.main_not_selected_staff),"",getString(R.string.ok),"");
                touchsound.playsoundTwo();
            } else {
                Intent intent = new Intent(MainActivity.this, Night_Duty_NimotsuFuda.class);
                intent.putExtra("Jimuto_id", jimuto_id);
                intent.putExtra("Jimuto_room", jimuto_room);
                //intent.putExtra("Jimuto_name", jimuto_name);
                startActivityForResult(intent, EVENT_REFRESH_ACTIVITY);
                touchsound.playsoundTwo();
            }
        }

        public  void showMyDialog(View view,String title,String mainText,String positiveButton,String negativeButton) {
            DialogFragment dialogFragment = new myDialog();
            Bundle args = new Bundle();
            args.putString("positivebutton",positiveButton);
            args.putString("negativebutton",negativeButton);
            args.putString("title",title);
            args.putString("maintext",mainText);
            dialogFragment.setArguments(args);
            dialogFragment.show(getSupportFragmentManager(), "myDialog");
        }
    }

    private class EventShowListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //??????????????????
            new HttpTask(null,"parcels","create").execute();
            new HttpTask(null,"ryosei","create").execute();
            new HttpTask(null,"parcel_event","create").execute();

            //??????????????????????????????
            String event_id = "";
            String ryosei_uid = "";
            String created_at = null;
            String event_type = null;
            String parcel_uid = "";
            String room_name = "";
            String ryosei_name = "";
            String target_event_uid = "";
            String is_finished = "";
            Map<String, String> item = (Map) parent.getItemAtPosition(position);
            //TextView configshow = findViewById(R.id.showText);
            //configshow.setText(item.get("id"));
            item.get("id");
            String sql = "SELECT uid, created_at, event_type,ryosei_uid, parcel_uid, room_name, ryosei_name, target_event_uid FROM parcel_event WHERE uid = '" +
                    item.get("id") + "'";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                event_id = cursor.getString(cursor.getColumnIndex("uid"));
                created_at = cursor.getString(cursor.getColumnIndex("created_at"));
                event_type = String.valueOf(cursor.getInt(cursor.getColumnIndex("event_type")));//int??????String???????????????
                parcel_uid = cursor.getString(cursor.getColumnIndex("parcel_uid"));
                room_name = cursor.getString(cursor.getColumnIndex("room_name"));
                ryosei_name = cursor.getString(cursor.getColumnIndex("ryosei_name"));
                target_event_uid = cursor.getString(cursor.getColumnIndex("target_event_uid"));
                ryosei_uid = cursor.getString(cursor.getColumnIndex("ryosei_uid"));
            }

            if (event_id == "") {
                return;
            }

            DialogFragment dialogFragment = new Delete_Event_Dialog();
            Bundle args = new Bundle();
            args.putString("event_id", event_id);
            args.putString("parcel_id", parcel_uid);
            args.putString("ryosei_id", ryosei_uid);
            args.putString("jimuto_id",jimuto_id);
            args.putString("event_type", event_type);
            dialogFragment.setArguments(args);
            dialogFragment.show(getSupportFragmentManager(), "Delete_Event_Dialog");
            touchsound.playsoundTwo();

        }

    }


    public class HttpTask {
        String result;
        String table;
        String method;
        public HttpTask(String result,String table, String method){
            this.result = result;
            this.table = table;
            this.method = method;

        }
        public void execute(){
            HttpListener httpListener=new HttpListener(result,table,method);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(httpListener);
        }
    }

    private class HttpListener implements Runnable {
        String result;
        String table;
        String method;

        public HttpListener(String result, String table, String method) {
            this.result = result;
            this.table = table;
            this.method = method;
        };


        @Override
        public void run(){
            //String json = getJsonFromDatabase();
            //?????????????????????????????????5???????????????????????????????????????????????????????????????
            //allaylist???uid???????????????
            int uids_per_one_sync=5;
            boolean onemore=true;

            while(onemore) {//PC?????????????????????????????????or?????????????????????????????????????????????????????????????????????while
                ArrayList<String> uids_for_sync = _helper.select_for_sync(db, table, uids_per_one_sync);
                onemore = (uids_per_one_sync == uids_for_sync.size());//???????????????????????????????????????????????????????????????ArrayList???5???????????????????????????false?????????

                String json = _helper.select_for_json(db, table, uids_for_sync);
                OkHttpPost postTask = new OkHttpPost(MainActivity.this, handler, json, db, _helper, method, table);
                postTask.url = postTask.url + "/" + table + "/" + method;
                postTask.setListener(createListener());
                postTask.execute();

                //????????????????????????????????????this.result?????????????????????
                int c=0;
                while (c<1000) {
                    if (this.result == null) {
                        try {
                            Thread.sleep(100);
                            c++;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (this.result.equals("Success")) {
                        //PC??????????????????????????????????????????
                        _helper.update_sharing_status_for_success(db,table,uids_for_sync);
                        result = null;
                        onemore=true;
                        break;
                    } else if (this.result.equals("")) {
                        //PC????????????????????????????????????
                        _helper.update_sharing_status_for_success(db,table,uids_for_sync);
                        result = null;
                        break;
                    }
                }
                if(c==1000)return;

            }
        }
        private OkHttpPost.Listener createListener() {
            return new OkHttpPost.Listener() {
                @Override
                public void onReceiveResponseFromPC(String res) {
                    result = res;
                }
            };
        }


    }

    /*//??????????????????????????????????????????????????????????????????
    private class SendRequestListener implements View.OnClickListener {
        String result = null;
        String table;
        String method;

        public SendRequestListener(String table, String method) {
            this.table = table;
            this.method = method;
        };

        @Override
        public void onClick(View view) {
            touchsound.playsoundTwo();
            /*
            HttpListener httpListener = new HttpListener(result, table, method);
            httpListener.Listen();

//            new HttpListener(result, table, method).Listen();
            new HttpTask(result, table, method).execute();
        }
    }

    private class ShowRecordsListener implements View.OnClickListener {
        String table;

        public ShowRecordsListener(String table) {
            this.table = table;
        }

        @Override
        public void onClick(View view) {
            // ????????????????????????SQL?????????????????????
//            String json = getJsonFromDatabase();
            String json = _helper.select_show_json(db,1,table);
            android.util.Log.d("json",json);
            android.util.Log.d("json",json);

        }
        */
/*
        private String getJsonFromDatabase() {
            int sharing_status = 1;
            switch (table) {
                case "ryosei":
                    return _helper.select_ryosei_show_json(db, sharing_status);
                case "parcels":
                    return _helper.select_parcels_show_json(db, sharing_status);
                case "parcel_event":
                    return _helper.select_event_show_json(db, sharing_status);
                default:
                    return "";
            }
        }

    }


 */


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //??????????????????????????????????????????????????????????????????????????????????????????
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case JIMUTOCHANGE_ACTIVITY:
                jimuto_id = intent.getStringExtra("Jimuto_id");
                jimuto_room = intent.getStringExtra("Jimuto_room_name");
                TextView jimuto_show = findViewById(R.id.main_jimutou_show);
                jimuto_show.setText(jimuto_room);

                _helper.jimuto_change_event(db,jimuto_id);
            case EVENT_REFRESH_ACTIVITY:
                boolean event_update = intent.getBooleanExtra("EventRefresh", false);
                eventLogshow();
            default:
                //??????????????????????????????
        }


        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            qr_uuid = scanResult.getContents();
            TextView qr_show = findViewById(R.id.qr_result);
            qr_show.setText(qr_uuid);
            // DB??????????????????????????????????????????
            _helper = new DatabaseHelper(MainActivity.this);
            SQLiteDatabase db = _helper.getWritableDatabase();
            String sql = " SELECT uid,is_released,fragile,owner_ryosei_name,owner_room_name,owner_uid FROM parcels WHERE uid ='" + qr_uuid + "';";
            Cursor cursor = db.rawQuery(sql, null);
            int is_released = 0;
            String uid = "";
            String owner_ryosei_name = "";
            String owner_room_name = "";
            int fragile = 0;
            if(cursor.getCount() == 0){//QR?????????????????????????????????????????????
                this.showMyDialog(null,getString(R.string.error),getString(R.string.qr_no_qr),getString(R.string.ok),"");
            }else if(cursor.getCount() == 1) {//QR???????????????????????????????????????????????????
                while (cursor.moveToNext()) {
                    uid = cursor.getString(cursor.getColumnIndex("owner_uid"));
                    owner_ryosei_name = cursor.getString(cursor.getColumnIndex("owner_ryosei_name"));
                    owner_room_name = cursor.getString(cursor.getColumnIndex("owner_room_name"));
                    is_released = cursor.getInt(cursor.getColumnIndex("is_released"));
                }

                if(is_released == 0){//????????????????????????????????????
                    this.showQRDialog(null,owner_room_name,owner_ryosei_name,uid);
                }else{//???????????????????????????????????????
                    this.showMyDialog(null,getString(R.string.error),getString(R.string.qr_already),getString(R.string.ok),"");
                }
            }else{//QR?????????????????????????????????????????????????????????
                this.showMyDialog(null,getString(R.string.error),getString(R.string.qr_more_than_two),getString(R.string.ok),"");
            }
        }

        //??????????????????
        new HttpTask(null,"parcels","create").execute();
        new HttpTask(null,"ryosei","create").execute();
        new HttpTask(null,"parcel_event","create").execute();

        //??????????????????????????????
        eventLogshow();

    }

    public void showQRDialog(View view, String owner_room, String owner_name, String owner_id) {
        DialogFragment dialogFragment = new Nimotsu_Uketori_QR_Dialog();
        Bundle args = new Bundle();
        args.putString("owner_room",owner_room);
        args.putString("owner_name",owner_name);
        args.putString("owner_id",owner_id);
        args.putString("release_staff_room",jimuto_room);
        args.putString("release_staff_name",jimuto_name);
        args.putString("release_staff_id",jimuto_id);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "Nimotsu_Uketori_Dialog");
    }

    public  void showMyDialog(View view,String title,String mainText,String positiveButton,String negativeButton) {
        DialogFragment dialogFragment = new myDialog();


        Bundle args = new Bundle();
        args.putString("positivebutton",positiveButton);
        args.putString("negativebutton",negativeButton);
        args.putString("title",title);
        args.putString("maintext",mainText);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "myDialog");
    }
}
