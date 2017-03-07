package com.vatcore.tictactoe;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Administrator on 2017/2/24.
 */

public class ComputerFragment extends Fragment {

    private static final String YOUR_PAWN_IS = "Your pawn is";

    private int[][] mChessBoard;  // default:0 , X:1 , O:2
    private int step = 0;  //max:9

    private boolean mPawn;  // false:X , true:O , mPawn:peopleA , !mPawn:computer or peopleB
    private boolean mPawnB = true;
    private boolean isGameStart;
    private boolean isGameOver;
    private boolean mWhoCanPlay;  // false:X , true:O  ... false:peopleA , true:peopleB ,who first ,default:false

    private Button mButtonRestart;

    private Button mButtonX;
    private Button mButtonO;

    private TextView mGameInfobox;

    private Button[][] mButtonArray = new Button[3][3];

    private static ComputerFragment sFragment;

    public static ComputerFragment newInstance() {
        if(sFragment==null) {
            sFragment = new ComputerFragment();
        }
        return sFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_computer, container, false);

        mButtonRestart = (Button) v.findViewById(R.id.button_restart);

        mButtonX = (Button) v.findViewById(R.id.button_x);
        mButtonO = (Button) v.findViewById(R.id.button_o);

        mGameInfobox = (TextView) v.findViewById(R.id.textview_game_infobox);

        mButtonArray[0][0] = (Button) v.findViewById(R.id.button00);
        mButtonArray[0][1] = (Button) v.findViewById(R.id.button01);
        mButtonArray[0][2] = (Button) v.findViewById(R.id.button02);
        mButtonArray[1][0] = (Button) v.findViewById(R.id.button10);
        mButtonArray[1][1] = (Button) v.findViewById(R.id.button11);
        mButtonArray[1][2] = (Button) v.findViewById(R.id.button12);
        mButtonArray[2][0] = (Button) v.findViewById(R.id.button20);
        mButtonArray[2][1] = (Button) v.findViewById(R.id.button21);
        mButtonArray[2][2] = (Button) v.findViewById(R.id.button22);

        initGame();

        mButtonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restart();
            }
        });

        mButtonX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isGameStart) {
                    mPawn = false;
                    mPawnB = true;
                    mButtonX.setBackgroundColor(Color.parseColor("#00FF00"));
                    mButtonO.setBackgroundColor(Color.parseColor("#FFFF00"));
                    mGameInfobox.setText(YOUR_PAWN_IS + " X");
                }
            }
        });

        mButtonO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isGameStart) {
                    mPawn = true;
                    mPawnB = false;
                    mButtonO.setBackgroundColor(Color.parseColor("#00FF00"));
                    mButtonX.setBackgroundColor(Color.parseColor("#FFFF00"));
                    mGameInfobox.setText(YOUR_PAWN_IS + " O");
                }
            }
        });

//        final Queue<Integer> list_i = new ArrayDeque<>();
//        final Queue<Integer> list_j = new ArrayDeque<>();

//        for(int i=0;i<mButtonArray.length;i++) {
//            for(int j=0;j<mButtonArray[i].length;j++) {
//                list_i.offer(i);
//                list_j.offer(j);
//
//                mButtonArray[i][j].setOnClickListener(new View.OnClickListener() {
//                    private int i = list_i.poll();
//                    private int j = list_j.poll();

        for(final int[] i={0};i[0]<mButtonArray.length;i[0]++) {
            for(final int[] j={0};j[0]<mButtonArray[i[0]].length;j[0]++) {

                mButtonArray[i[0]][j[0]].setOnClickListener(new View.OnClickListener() {
                    private int a = i[0];
                    private int b = j[0];

                    @Override
                    public void onClick(View v) {
                        if(!isGameStart&&mPawn==mWhoCanPlay) {
                            isGameStart = true;
                        }
                        else if(!isGameStart&&mPawnB==mWhoCanPlay){
                            isGameStart = true;
                            computerDefault();
                            return;
                        }
                        if(!isGameOver) {
                            if (peopleA(a, b) && !isGameOver) {
                                computerDefault();
                            }
                        }
                    }
                });
            }
        }

        return v;
    }

    private boolean peopleA(int i, int j){
//        Log.i("IJ",i + "," + j);
        if (mPawn&&mChessBoard[i][j]==0&&!isGameOver) {  //mPawn:判断棋子是X还是O, mPawn==!mWhoCanPlay:判断此玩家能否下棋, mChessBoard[i][j]==0:判断此格是否能下, isGameOver:判断游戏是否结束
            mButtonArray[i][j].setText("O");
            mChessBoard[i][j] = 2;
            mWhoCanPlay = !mWhoCanPlay;
            step++;
        }
        else if(!mPawn&&mChessBoard[i][j]==0&&!isGameOver){
            mButtonArray[i][j].setText("X");
            mChessBoard[i][j] = 1;
            mWhoCanPlay = !mWhoCanPlay;
            step++;
        }
        else {
            return false;
        }

        judge();
        return true;
    }

    private void computerDefault() {

//        computerRandom();

//        mWhoCanPlay = !mWhoCanPlay;
        computerHard();
        judge();
    }

    private void computerIntercept() {  // 对两个连续的棋子马上拦截或下子
        int computerPawn = 0;
        if(mPawnB) {  //computer:O
            computerPawn = 2;
        }
        else {  //computer:X;
            computerPawn = 1;
        }

        int[] enemy = new int[]{-1,-1};
        I:
        for(int i=0;i<mButtonArray.length;i++) {
            for(int j=0;j<mButtonArray[i].length;j++) {
                if(i==1&&j==1) continue;
                if(mChessBoard[1][1]!=0&&mChessBoard[1][1]==mChessBoard[i][j]) {
                    int a = i;
                    int b = j;
                    if(i==0) a = 2;
                    if(j==0) b = 2;
                    if(i==2) a = 0;
                    if(j==2) b = 0;
                    if(mChessBoard[a][b]==0&&computerPawn==mChessBoard[1][1]) {  //下
                        if(computerPut(a,b)) return;
                    }
                    else if(mChessBoard[a][b]==0&&computerPawn!=mChessBoard[1][1]){  //加入拦截数组
                        enemy[0] = a;
                        enemy[1] = b;
                    }
//                    if(computerPut(a,b)) break I;  //先处理自己的连续,再拦截
                }
            }
        }
        if(enemy[0]!=-1&&enemy[1]!=-1) {
            if(computerPut(enemy[0],enemy[1])) return;
        }

        for(int i=0,count=0;i<mButtonArray.length;i++) {
            for(int j=0; j < mButtonArray[i].length; j++) {
                count++;
                if(count%2==0&&mChessBoard[i][j]!=0) {
                    if(i==1) {
                        for(int q=0;q<3;q=q+2) {
                            int a = i;
                            if(q==0) a = 2;
                            if(q==2) a = 0;
                            if(mChessBoard[i][j]==mChessBoard[q][j]) {
                                if (mChessBoard[a][j] == 0 && computerPawn == mChessBoard[i][j]) {  //下
                                    if (computerPut(a, j)) return;
                                } else if (mChessBoard[a][j] == 0 && computerPawn != mChessBoard[i][j]) {  //加入拦截数组
                                    enemy[0] = a;
                                    enemy[1] = j;
                                }
                            }
                        }
                    }
                    else if(j==1) {
                        for(int q=0;q<3;q=q+2) {
                            int b = j;
                            if(q==0) b = 2;
                            if(q==2) b = 0;
                            if(mChessBoard[i][j]==mChessBoard[i][q]) {
                                if (mChessBoard[i][b] == 0 && computerPawn == mChessBoard[i][j]) {  //下
                                    if (computerPut(i, b)) return;
                                } else if (mChessBoard[i][b] == 0 && computerPawn != mChessBoard[i][j]) {  //加入拦截数组
                                    enemy[0] = i;
                                    enemy[1] = b;
                                }
                            }
                        }
                    }
                }
            }
        }

        if(enemy[0]!=-1&&enemy[1]!=-1) {
            if(computerPut(enemy[0],enemy[1])) return;
        }

        if(mChessBoard[0][0]!=0) {
            if (mChessBoard[0][0] == mChessBoard[0][2]) {
                if (mChessBoard[0][1] == 0 && computerPawn == mChessBoard[0][0]) {  //下
                    if (computerPut(0, 1)) return;
                } else if (mChessBoard[0][1] == 0 && computerPawn != mChessBoard[0][0]) {  //加入拦截数组
                    enemy[0] = 0;
                    enemy[1] = 1;
                }
            }
            if (mChessBoard[0][0] == mChessBoard[2][0]) {
                if (mChessBoard[1][0] == 0 && computerPawn == mChessBoard[0][0]) {  //下
                    if (computerPut(1, 0)) return;
                } else if (mChessBoard[1][0] == 0 && computerPawn != mChessBoard[0][0]) {  //加入拦截数组
                    enemy[0] = 1;
                    enemy[1] = 0;
                }
            }
        }
        if(mChessBoard[2][2]!=0) {
            if (mChessBoard[2][2] == mChessBoard[2][0]) {
                if (mChessBoard[2][1] == 0 && computerPawn == mChessBoard[2][2]) {  //下
                    if (computerPut(2, 1)) return;
                } else if (mChessBoard[2][1] == 0 && computerPawn != mChessBoard[2][2]) {  //加入拦截数组
                    enemy[0] = 2;
                    enemy[1] = 1;
                }
            }
            if (mChessBoard[2][2] == mChessBoard[0][2]) {
                if (mChessBoard[1][2] == 0 && computerPawn == mChessBoard[2][2]) {  //下
                    if (computerPut(1, 2)) return;
                } else if (mChessBoard[1][2] == 0 && computerPawn != mChessBoard[2][2]) {  //加入拦截数组
                    enemy[0] = 1;
                    enemy[1] = 2;
                }
            }
        }

        if(enemy[0]!=-1&&enemy[1]!=-1) {
            if(computerPut(enemy[0],enemy[1])) return;
        }

//        for(int i=0,count=0;i<mButtonArray.length;i++) {
//            for (int j = 0; j < mButtonArray[i].length; j++) {
//                count++;
//                if (count % 2 == 0 && mChessBoard[i][j] != 0) {
//                    int a = i;
//                    int b = j;
//                    if(i==0) a = 2;
//                    if(j==0) b = 2;
//                    if(i==2) a = 0;
//                    if(j==2) b = 0;
//                    if(i==1) {
//
//                    }
//                    else if(j==1) {
//
//                    }
//                }
//            }
//        }
    }

    private void computerGoodLocation() {
//        if(mChessBoard[1][1]==0) {
//            if (computerPut(1, 1)) return;
//        }

        int[] r1 = new int[5];
        int[] r2 = new int[5];
        r1[0] = 0; r2[0] = 0;
        r1[1] = 0; r2[1] = 2;
        r1[2] = 2; r2[2] = 0;
        r1[3] = 2; r2[3] = 2;

        r1[4] = 1; r2[4] = 1;
        boolean[] b = new boolean[5];

        W:
        while(true) {
            for(int i=0,j=0;i<b.length;i++) {
                if(b[i]) j++;
                if(j==5) {
                    break W;
                }
            }
            int r = new Random().nextInt(5);

            if (mChessBoard[r1[r]][r2[r]] == 0) {
                if (computerPut(r1[r], r2[r])) {
                    return;
                }
                else {
                    b[r] = true;
                }
            }
            b[r] = true;
        }

//        computerPut(1, 1);


//        if(mChessBoard[0][0]==0) {
//            if (computerPut(0, 0)) return;
//        }
//        if(mChessBoard[0][2]==0) {
//            if (computerPut(0, 2)) return;
//        }
//        if(mChessBoard[2][0]==0) {
//            if (computerPut(2, 0)) return;
//        }
//        if(mChessBoard[2][2]==0) {
//            if (computerPut(2, 2)) return;
//        }
    }

    private void computerRandom() {
        int i, j;
        while(true) {
            i = new Random().nextInt(3);
            j = new Random().nextInt(3);
            if(computerPut(i ,j)) break;
        }
    }

    private boolean computerPut(int i, int j) {
        if (mChessBoard[i][j] == 0 && !isGameOver) {
            if (mPawnB) {  //mPawn:判断棋子是X还是O, mPawn==!mWhoCanPlay:判断此玩家能否下棋, mChessBoard[i][j]==0:判断此格是否能下, isGameOver:判断游戏是否结束
                mButtonArray[i][j].setText("O");
                mChessBoard[i][j] = 2;
                mWhoCanPlay = !mWhoCanPlay;
                step++;
                return true;
            } else if (!mPawnB) {
                mButtonArray[i][j].setText("X");
                mChessBoard[i][j] = 1;
                mWhoCanPlay = !mWhoCanPlay;
                step++;
                return true;
            }
        }
        return false;
    }

    private void computerHard() {
        computerIntercept();
        if(mPawnB==mWhoCanPlay) {
            computerGoodLocation();
            if(mPawnB==mWhoCanPlay) {
                computerRandom();
            }
        }
    }

    private void judge() {
        if(mChessBoard[0][0]!=0&&mChessBoard[0][0]==mChessBoard[0][1]&&mChessBoard[0][1]==mChessBoard[0][2]) win(mChessBoard[0][0]);
        else if(mChessBoard[1][0]!=0&&mChessBoard[1][0]==mChessBoard[1][1]&&mChessBoard[1][1]==mChessBoard[1][2]) win(mChessBoard[1][0]);
        else if(mChessBoard[2][0]!=0&&mChessBoard[2][0]==mChessBoard[2][1]&&mChessBoard[2][1]==mChessBoard[2][2]) win(mChessBoard[2][0]);
        else if(mChessBoard[0][0]!=0&&mChessBoard[0][0]==mChessBoard[1][0]&&mChessBoard[1][0]==mChessBoard[2][0]) win(mChessBoard[0][0]);
        else if(mChessBoard[0][1]!=0&&mChessBoard[0][1]==mChessBoard[1][1]&&mChessBoard[1][1]==mChessBoard[2][1]) win(mChessBoard[0][1]);
        else if(mChessBoard[0][2]!=0&&mChessBoard[0][2]==mChessBoard[1][2]&&mChessBoard[1][2]==mChessBoard[2][2]) win(mChessBoard[0][2]);
        else if(mChessBoard[0][0]!=0&&mChessBoard[0][0]==mChessBoard[1][1]&&mChessBoard[1][1]==mChessBoard[2][2]) win(mChessBoard[0][0]);
        else if(mChessBoard[2][0]!=0&&mChessBoard[2][0]==mChessBoard[1][1]&&mChessBoard[1][1]==mChessBoard[0][2]) win(mChessBoard[2][0]);
        else if(step==9) win(0);
    }

    private void win(int who) {
        if(who==0) {
            mGameInfobox.setText("tie");
        }
        else if(who==1) {
            mGameInfobox.setText("X win");
        }
        else if(who==2) {
            mGameInfobox.setText("O win");
        }
        isGameOver = true;
    }

    private void initGame() {
        mChessBoard = new int[3][3];
        step = 0;
        mPawn = false;
        mPawnB = true;
        isGameStart = false;
        isGameOver = false;
        mWhoCanPlay = false;

        mButtonX.setBackgroundColor(Color.parseColor("#00FF00"));  //default ,green
        mButtonO.setBackgroundColor(Color.parseColor("#FFFF00"));  //default ,yellow
        mGameInfobox.setText(YOUR_PAWN_IS + " X");  //default

        for(Button[] a:mButtonArray) {
            for(Button button:a) {
                button.setText("");
            }
        }
    }

    private void restart() {
        initGame();
    }
}