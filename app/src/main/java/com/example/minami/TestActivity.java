package com.example.minami;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private Button know,unknow;
    private TextView word;
    int i = 0;
    int truenum,wrongnum =0;

    private TextToSpeech mtts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        intitts();

        know = findViewById(R.id.btn_test_know);
        unknow = findViewById(R.id.btn_test_unknow);
        word = findViewById(R.id.tv_test_word);

        int[] ran_group = randomCommon(0,cet_4_en.length,21);
        int[] ran_group2 = randomCommon(0,cet_4_en.length,21);

        word.setText(cet_4_en[ran_group[0]]);
        sound(cet_4_en[ran_group[0]]);
        know.setText(cet_4_cn[ran_group[0]]);
        unknow.setText(cet_4_cn[ran_group2[0]]);

        know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i =i+1;
                truenum = truenum +1;
                know.setText(cet_4_cn[ran_group[i]]);
                unknow.setText(cet_4_cn[ran_group2[i]]);
                if(i+1 == ran_group.length){
                    int gg = truenum*8000/20;
                    String h = String.valueOf(gg);
                    mydialog(h);
                    i=0;
                    truenum=0;
                }else {
                    word.setText(cet_4_en[ran_group[i]]);
                    sound(cet_4_en[ran_group[i]]);
                }
            }
        });

        unknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i =i+1;
                wrongnum = wrongnum +1;
                know.setText(cet_4_cn[ran_group[i]]);
                unknow.setText(cet_4_cn[ran_group2[i]]);
                if(i+1 == ran_group.length){
                    int gg =8000-wrongnum*8000/20;
                    String h = String.valueOf(gg);
                    mydialog(h);
                    i=0;
                    wrongnum=0;
                }else {
                    word.setText(cet_4_en[ran_group[i]]);
                    sound(cet_4_en[ran_group[i]]);
                }
            }
        });

    }

    private void sound(String text){

        mtts.speak(text, TextToSpeech.QUEUE_FLUSH,null);
    }

    private void intitts(){
        mtts = new TextToSpeech(this,this);
        mtts.setPitch(0.7f);
        mtts.setSpeechRate(1.0f);

    }

    private void mydialog(String hello){
        AlertDialog aldg;
        AlertDialog.Builder adBd=new AlertDialog.Builder(TestActivity.this);
        adBd.setTitle("你掌握的词汇量约为");

        adBd.setMessage(hello);
        adBd.setPositiveButton("返回主界面", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                intent = new Intent(TestActivity.this, InterfaceActivity.class);
                startActivity(intent);
            }
        });

        aldg=adBd.create();
        aldg.show();
    }

    public int[] randomCommon(int min, int max, int n){
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while(count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if(num == result[j]){
                    flag = false;
                    break;
                }
            }
            if(flag){
                result[count] = num;
                count++;
            }
        }
        return result;
    }

    private String[] cet_4_en= {"cube",
            "submerge",
            "credit",
            "surrounding",
            "stove",
            "submit",
            "carrier",
            "imply",
            "strain",
            "consist",
            "strap",
            "efficient",
            "accommodation",
            "strategic",
            "layer",
            "exclaim",
            "representative",
            "forecast",
            "discipline",
            "neutral",
            "interpret",
            "knot",
            "desirable",
            "promote",
            "acceptance",
            "mayor",
            "equation",
            "routine",
            "ripe",
            "prove",
            "likewise",
            "chap",
            "explore",
            "overnight",
            "strategy",
            "straw",
            "bind",
            "stream",
            "bearing",
            "suppose",
            "access",
            "remain",
            "abstract",
            "stretch",
            "approximate",
            "striking",
            "abuse",
            "critic",
            "interpretation",
            "string",
            "illustrate",
            "helpful",
            "leak",
            "accountant",
            "crude",
            "product",
            "strip",
            "stripe",
            "communicate",
            "following",
            "hedge",
            "consumer",
            "emotional",
            "craft",
            "institute",
            "indispensable",
            "scheme",
            "scale",
            "replace",
            "bark"};

    private String[] cet_4_cn={"立方体；立方",
            "浸没，潜入水中",
            "信贷，赊账；赞扬，荣誉；学分；信任",
            "周围的事物，环境",
            "炉，火炉，电炉",
            "屈从，听从，服从；呈送，提交；主张，建议",
            "运输工具，运载工具；带菌者；载重架，置物架",
            "暗示，意指",
            "拉紧；过劳；极度紧张；张力；扭伤，拉伤；旋律；品种，家系；气质，个性特点",
            "由…组成；在于；一致",
            "带子",
            "效率高的；有能力的",
            "住处，膳宿",
            "对全局有重要意义的，关键的；战略上的",
            "层，层次",
            "呼喊，惊叫",
            "代表，代理人",
            "预测，预报",
            "纪律；训练；惩罚；学科",
            "中立的，中性的",
            "解释，说明，口译",
            "（绳子的）结，（树的）节",
            "值得向往的；可取的",
            "促进，增进，发扬；提升；宣传，推销",
            "接受，承认；容忍",
            "市长",
            "方程式，等式",
            "例行公事，惯例，惯常的程序",
            "熟的，时机成熟的",
            "证明；结果是；证实",
            "同样地；也，又",
            "小伙子，男人，家伙",
            "探险，探索；仔细查阅，探究",
            "在整个夜里；在短时间内，突然",
            "战略，策略",
            "稻草；麦秆吸管，吸管",
            "捆绑，包扎，装订；约束；使结合，使黏合",
            "河，溪流；一股，一串",
            "举止，风度；方位，方向感；轴承",
            "猜想，假定，让",
            "接近；通道，入口",
            "剩下，余留，保持；仍然是",
            "抽象的，抽象派的",
            "伸展，延续；伸长，拉长；使倾注全力；使紧张",
            "近似的",
            "显著的，突出的；惹人注目的，容貌出众的",
            "滥用；虐待，伤害；辱骂，毁谤",
            "批评家，爱挑剔的人",
            "解释，口译",
            "线，细绳；一串，一行",
            "说明",
            "给予帮助的，有用的",
            "漏，泄露",
            "会计人员，会计师",
            "简陋的，天然的；粗鲁的，粗俗的",
            "产品，产物；乘积",
            "脱光衣服；剥夺，夺去",
            "条纹",
            "通讯，交际，交流；连接，相通；传达，传播；传染",
            "接着的，下列的",
            "篱笆，树篱，障碍物",
            "消费者，用户，消耗者",
            "感情的，情绪的",
            "工艺，手艺；船，航空器",
            "研究所，学院",
            "必不可少的，必需的",
            "计划，方案；阴谋",
            "大小，规模；等级，级别",
            "代替，取代；更换，调换；把…放回原处",
            "吠，叫"};

    @Override
    public void onInit(int status) {

    }
}