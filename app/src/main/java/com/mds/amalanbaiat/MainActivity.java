package com.mds.amalanbaiat;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.net.Uri;
import android.media.MediaPlayer;
import android.content.Intent;

public class MainActivity extends Activity {
    float d;
    FrameLayout root;
    int page = -1; // -1 intro, 0 guru, -2 sanad, 1..18 content
    VideoView video;
    int count = 0;

    final int BG = Color.rgb(247,241,228), TEXT = Color.rgb(45,40,31), BUTTON = Color.rgb(78,107,77);

    @Override public void onCreate(Bundle b) {
        super.onCreate(b); requestWindowFeature(Window.FEATURE_NO_TITLE);
        d = getResources().getDisplayMetrics().density;
        getWindow().setStatusBarColor(BG); 
        getWindow().setNavigationBarColor(Color.rgb(233,222,200));
        getWindow().setNavigationBarDividerColor(Color.rgb(233,222,200));
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
        root = new FrameLayout(this); root.setBackgroundResource(com.mds.amalanbaiat.R.drawable.bg); setContentView(root);
        showIntro();
    }

    @Override public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus) hideSystemNavigation();
    }

    void clear(){ root.removeAllViews(); }
    void fade(View v){ AlphaAnimation a=new AlphaAnimation(0f,1f); a.setDuration(280); v.startAnimation(a); }

    void showIntro(){
        clear();
        hideSystemNavigation(); video=new VideoView(this); root.addView(video,new FrameLayout.LayoutParams(-1,-1));
        video.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.logo_intro)); video.setOnPreparedListener(mp->{ mp.setLooping(false); mp.start(); });
        video.setOnCompletionListener(mp -> showGuru());
    }

    ImageView fullImage(int res){ ImageView iv=new ImageView(this); iv.setImageResource(res); iv.setScaleType(ImageView.ScaleType.FIT_CENTER); return iv; }

    void showGuru(){ page=0; hideSystemNavigation(); clear(); root.setBackgroundColor(Color.BLACK); ImageView iv=fullImage(R.drawable.guru_mursyid); root.addView(iv,new FrameLayout.LayoutParams(-1,-1)); addBottomButton("LANJUT",false,()->showSanad()); fade(iv); }
    void showSanad(){ page=-2; hideSystemNavigation(); clear(); root.setBackgroundColor(Color.BLACK); ImageView iv=fullImage(R.drawable.sanad_thoriqoh); root.addView(iv,new FrameLayout.LayoutParams(-1,-1)); addBottomButton("LANJUTKAN",false,()->showPage(1)); fade(iv); }

    TextView tv(String s,float sp,boolean bold){ TextView t=new TextView(this); t.setText(s); t.setTextColor(TEXT); t.setTextSize(sp); t.setGravity(Gravity.CENTER); t.setTypeface(Typeface.DEFAULT,bold?Typeface.BOLD:Typeface.NORMAL); t.setPadding(20,8,20,8); return t; }
    Button btn(String s){ Button b=new Button(this); b.setText(s); b.setTextSize(14); b.setTextColor(Color.WHITE); b.setAllCaps(false); b.setBackgroundColor(BUTTON); return b; }

    void showPage(int p){ page=p; count=0; clear(); hideSystemNavigation(); root.setBackgroundResource(R.drawable.bg);
        LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setGravity(Gravity.CENTER_HORIZONTAL); box.setPadding(18,10,18,8);
        ScrollFrame sf=new ScrollFrame(this); sf.addView(box); root.addView(sf,new FrameLayout.LayoutParams(-1,-1));
        ImageView logo=new ImageView(this); logo.setImageResource(R.drawable.logo_mds); logo.setScaleType(ImageView.ScaleType.FIT_CENTER); box.addView(logo,new LinearLayout.LayoutParams(-1,92));
        if(p==1){ box.addView(tv("AMALAN BAIAT",24,true)); box.addView(tv("AMALAN-AMALAN BAGI YANG AKAN MENGIKUTI BAI’AT THORIQOH SYADZILIYYAH",18,true)); box.addView(tv("1.Menjalankan Puasa Tiga hari, dimulai pada hari Selasa sampai pada hari Kamis, buka puasa terakhir menanti setelah dibai’at oleh Mursyidnya. Selama menjalankan Puasa tidak boleh makan makanan yang bernyawa (Telor, Ikan, daging dll).",17,false)); }
        else if(p==2) box.addView(tv("Malam Selasa ( hari Pertama akan menjalankan Puasa ) sampai dengan malam Kamis, mengamalkan atau membaca amalan-amalan sebagai berikut :",19,false));
        else if(p==3) sholatHajatCounter(box);
        else if(p==4) box.addView(tv("Setelah Selesai menjalankan sholat (Posisi duduk tetap seperti duduk tahiyat akhir, kemudian membaca wirid :",19,false));
        else if(p==5) dzikir(box,"membaca surat al ikhlas 50 kali","بِسْمِ اللّٰهِ الرَّحْمٰنِ الرَّحِيْمِ قُلْ هُوَ اللّٰهُ اَحَدٌۚاَللّٰهُ\nالصَّمَدُۚلَمْ يَلِدْ وَلَمْ يُوْلَدْۙوَلَمْ يَكُنْ لَّهُ كُفُوًا اَحَدٌۙ","Bismillahirrahmanirrahim.Qul huwallahu ahad.Allahu sh-shamad.Lam yalid wa lam yulad.Wa lam yakul lahu kufuwan ahad.",50);
        else if(p==6) dzikir(box,"membaca sholawat 50 kali","اللهم صلِّ على سيِّدنا محمَّد، وعلى آل سيِّدنا محمَّد","Allohumma Sholli 'ala sayyidina Muhammad, wa 'alaa aali sayyidina Muhammad.",50);
        else if(p==7) dzikir(box,"membaca istighfar 50 kali","أَسْتَغْفِرُ اللهَ الْعَظِيمَ","Astaghfirulloh hal adhiim",50);
        else if(p==8) dzikir(box,"membaca 50 kali","لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللهِ الْعَلِيِّ الْعَظِيمِ","Laa haula wa laa quwwata, illabillahil 'aliyil adhim",50);
        else if(p==9) box.addView(tv("Kemudian Sujud Taubat Membaca 50 kali\n\nلَا إِلٰهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ\n\nLaa ilaaha illa anta .subhaanaka, innikuntu minad dhoolimiin.\n\nsetelah selasai kembali duduk.",19,false));
        else if(p==10) dzikir(box,"Setelah kembali duduk, DiLanjut Wirid Di Bawah Ini :\n\nإِلَى حَضْرَةِ النَّبِيِّ الْمُصْطَفَى صَلَّى اللهُ عَلَيْهِ وَسَلَّمَ\n\nilla hadrotin nabiyyil mushthofa Shollallohu 'alaihi wassalam\n\nkemudian membaca alfatihah 10 kali.","","",10);
        else if(p==11) dzikir(box,"إِلَى حَضْرَةِ الْإِمَامِ أَبِي الْحَسَنِ عَلِيِّ الشَّاذِلِيِّ وَإِلَى شَيْخِي وَمُرَبِّي الشَّيْخِ أَحْمَدْ وَافِي مَيْمُونْ : الْفَاتِحَة\n\nilla hadrotil imam abil hasan 'ali asy syadzili wa illa syaikhi wa murobbii, syaikh ahmad wafi maimoen\n\nkemudian membaca alfatihah 10 kali","","",10);
        else if(p==12) dzikir(box,"membaca surat al ikhlas 30 kali","بِسْمِ اللّٰهِ الرَّحْمٰنِ الرَّحِيْمِ قُلْ هُوَ اللّٰهُ اَحَدٌۚاَللّٰهُ\nالصَّمَدُۚلَمْ يَلِدْ وَلَمْ يُوْلَدْۙوَلَمْ يَكُنْ لَّهُ كُفُوًا اَحَدٌۙ","Bismillahirrahmanirrahim.Qul huwallahu ahad.Allahu sh-shamad.Lam yalid wa lam yulad.Wa lam yakul lahu kufuwan ahad.",30);
        else if(p==13) dzikir(box,"membaca surat al falaq10 kali","بِسْمِ اللّٰهِ الرَّحْمٰنِ الرَّحِيْمِ قُلْ اَعُوْذُ بِرَبِّ الْفَلَقِۙ.مِنْ شَرِّ مَا خَلَقَۙ.وَمِنْ شَرِّ غَاسِقٍ اِذَا وَقَبَۙ.وَمِنْ شَرِّ النَّفّٰثٰتِ فِى الْعُقَدِۙ.وَمِنْ شَرِّ حَاسِدٍ اِذَا حَسَدَ","Bismillahirrahmanirrahim.Qul a'udzu birabbil falaq.Min syarri ma khalaq.Wa min syarri ghasiqin idza waqab.Wa min syarrin-naffatsati fil 'uqad.Wa min syarri hasidin idza hasad",10);
        else if(p==14) dzikir(box,"membaca surat annas 10 kali","بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ.قُلْ أَعُوذُ بِرَبِّ النَّاسِ .مَلِكِ النَّاسِ.إِلَٰهِ النَّاسِ.مِنْ شَرِّ الْوَسْوَاسِ الْخَنَّاسِ.الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ مِنَ مِنَ الْجِنَّةِ وَالنَّاسِ","Bismillahirrahmaanirrahiim. Qul a'udzu birabbin-nas. Malikin-nas. Ilahin-nas. Min syarril-waswasil-khannas. Alladzii yuwaswisu fii shuduurin-nas. Minal-jinnati wan-nas.",10);
        else if(p==15) dzikir(box,"Kemudian Berdo’a :\n\n(223x) يَا حَيُّ يَا قَيُّومُ\n\nyaa Hayyu yaa qoyyum 223x","","",223);
        else if(p==16) dzikir(box,"أَخِي قَلْبِي بِنُورِكَ أَقِمْنِي لِشُهُودِكَ وَعَرِّفْنِي الطَّرِيقَ إِلَيْكَ 3x\n\nAhyiinqolbii binuurika, wa aqimnii lisyuhuudika, wa 'arifnii thoriqo ilaika 3x","","",3);
        else if(p==17) box.addView(tv("Setelah Semuanya Selesai, Apabila Akan Tidur (Posisi Kepala di Utara Wajah Menghadap Kiblat). Mohon Kepada Allah SWT Semoga Diberi Impian / Mimpi Yang Bagus.",19,false));
        else if(p==18){ box.addView(tv("ALHAMDULILLAH",26,true)); box.addView(tv("Amalan sudah selesai.\n\nsemoga diberi tetapnya iman, hati yang terang, dan selamat dunia akherat. aamiin.",19,false)); }
        addNav(p);
        fade(sf);
    }

    void sholatHajatCounter(LinearLayout box){
        // Teks asli halaman Sholat Hajat dipertahankan.
        box.addView(tv("Menjalankan Sholat Hajat 4 Roka’at ( dua kali salam ) Bacaanya :\nRoka’at 1, Fatihah kemudian Surat Ikhlas 10 Kali\nRoka’at 2, Fatihah kemudian Surat Ikhlas 20 Kali\nRoka’at 3, Fatihah kemudian Surat Ikhlas 30 Kali\nRoka’at 4, Fatihah kemudian Surat Ikhlas 40 Kali.",18,false));

        TextView rakaatInfo=tv("Raka’at saat ini: Raka’at 1",19,true);
        box.addView(rakaatInfo);

        TextView counter=tv("0 / 10",28,true);
        LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,64);
        cp.setMargins(0,dp(8),0,dp(12));
        box.addView(counter,cp);

        Button hitung=btn("HITUNG");
        hitung.setTextSize(16);
        LinearLayout.LayoutParams hp=new LinearLayout.LayoutParams(-1,84);
        hp.setMargins(dp(20),dp(16),dp(20),dp(34));
        box.addView(hitung,hp);

        final int[] rakaat={1};
        final int[] target={10};
        final int[] hitungan={0};

        hitung.setOnClickListener(v -> {
            if(rakaat[0] > 4) return;

            hitungan[0]++;

            if(hitungan[0] >= target[0]){
                if(rakaat[0] == 4){
                    hitungan[0]=target[0];
                    counter.setText(target[0] + " / " + target[0]);
                    rakaatInfo.setText("Raka’at saat ini: SELESAI");
                    hitung.setText("SELESAI");
                    hitung.setEnabled(false);
                    hitung.setAlpha(0.7f);
                } else {
                    hitungan[0]=0;
                    rakaat[0]++;
                    target[0]=rakaat[0] * 10;
                    rakaatInfo.setText("Raka’at saat ini: Raka’at " + rakaat[0]);
                    counter.setText("0 / " + target[0]);
                }
            } else {
                counter.setText(hitungan[0] + " / " + target[0]);
            }
        });
    }

    void dzikir(LinearLayout box,String title,String arab,String latin,int target){
        box.addView(tv(title,19,true));
        if(!arab.isEmpty()) { TextView a=tv(arab,24,false); a.setTypeface(Typeface.create("sans",Typeface.NORMAL)); box.addView(a); }
        if(!latin.isEmpty()) box.addView(tv(latin,17,false));
        TextView hint=tv("MULAI MEMBACA",15,true); box.addView(hint);
        TextView counter=tv("0 / "+target,28,true);
        LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,64);
        cp.setMargins(0,dp(10),0,dp(18));
        box.addView(counter,cp);
        Button tap=btn("Ketuk disini");
        tap.setTextSize(16);
        LinearLayout.LayoutParams tp=new LinearLayout.LayoutParams(-1,84);
        tp.setMargins(dp(20),dp(16),dp(20),dp(34));
        box.addView(tap,tp);
        tap.setOnClickListener(v->{ if(count<target){ count++; counter.setText(count+" / "+target); if(count>=target){ hint.setText("SELESAI — LANJUTKAN"); tap.setEnabled(false); }} });
    }

    void addNav(int p){
        LinearLayout nav=new LinearLayout(this);
        nav.setOrientation(LinearLayout.HORIZONTAL);
        nav.setGravity(Gravity.CENTER_VERTICAL);
        nav.setPadding(dp(10),dp(6),dp(10),dp(6));
        nav.setBackgroundColor(BUTTON);

        Button back=btn("KEMBALI");
        Button next=btn(p==18?"SELESAI":"LANJUTKAN");
        back.setTextSize(15); next.setTextSize(15);
        back.setMinHeight(dp(52)); next.setMinHeight(dp(52));

        LinearLayout.LayoutParams blp=new LinearLayout.LayoutParams(0,dp(54),1);
        nav.addView(back,blp);
        LinearLayout.LayoutParams nlp=new LinearLayout.LayoutParams(0,dp(54),1);
        nlp.setMargins(dp(12),0,0,0);
        nav.addView(next,nlp);

        if(p==1) {
            back.setEnabled(false);
            back.setAlpha(0.55f);
        } else {
            back.setOnClickListener(v->{
                if(p==2) showPage(1);
                else if(p>=3) showPage(p-1);
            });
        }

        if(p==18) next.setOnClickListener(v->finishAffinity());
        else next.setOnClickListener(v->showPage(p+1));

        // Reserve a real area for the bottom navigation so it can NEVER
        // overlap the counter or the counting button, even after scrolling.
        final int navHeight=dp(70);
        final int navBottom=dp(18);
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(
                -1, navHeight, Gravity.BOTTOM);
        lp.setMargins(dp(8),0,dp(8),navBottom);
        root.addView(nav,lp);

        View child=root.getChildAt(0);
        if(child instanceof ScrollFrame){
            ScrollFrame sf=(ScrollFrame)child;
            FrameLayout.LayoutParams sfp=(FrameLayout.LayoutParams)sf.getLayoutParams();
            sfp.width=-1;
            sfp.height=-1;
            sfp.leftMargin=0;
            sfp.rightMargin=0;
            sfp.topMargin=0;
            sfp.bottomMargin=navHeight+navBottom+dp(16);
            sf.setLayoutParams(sfp);
            sf.setPadding(dp(18),dp(10),dp(18),dp(20));
            sf.setClipToPadding(true);
        }
    }

    void addBottomButton(String text, boolean left, Runnable action){
        Button b=btn(text);
        b.setTextSize(15);
        b.setOnClickListener(v->action.run());
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(
                dp(190),dp(58),Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
        lp.bottomMargin=dp(58);
        root.addView(b,lp);
    }

    void hideSystemNavigation(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    int dp(int value){
        return (int)(value * getResources().getDisplayMetrics().density + 0.5f);
    }

    static class ScrollFrame extends android.widget.ScrollView { ScrollFrame(android.content.Context c){ super(c); setFillViewport(true); setClipToPadding(false); } }
}
