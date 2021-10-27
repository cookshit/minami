 package com.example.minami;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

 public class LearnWordActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

     public static UserHelp userHelp;
     public static WordHelp wordHelp2 = new WordHelp();
     private MyOpenHelp myOpenHelp;

     private Button pre_word;
     private Button next_word;
     private Button speak;
     private TextView cn,en;

     private int learn_number ;

     private TextToSpeech mtts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learnword);

        myOpenHelp = userHelp.getOpenHelp();
        intitts();

        mtts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == mtts.SUCCESS) {
                    int result = mtts.setLanguage(Locale.ENGLISH);
                    if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
                            && result != TextToSpeech.LANG_AVAILABLE){
                        Toast.makeText(LearnWordActivity.this, "TTS暂时不支持这种语音的朗读！",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        pre_word = findViewById(R.id.danci_pre);
        next_word = findViewById(R.id.danci_next);
        speak = findViewById(R.id.learn_btn_speak);
        cn=findViewById(R.id.tv_learn_cn);
        en=findViewById(R.id.tv_learn_en);

        learn_number = query_learn_num();//从数据库中读取计划学习数
        initbook();//初始化词库

        //en.setText(booken[query()]);
        en.setText(wordHelp2.getCet4en()[query()]);
        cn.setText(wordHelp2.getCet4cn()[query()]);
        //cn.setText(bookcn[query()]);

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound(wordHelp2.getCet4en()[query()]);
            }
        });

        pre_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_pre();
            }
        });

        next_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_next();
            }
        });
    }



    /*函数名：btn_pre()
    *功能描述：1.显示上一个单词
    *         2.当显示的单词已经是词库中的第一个单词时进行提示
    *         3.记录学习进度，在下次学习时直接跳到上一次学习到的单词
    * */
     public void btn_pre(){
         int i =query()-1;
         if(i<0){
             i=0;
             Toast.makeText(getApplicationContext(),"已经是第一个单词了",Toast.LENGTH_SHORT).show();
         }
         else {
             en.setText(wordHelp2.getCet4en()[i]);
             cn.setText(wordHelp2.getCet4cn()[i]);
             sound(wordHelp2.getCet4en()[i]);
             dbalter(i,userHelp.getUsername());
         }

     }


     /*函数名：btn_next()
      *功能描述：1.显示下一个单词
      *         2.当显示的单词已经是词库中的最后一个单词时进行提示
      *         3.记录学习进度，在下次学习时直接跳到上一次学习到的单词
      *         4.已学习单词数量达到计划学习单词数量时界面按钮变更并跳转到复习单词界面
      * */
     public void btn_next(){
         int i =query()+1;
         if(i== wordHelp2.getCet4cn().length){
             i= wordHelp2.getCet4cn().length-1;
             Toast.makeText(getApplicationContext(),"已经是最后一个单词了",Toast.LENGTH_SHORT).show();
         }else if(i%learn_number==0&&i!=0){
             dbalter(i,userHelp.getUsername());
             Intent intent = null;
             intent = new Intent(LearnWordActivity.this,ReciteActivity.class);
             startActivity(intent);

         }else if(i%learn_number==(learn_number-1)){
             next_word.setText("开始复习所学单词！");
             pre_word.setVisibility(View.INVISIBLE);
             en.setText(wordHelp2.getCet4en()[i]);
             cn.setText(wordHelp2.getCet4cn()[i]);
             sound(wordHelp2.getCet4en()[i]);
             dbalter(i,userHelp.getUsername());
         }else {
             en.setText(wordHelp2.getCet4en()[i]);
             cn.setText(wordHelp2.getCet4cn()[i]);
             sound(wordHelp2.getCet4en()[i]);
             dbalter(i,userHelp.getUsername());
         }
     }


     /*函数名：intitts()
      *功能描述：1.初始化TTS，设置音调和语速
      *
      * */
     private void intitts(){
         mtts = new TextToSpeech(this,this);
         mtts.setPitch(0.7f);
         mtts.setSpeechRate(1.0f);

     }


     private void sound(String text){
         mtts.speak(text,TextToSpeech.QUEUE_FLUSH,null);
     }

     
     private void dbalter(int i,String username){
         SQLiteDatabase db = myOpenHelp.getWritableDatabase();
         ContentValues values = new ContentValues();

         values.put("flag",i);
         db.update("User",values,"name=?",new String[] {username});

         values.clear();
     }

     public int query(){

         SQLiteDatabase db = myOpenHelp.getWritableDatabase();
         Cursor cursor = db.query("User",new String[]{"flag","word_book"},"name=?",new String[]{userHelp.getUsername()},
                 null,null,null);
         cursor.moveToFirst();
         int flag= cursor.getInt(0);

         cursor.close();
         return flag;
     }

     public int querybook(){

         SQLiteDatabase db = myOpenHelp.getWritableDatabase();
         Cursor cursor = db.query("User",new String[]{"flag","word_book"},"name=?",new String[]{userHelp.getUsername()},
                 null,null,null);
         cursor.moveToFirst();
         int flag= cursor.getInt(1);

         cursor.close();
         return flag;
     }

     public int query_learn_num(){

         SQLiteDatabase db = myOpenHelp.getWritableDatabase();
         Cursor cursor = db.query("User",new String[]{"flag","word_book","learn_num"},"name=?",new String[]{userHelp.getUsername()},
                 null,null,null);
         cursor.moveToFirst();
         int flag= cursor.getInt(2);

         cursor.close();
         return flag;
     }

     public void initbook(){
         if(querybook()==1){
             wordHelp2.setCet4cn(bookcn1);
             wordHelp2.setCet4en(booken1);
         }else if(querybook()==2){
             wordHelp2.setCet4cn(bookcn2);
             wordHelp2.setCet4en(booken2);
         }else if(querybook()==3){
             wordHelp2.setCet4cn(bookcn3);
             wordHelp2.setCet4en(booken3);
         }else{

         }
     }

     public boolean onKeyDown(int keyCode, KeyEvent event) {
         if ((keyCode == KeyEvent.KEYCODE_BACK)) {
             Intent intent = null;
             intent = new Intent(LearnWordActivity.this, InterfaceActivity.class);
             startActivity(intent);
             return false;
         }else {
             return super.onKeyDown(keyCode, event);
         }
     }




     private String[] booken1 = {"gramme",
             "congress",
             "bump",
             "stroke",
             "ingredient",
             "arbitrary",
             "pinch",
             "exploit",
             "action",
             "ash",
             "rope",
             "bulk",
             "strengthen",
             "independent",
             "board",
             "recall",
             "studio",
             "grave",
             "eve",
             "formal",
             "absorb",
             "sensitive",
             "ability",
             "fairy",
             "talent",
             "comparison",
             "stuff",
             "brow",
             "infer",
             "invasion",
             "grand",
             "stress",
             "journalist",
             "supply",
             "penetrate",
             "subject",
             "pole",
             "raw",
             "embassy",
             "carpenter",
             "appropriate",
             "socialist",
             "protein",
             "enlarge",
             "inherit",
             "chemist",
             "conflict",
             "drain",
             "architecture",
             "charity",
             "entitle",
             "subsequent",
             "span",
             "pea",
             "instruct",
             "spite",
             "slender",
             "automobile",
             "behavior",
             "envy",
             "substance",
             "contest",
             "spit",
             "mutual",
             "dorm",
             "substantial",
             "meanwhile",
             "desire",
             "conviction"
     };

     private String[] bookcn1 ={"克",
             "代表大会；国会，议会",
             "撞，碰；颠簸着前进",
             "中风；一举，一次努力；划桨，划水；击，敲；报时的钟声；笔画，一笔；抚摸",
             "组成部分，成分，原料；要素，因素",
             "随心所欲的，专断的",
             "捏，拧",
             "剥削；利用；开拓",
             "行动；已做的事；作用，功能；情节",
             "灰",
             "绳，索",
             "物体；体积；大批",
             "加强，巩固",
             "独立的，自主的；无偏见的；不相关联的",
             "板，牌子；委员会，董事会；木板，纸板；伙食",
             "回忆起；召回，叫回；收回，撤销",
             "画室，摄影室；播音室，录音室，摄影棚",
             "j.严重的",
             "前夜，前夕，前一刻",
             "正式的，礼仪上的",
             "吸收；吸引…的注意，使全神贯注；把…并入，同化",
             "敏感的，灵敏的，神经过敏的；容易生气的；易受伤害的",
             "能力，本领；才能，才智",
             "小精灵，小仙子",
             "天才，才能，人才",
             "比较，对照；比拟，比喻",
             "材料，东西",
             "额，眉，眉毛",
             "推论，推断；猜想",
             "入侵，侵略，侵犯",
             "宏伟的，重大的；傲慢的，派头大的；绝佳的；全部的",
             "压力；强调，重要性；重音",
             "新闻工作者，新闻记者",
             "供给，供应",
             "透入，渗入，进入；刺穿；洞察，了解",
             "题目，学科；主语",
             "杆；极，磁极，电极",
             "自然状态的，未加工过的；未煮的，生的；未经分析的，原始的；生疏无知的，未经训练的；露肉而刺痛的",
             "大使馆；大使馆全体成员",
             "木工，木匠",
             "适当的，恰当的",
             "社会主义者",
             "蛋白质",
             "扩大，扩展，放大",
             "继承",
             "化学家，药剂师",
             "冲突，抵触；争论；战斗，战争",
             "排去，放水",
             "建筑学；建筑式样，建筑风格",
             "救济金，施舍物",
             "给…权利；给…题名",
             "随后的，后来的",
             "跨距，一段时间",
             "豌豆",
             "教，教育；指示，通知",
             "恶意；怨恨；不顾",
             "细长的，苗条的；微薄的，不足的",
             "汽车，机动车",
             "行为，举止，表现",
             "妒忌，羡慕；妒忌的对象",
             "物质；实质；大意，要旨；根据，理由",
             "竞赛，争夺",
             "吐唾沫，吐出",
             "互相的；共同的",
             "宿舍",
             "客观的，大量的；物质的；坚固的；实质的，真实的",
             "与此同时",
             "，要求n.愿望，欲望",
             "确信，坚定的信仰；说服，信服；定罪，判罪"
     };

     private String[] booken2 = {
             "definite",
             "cautious",
             "prayer",
             "nest",
             "domestic",
             "chest",
             "airline",
             "rebel",
             "satisfactory",
             "stem",
             "render",
             "object",
             "gardener",
             "shrink",
             "parade",
             "rumour",
             "rug",
             "establish",
             "primarily",
             "kindness",
             "breast",
             "sticky",
             "boost",
             "fund",
             "incredible",
             "abroad",
             "detective",
             "stiff",
             "stimulate",
             "fame",
             "consume",
             "accelerate",
             "lightning",
             "sting",
             "bound",
             "rouse",
             "cultivate",
             "material",
             "personnel",
             "display",
             "particle",
             "frog",
             "impression",
             "biology",
             "drunk",
             "barrier",
             "stock",
             "fisherman",
             "politician",
             "royal",
             "barber",
             "stocking",
             "delegate",
             "highlight",
             "depression",
             "signature",
             "atmosphere",
             "evaluate",
             "rescue",
             "personality",
             "latter",
             "parliament",
             "input",
             "partial",
             "loyalty",
             "calendar",
             "overlook",
             "debate",
             "stoop"
     };

     private String[] bookcn2 = {"明确的，肯定的",
             "十分小心的，谨慎的",
             "祷告，祈祷；祷文；祈求，祈望",
             "窝，穴v.筑巢",
             "本国的；家庭的；驯养的",
             "胸腔，胸膛；箱子",
             "航空公司；航线",
             "反叛分子，反对者",
             "令人满意的",
             "茎，树干；词干",
             "使得，致使；给予，提供；翻译",
             "实物，物体；目的，目标；对象，客体；宾语",
             "园丁，花匠",
             "使起皱，使收缩；退缩，畏缩",
             "游行，检阅",
             "谣言，谣传",
             "小地毯",
             "建立，设立；确立；证实",
             "首先；主要的",
             "仁慈，好意",
             "乳房，胸脯，胸膛",
             "黏性的；（天气）湿热的",
             "提高，增加；推动，激励；替…做广告，宣扬",
             "资金，基金",
             "不能相信的，不可信的；难以置信的，不可思议的，惊人的",
             "到国外；在传播，在流传",
             "侦探，私人侦探",
             "硬的，僵直的；不灵活的；拘谨的，生硬的；费劲的；（风等）强烈的",
             "刺激，激励，激发",
             "名声，名望",
             "消费；吃完，喝光；毁灭；使入迷",
             "使加快，使增速",
             "闪电",
             "刺，蛰；刺痛，使痛苦；激怒",
             "一定的；有义务的",
             "惊起，唤起，唤醒",
             "耕，种植；培养；陶冶；发展",
             "材料，素材",
             "人员，员工",
             "陈列，展示；显示",
             "粒子，微粒，颗粒；小品词，语助词",
             "蛙",
             "印象，印记",
             "生物学，生态学",
             "醉的，陶醉的",
             "栅栏；检票口；屏障；障碍，隔阂",
             "原料，库存品；股本，公债；世系，血统；汤汁；家畜，牲畜",
             "渔民，渔夫",
             "政治家，政客",
             "王室的，皇家的",
             "理发师",
             "长筒袜",
             "代表，代表团成员",
             "强调，突出，使显著",
             "抑郁，沮丧；不景气，萧条；洼地，凹陷",
             "署名，签名，签字",
             "大气，空气；气氛，环境",
             "评价，估…的价",
             "营救，救援",
             "人格，个性；人物，名人",
             "后者的，后一半的",
             "议会，国会",
             "输入；投入的资金；输入的数据",
             "部分的；不公平的；偏爱的，偏袒的",
             "忠诚，忠心",
             "日历，历书，历法",
             "忽视；宽恕；俯瞰",
             "争论，辩论",
             "弯身n.弯腰，曲背"
     };

     private String[] booken3 = {"cube",
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

     private String[] bookcn3 ={"立方体；立方",
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