package com.vatcore.tictactoe;

import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/26.
 */

public class MatchFragment extends Fragment {

    private static final String YOUR_PAWN_IS = "Your pawn is";

    private static boolean sAliveThreadFlag = true;
    private static int sAliveThreadCount = 0;
    private static Thread sAliveThread;

    private String mHost = "";
    private int mUserId = -1;  //由服务器分配
    private String mUserPassword = "";  //由服务器分配
    private int mRoomId = -1;  //所在房间
    private String mRoomPassword = "";

//    private int[][] mChessBoard  = new int[3][3];  // default:0 , X:1 , O:2
    private int step = 0;  //max:9
    private boolean mMyPawn;  // false:X , true:O , mPawn:peopleA , !mPawn:computer or peopleB
    private boolean mEnemyPawn = true;
    private boolean isMatch;  //一旦选定棋子并match了,则此连接的比赛都为选定棋子,只有重新连接才能更换棋子
//    private boolean isInRoom;
//    private boolean isReady;
    private boolean isGameStart;
    private boolean isGameOver;
    private boolean mCanPlay;  // false:X , true:O  ... false:peopleA , true:peopleB ,who first ,default:false

    private EditText mEditTextHost;
    private ToggleButton mToggleButtonConnect;
    private TextView mTextViewServiceInfobox;
    private Button mButtonMatch;
    private ToggleButton mButtonReady;

    private Button mButtonX;
    private Button mButtonO;

    private TextView mTextViewGameInfobox;
    private Button[][] mButtonArray = new Button[3][3];

    private static MatchFragment sFragment;

    public static MatchFragment newInstance() {
        if(sFragment==null) {
            sFragment = new MatchFragment();
        }
        return sFragment;
    }

    private void initService() {
        sAliveThreadFlag = true;
        sAliveThreadCount = 0;
        sAliveThread = null;
        mUserId = -1;
        mUserPassword = "";
        initMatch();
    }

    private void initMatch() {
        mRoomId = -1;  //所在房间
        mRoomPassword = "";
//        mMyPawn = false;
//        mEnemyPawn = true;
        isMatch = false;
//        isReady = false;
        mCanPlay = false;

//        mButtonX.setBackgroundColor(Color.parseColor("#00FF00"));  //default ,green
//        mButtonO.setBackgroundColor(Color.parseColor("#FFFF00"));  //default ,yellow
//        mTextViewGameInfobox.setText(YOUR_PAWN_IS + " X");  //default

        initGame();
    }

    private void initGame() {
//        mChessBoard = new int[3][3];
        step = 0;
//        isGameStart = false;
//        isGameOver = false;

        for(Button[] a:mButtonArray) {
            for(Button button:a) {
                button.setText("");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_match, container, false);
        mEditTextHost = (EditText) v.findViewById(R.id.edit_text_host);
        mToggleButtonConnect = (ToggleButton) v.findViewById(R.id.toggle_button_connect);
        mTextViewServiceInfobox = (TextView) v.findViewById(R.id.textview_service_infobox);
        mButtonMatch = (Button) v.findViewById(R.id.button_match);
        mButtonReady = (ToggleButton) v.findViewById(R.id.button_ready);

        mButtonX = (Button) v.findViewById(R.id.button_x);
        mButtonO = (Button) v.findViewById(R.id.button_o);
        mTextViewGameInfobox = (TextView) v.findViewById(R.id.textview_game_infobox);

        mButtonArray[0][0] = (Button) v.findViewById(R.id.button00);
        mButtonArray[0][1] = (Button) v.findViewById(R.id.button01);
        mButtonArray[0][2] = (Button) v.findViewById(R.id.button02);
        mButtonArray[1][0] = (Button) v.findViewById(R.id.button10);
        mButtonArray[1][1] = (Button) v.findViewById(R.id.button11);
        mButtonArray[1][2] = (Button) v.findViewById(R.id.button12);
        mButtonArray[2][0] = (Button) v.findViewById(R.id.button20);
        mButtonArray[2][1] = (Button) v.findViewById(R.id.button21);
        mButtonArray[2][2] = (Button) v.findViewById(R.id.button22);

        initService();
        //dev
        mEditTextHost.setText("192.168.1.102:8080");
        //
        mButtonX.setBackgroundColor(Color.parseColor("#00FF00"));  //default ,green
        mButtonO.setBackgroundColor(Color.parseColor("#FFFF00"));  //default ,yellow
        mTextViewGameInfobox.setText(YOUR_PAWN_IS + " X");  //default

        for(final int[] i={0};i[0]<mButtonArray.length;i[0]++) {
            for(final int[] j={0};j[0]<mButtonArray[i[0]].length;j[0]++) {

                mButtonArray[i[0]][j[0]].setOnClickListener(new View.OnClickListener() {
                    private int a = i[0];
                    private int b = j[0];

                    @Override
                    public void onClick(View v) {
                        if(mCanPlay) {
                            mCanPlay = false;
//                            mCanPlay = !mCanPlay;
                            new PlayTask().execute(a, b);

//                            mCanPlay = false;
                        }
                    }
                });
            }
        }

        mToggleButtonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(mToggleButtonConnect.isChecked()) {
                sAliveThreadFlag = true;

                mTextViewServiceInfobox.setText("connecting...");
                new ConnectTask().execute();
            }
            else {
                mUserId = -1;
                sAliveThreadFlag = false;
                initService();

                mTextViewServiceInfobox.setText("disconnecting...");

//                    new DisconnectTask().execute();  //未实现
            }
            }

        });

        mButtonMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMatch();
                mButtonReady.setChecked(false);
                isMatch = true;
                new MatchTask().execute();

                if(sAliveThreadCount==0) {
                    sAliveThread = new Thread() {
                        @Override
                        public void run() {
                            while (sAliveThreadFlag) {
                                new AliveTask().execute();
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    sAliveThreadCount++;
                    sAliveThread.start();
                }
            }
        });

        mButtonReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMatch) {
                    if(mButtonReady.isChecked()) {
                        new ReadyTask().execute();
                    }
                    else {
//                        new CancelReadyTask().execute();  //未实现
                    }
                }
            }
        });

        mButtonX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isMatch) {
                    mMyPawn = false;
                    mEnemyPawn = true;
                    mButtonX.setBackgroundColor(Color.parseColor("#00FF00"));
                    mButtonO.setBackgroundColor(Color.parseColor("#FFFF00"));
                    mTextViewGameInfobox.setText(YOUR_PAWN_IS + " X");
                }
            }
        });

        mButtonO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isMatch) {
                    mMyPawn = true;
                    mEnemyPawn = false;
                    mButtonO.setBackgroundColor(Color.parseColor("#00FF00"));
                    mButtonX.setBackgroundColor(Color.parseColor("#FFFF00"));
                    mTextViewGameInfobox.setText(YOUR_PAWN_IS + " O");
                }
            }
        });

        return v;
    }

    private static byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        }
        finally {
            connection.disconnect();
        }

    }

    private static String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }


    private class ConnectTask extends AsyncTask<Void,Void,List<Object>> {

        private static final String TAG = "ConnectTask";
        private String host = mEditTextHost.getText().toString();

        @Override
        protected List<Object> doInBackground(Void... params) {
            List<Object> objectList = new ArrayList<>();
            try{
                String url = Uri.parse("http://" + host + "/connect")
                        .buildUpon()
                        .build().toString();
                Log.i(TAG, url);
                String jsonString = getUrlString(url);
                Log.i(TAG, "Received JSON: " + jsonString);
                JSONObject jsonBody = new JSONObject(jsonString);

                boolean isConnectOk = jsonBody.getBoolean("connectOk");
                int userId = jsonBody.getInt("userId");
                String userPassword = jsonBody.getString("userPassword");

                objectList.add(host);
                objectList.add(isConnectOk);
                objectList.add(userId);
                objectList.add(userPassword);
            }
            catch(IOException ioe) {
                Log.e(TAG, "Failed to fetch URL: ",ioe);
            }
            catch (JSONException je) {
                Log.e(TAG, "Failed to parse JSON", je);
            }
            return objectList;

        }

        @Override
        protected void onPostExecute(List<Object> objectList) {

            String host = "";
            boolean isConnectOk = false;
            int userId = -1;
            String userPassword = "";

            try {
                host = (String) objectList.get(0);
                isConnectOk = (boolean) objectList.get(1);
                userId = (int) objectList.get(2);
                userPassword = (String) objectList.get(3);
            }
            catch (Exception e) {
                Log.e(TAG,"e",e);
                mTextViewServiceInfobox.setText("connect error");
                mToggleButtonConnect.setChecked(false);
            }

            if(isConnectOk) {
                Log.i(TAG, isConnectOk + "." + userId);
                mHost = host;
                mUserId = userId;
                mUserPassword = userPassword;

                mEditTextHost.setText(host);
                mToggleButtonConnect.setTextOn("userId:" + userId);
                mTextViewServiceInfobox.setText("success connect p:" + userPassword);
            }
        }
    }

    private class MatchTask extends AsyncTask<Void,Void,List<Object>> {

        private static final String TAG = "MatchTask";
        private String host = mHost;

        @Override
        protected List<Object> doInBackground(Void... params) {
            List<Object> objectList = new ArrayList<>();
            try{
                String url = Uri.parse("http://" + host + "/match")
                        .buildUpon()
                        .appendQueryParameter("userId", mUserId + "")
                        .appendQueryParameter("userPassword", mUserPassword)
                        .appendQueryParameter("pawn", mMyPawn + "")
                        .build().toString();
                Log.i(TAG, url);
                String jsonString = getUrlString(url);
                Log.i(TAG, "Received JSON: " + jsonString);
                JSONObject jsonBody = new JSONObject(jsonString);


                ////
                boolean isMatchOk = jsonBody.getBoolean("isMatchOk");
                int roomId = jsonBody.getInt("roomId");
                String roomPassword = jsonBody.getString("roomPassword");

                objectList.add(isMatchOk);
                objectList.add(roomId);
                objectList.add(roomPassword);
            }
            catch(IOException ioe) {
                Log.e(TAG, "Failed to fetch URL: ",ioe);
            }
            catch (JSONException je) {
                Log.e(TAG, "Failed to parse JSON", je);
            }
            return objectList;

        }

        @Override
        protected void onPostExecute(List<Object> objectList) {

            boolean isMatchOk = false;
            int roomId = -1;
            String roomPassword = "";

            try {
                isMatchOk = (boolean) objectList.get(0);
                roomId = (int) objectList.get(1);
                roomPassword = (String) objectList.get(2);
            }
            catch (Exception e) {
                Log.e(TAG,"e",e);
                mTextViewServiceInfobox.setText("match error");
            }

            if(isMatchOk) {
                mRoomId = roomId;
                mRoomPassword = roomPassword;

                mTextViewServiceInfobox.setText("success match p:" + roomPassword);
            }
        }
    }

    //
    private class ReadyTask extends AsyncTask<Void,Void,List<Object>> {

        private static final String TAG = "ReadyTask";
        private String host = mHost;

        @Override
        protected List<Object> doInBackground(Void... params) {
            List<Object> objectList = new ArrayList<>();
            try {
                String url = Uri.parse("http://" + host + "/ready")
                        .buildUpon()
                        .appendQueryParameter("userId", mUserId + "")
                        .appendQueryParameter("userPassword", mUserPassword)
                        .appendQueryParameter("roomId", mRoomId + "")
                        .appendQueryParameter("roomPassword", mRoomPassword)
                        .build().toString();
                Log.i(TAG, url);
                String jsonString = getUrlString(url);
                Log.i(TAG, "Received JSON: " + jsonString);
                JSONObject jsonBody = new JSONObject(jsonString);

                //
                boolean isReadyOk = jsonBody.getBoolean("isReadyOk");


                objectList.add(0,isReadyOk);

            } catch (IOException ioe) {
                Log.e(TAG, "Failed to fetch URL: ", ioe);
            } catch (JSONException je) {
                Log.e(TAG, "Failed to parse JSON", je);
            }
            return objectList;

        }

        @Override
        protected void onPostExecute(List<Object> objectList) {


            boolean isReadyOk = false;

            try {
                isReadyOk = (boolean) objectList.get(0);
            }
            catch (Exception e) {
                Log.e(TAG,"e",e);
                mTextViewServiceInfobox.setText("ready error");
            }

            if(isReadyOk) {
                mTextViewGameInfobox.setText("");
                mButtonReady.setChecked(true);
                initGame();
            }
        }
    }

    private class PlayTask extends AsyncTask<Integer,Void,List<Object>> {

        private static final String TAG = "PlayTask";
        private String host = mHost;

        @Override
        protected List<Object> doInBackground(Integer... params) {
            List<Object> objectList = new ArrayList<>();
            try {
                String url = Uri.parse("http://" + host + "/play")
                        .buildUpon()
                        .appendQueryParameter("userId", mUserId + "")
                        .appendQueryParameter("userPassword", mUserPassword)
                        .appendQueryParameter("roomId", mRoomId + "")
                        .appendQueryParameter("roomPassword", mRoomPassword)
                        .appendQueryParameter("i", params[0] + "")
                        .appendQueryParameter("j", params[1] + "")
                        .build().toString();
                Log.i(TAG, url);
                String jsonString = getUrlString(url);
                Log.i(TAG, "Received JSON: " + jsonString);
                JSONObject jsonBody = new JSONObject(jsonString);

                //
                boolean isPlayOk = jsonBody.getBoolean("isPlayOk");


                objectList.add(0,isPlayOk);
                objectList.add(1,params[0]);
                objectList.add(2,params[1]);

            } catch (IOException ioe) {
                Log.e(TAG, "Failed to fetch URL: ", ioe);
            } catch (JSONException je) {
                Log.e(TAG, "Failed to parse JSON", je);
            }
            return objectList;

        }

        @Override
        protected void onPostExecute(List<Object> objectList) {


            boolean isPlayOk = false;
            int i = -1;
            int j = -1;

            try {
                isPlayOk = (boolean) objectList.get(0);
                i = (int) objectList.get(1);
                j = (int) objectList.get(2);
            }
            catch (Exception e) {
                Log.e(TAG,"e",e);
                mTextViewServiceInfobox.setText("ready error");
            }

            if(isPlayOk) {
                if(!mMyPawn) {
                    mButtonArray[i][j].setText("X");
//                    mCanPlay = !mCanPlay;  //修改
                }
                else {
                    mButtonArray[i][j].setText("O");
//                    mCanPlay = !mCanPlay;
                }
            }
        }
    }

    private class AliveTask extends AsyncTask<Void,Void,List<Object>> {

        private static final String TAG = "AliveTask";
        private String host = mHost;

        @Override
        protected List<Object> doInBackground(Void... params) {
            List<Object> objectList = new ArrayList<>();
            try {
                String url = Uri.parse("http://" + host + "/alive")
                        .buildUpon()
                        .appendQueryParameter("userId", mUserId + "")
                        .appendQueryParameter("userPassword", mUserPassword)
                        .appendQueryParameter("roomId", mRoomId + "")
                        .appendQueryParameter("roomPassword", mRoomPassword)
                        .build().toString();
                Log.i(TAG, url);
                String jsonString = getUrlString(url);
                Log.i(TAG, "Received JSON: " + jsonString);
                JSONObject jsonBody = new JSONObject(jsonString);

                //
                boolean isAliveOk = jsonBody.getBoolean("isAliveOk");
                JSONObject aliveMessageMap = jsonBody.getJSONObject("aliveMessageMap");
                boolean isEnemyInTheRoom = aliveMessageMap.getBoolean("isEnemyInTheRoom");
                boolean isRoomClose = aliveMessageMap.getBoolean("isRoomClose");
                boolean isReady = aliveMessageMap.getBoolean("isReady");
                boolean isEnemyReady = aliveMessageMap.getBoolean("isEnemyReady");
                boolean isBothReady = aliveMessageMap.getBoolean("isBothReady");
                boolean isWin = aliveMessageMap.getBoolean("isWin");
                boolean isTie = aliveMessageMap.getBoolean("isTie");
                boolean isLose = aliveMessageMap.getBoolean("isLose");
                JSONArray chessBoardArray = aliveMessageMap.getJSONArray("chessBoard");
                boolean whoCanPlay = aliveMessageMap.getBoolean("whoCanPlay");



                objectList.add(0,isAliveOk);
                objectList.add(1,isEnemyInTheRoom);
                objectList.add(2,isRoomClose);
                objectList.add(3,isReady);
                objectList.add(4,isEnemyReady);
                objectList.add(5,isBothReady);
                objectList.add(6,isWin);
                objectList.add(7,isTie);
                objectList.add(8,isLose);
                objectList.add(9,chessBoardArray);
                objectList.add(10,whoCanPlay);

            } catch (IOException ioe) {
                Log.e(TAG, "Failed to fetch URL: ", ioe);
            } catch (JSONException je) {
                Log.e(TAG, "Failed to parse JSON", je);
            }
            return objectList;

        }

        @Override
        protected void onPostExecute(List<Object> objectList) {


            boolean isAliveOk = false;
            boolean isEnemyInTheRoom = false;
            boolean isRoomClose = false;
            boolean isReady = false;
            boolean isEnemyReady = false;
            boolean isBothReady = false;
            boolean isWin = false;
            boolean isTie = false;
            boolean isLose = false;
            JSONArray chessBoardArray = null;
            boolean whoCanPlay = false;

            try {
                isAliveOk = (boolean) objectList.get(0);
                isEnemyInTheRoom = (boolean) objectList.get(1);
                isRoomClose = (boolean) objectList.get(2);
                isReady = (boolean) objectList.get(3);
                isEnemyReady = (boolean) objectList.get(4);
                isBothReady = (boolean) objectList.get(5);
                isWin = (boolean) objectList.get(6);
                isTie = (boolean) objectList.get(7);
                isLose = (boolean) objectList.get(8);
                chessBoardArray = (JSONArray) objectList.get(9);
                whoCanPlay = (boolean) objectList.get(10);
            }
            catch (Exception e) {
                Log.e(TAG,"e",e);
                mTextViewServiceInfobox.setText("alive error");
                initMatch();
                mButtonReady.setChecked(false);
            }

            if(isAliveOk) {
                if(isRoomClose) {
                    initMatch();
                    mTextViewServiceInfobox.setText("room is close!!!");
                    return;
                }
                if(isEnemyInTheRoom) {
                    String enemy_enter = "enemy enter";
                    String space = " ";
                    String ready = "ready";
                    String enemy_ready = "enemy_ready";
                    mTextViewServiceInfobox.setText(enemy_enter);
                    if(mMyPawn==whoCanPlay) {
                        mCanPlay = true;
                    }
                    else {

                    }

                    if(chessBoardArray != null) {
                        try {
//                            JSONArray chessBoardArrayZero = chessBoardArray.getJSONArray(0);
//                            JSONArray chessBoardArrayOne = chessBoardArray.getJSONArray(1);
//                            JSONArray chessBoardArrayTwo = chessBoardArray.getJSONArray(2);

                            int[][] chessBoard = new int[3][3];
                            for(int i=0;i<chessBoard.length;i++) {
                                for(int j=0;j<chessBoard[i].length;j++) {
                                    chessBoard[i][j] = chessBoardArray.getJSONArray(i).getInt(j);
                                }
                            }
                            for(int i=0;i<mButtonArray.length;i++) {
                                for(int j=0;j<mButtonArray[i].length;j++) {
                                    if(chessBoard[i][j]==1) {
                                        mButtonArray[i][j].setText("X");
                                    }
                                    else if(chessBoard[i][j]==2) {
                                        mButtonArray[i][j].setText("O");
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            Log.e(TAG,"chessBoardArray",e);
                        }
                    }

                    if(isReady) {
                        mTextViewServiceInfobox.setText(mTextViewServiceInfobox.getText() + space + ready);
                    }
                    if(isEnemyReady) {
                        mTextViewServiceInfobox.setText(mTextViewServiceInfobox.getText() + space + enemy_ready);
                    }
                    if(isBothReady&&mIsBothReadyCount==0) {
                        initGame();
                        isGameStart = true;
                        if(!mMyPawn) {  //x棋先走
                            mCanPlay = true;
                        }
                        mTextViewServiceInfobox.setText("game start!");
                        mIsBothReadyCount++;
                    }
                    if(isBothReady) {
                        mTextViewServiceInfobox.setText("game start!");
                    }
                    if(isWin) {
                        mTextViewGameInfobox.setText("you win");
                        mButtonReady.setChecked(false);
                        mIsBothReadyCount = 0;
                        mCanPlay = false;
                    }
                    else if(isTie) {
                        mTextViewGameInfobox.setText("tie");
                        mButtonReady.setChecked(false);
                        mIsBothReadyCount = 0;
                        mCanPlay = false;
                    }
                    else if(isLose) {
                        mTextViewGameInfobox.setText("you lose");
                        mButtonReady.setChecked(false);
                        mIsBothReadyCount = 0;
                        mCanPlay = false;
                    }
                }
            }
        }
    }

    private int mIsBothReadyCount = 0;
}
