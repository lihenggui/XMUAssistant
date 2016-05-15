package com.merxury.xmuassistant;

import java.util.HashMap;

/**
 * Created by lihen on 2016/5/15.
 * 存储了所有校区宿舍的名字
 */
public class Room {
    // 包括漳州校区
    // public static final String[] YuanQu = { "本部芙蓉区", "本部石井区", "本部南光区",
    // "本部凌云区", "本部勤业区", "本部海滨新区", "本部丰庭区", "海韵学生公寓",
    // "曾厝安学生公寓", "漳州校区博学园", "漳州校区囊萤园", "漳州校区笃行园", "漳州校区映雪园", "漳州校区勤业园",
    // "漳州校区芙蓉园", "漳州校区若谷园", "漳州校区凌云园",
    // "漳州校区丰庭园", "漳州校区南安园", "漳州校区南光园", "漳州校区嘉庚若谷园", "翔安校区芙蓉区", "翔安校区南安区",
    // "翔安校区南光区" };
    // public static final String[] YuanQuCode = { "01", "02", "03", "04", "05",
    // "06", "07", "08", "09", "21", "22", "23",
    // "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35" };

    // 不包括漳州校区
    public static final String[] YuanQu = {"本部芙蓉区", "本部石井区", "本部南光区", "本部凌云区", "本部勤业区", "本部海滨新区", "本部丰庭区", "海韵学生公寓",
            "曾厝安学生公寓", "翔安校区芙蓉区", "翔安校区南安区", "翔安校区南光区"};
    public static final String[] YuanQuCode = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "33", "34", "35"};

    // 本部
    public static final String[] BengBu_FuRong = {"芙蓉1", "芙蓉2", "芙蓉3", "芙蓉4", "芙蓉5", "芙蓉6", "芙蓉7", "芙蓉8", "芙蓉9",
            "芙蓉10", "芙蓉11", "芙蓉12", "芙蓉13"};
    public static final String[] BengBu_FuRongCode = {"01", "02", "03", "04", "05", "06", "09", "10", "11", "07",
            "13", "12", "08"};

    public static final String[] BengBu_ShiJing = {"石井1", "石井2", "石井3", "石井4", "石井5", "石井6", "石井7"};
    public static final String[] BengBu_ShiJingCode = {"01", "02", "03", "04", "05", "06", "07"};

    public static final String[] BengBu_NanGuang = {"南光4", "南光7", "综合楼"};
    public static final String[] BengBu_NanGuangCode = {"14", "15", "16"};

    public static final String[] BengBu_LingYun = {"凌云1", "凌云2", "凌云3", "凌云4", "凌云5", "凌云6", "凌云7", "凌云8", "凌云9",
            "凌云10", "东村"};
    public static final String[] BengBu_LingYunCode = {"01", "02", "03", "12", "13", "06", "07", "08", "09", "10",
            "14"};

    public static final String[] BengBu_QinYe = {"勤业4", "勤业6", "勤业7"};
    public static final String[] BengBu_QinYeCode = {"15", "06", "07"};

    public static final String[] BengBu_Haibing = {"新区一", "新区二", "新区三"};
    public static final String[] BengBu_HaibingCode = {"17", "18", "16"};

    public static final String[] BengBu_FengTing = {"丰庭1", "丰庭2", "丰庭4", "丰庭5", "笃行3"};
    public static final String[] BengBu_FengTingCode = {"01", "02", "04", "05", "06"};

    // 海韵
    public static final String[] HaiYun = {"海韵8", "海韵9", "海韵10", "海韵11", "海韵12", "海韵13", "海韵14", "海韵15"};
    public static final String[] HaiYunCode = {"08", "09", "10", "11", "12", "13", "14", "15"};

    public static final String[] ZengCuoAn = {"公寓1", "公寓2", "公寓3", "公寓4", "公寓5", "公寓6", "公寓7", "公寓8"};
    public static final String[] ZengCuoAnCode = {"01", "02", "03", "04", "05", "06", "07", "08"};

    // 翔安
    public static final String[] XiangAn_FuRong = {"翔安芙蓉1", "翔安芙蓉2", "翔安芙蓉3", "翔安芙蓉4", "翔安芙蓉5", "翔安芙蓉6", "翔安芙蓉7",
            "翔安芙蓉8", "翔安芙蓉9", "翔安芙蓉10"};
    public static final String[] XiangAn_FuRongCode = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10"};

    public static final String[] XiangAn_NanAn = {"翔安南安1", "翔安南安2", "翔安南安3", "翔安南安4", "翔安南安5", "翔安南安6", "翔安南安7",
            "翔安南安8", "翔安南安9", "翔安南安10", "翔安南安11", "翔安南安12", "翔安南安13"};
    public static final String[] XiangAn_NanAnCode = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13"};

    public static final String[] XiangAn_NanGuang = {"翔安南光1", "翔安南光2", "翔安南光3", "翔安南光4", "翔安南光5", "翔安南光6", "翔安南光7",
            "翔安南光8"};
    public static final String[] XiangAn_NanGuangCode = {"01", "02", "03", "04", "05", "06", "07", "08"};

    // 漳校
    // public static final String[] ZhangZhou_BoXue = { "博学1", "博学2", "博学3" };
    // public static final String[] ZhangZhou_BoXueCode = { "01", "02", "03" };
    //
    // public static final String[] ZhangZhou_NangYing = { "囊萤1", "囊萤2", "囊萤3"
    // };
    // public static final String[] ZhangZhou_NangYingCode = { "01", "02", "03"
    // };
    //
    // public static final String[] ZhangZhou_DuXing = { "笃行1", "笃行2" };
    // public static final String[] ZhangZhou_DuXingCode = { "01", "02" };
    //
    // public static final String[] ZhangZhou_YingXue = { "映雪1", "映雪2", "映雪3" };
    // public static final String[] ZhangZhou_YingXueCode = { "01", "02", "03"
    // };
    //
    // public static final String[] ZhangZhou_QinYe = { "勤业1", "勤业2", "勤业3",
    // "勤业4", "勤业5", "勤业6", "勤业7" };
    // public static final String[] ZhangZhou_QinYeCode = { "01", "02", "03",
    // "04", "05", "06", "07" };
    //
    // public static final String[] ZhangZhou_FuRong = { "芙蓉1", "芙蓉2", "芙蓉3",
    // "芙蓉4", "芙蓉5" };
    // public static final String[] ZhangZhou_FuRongCode = { "01", "02", "03",
    // "04", "05" };
    //
    // public static final String[] ZhangZhou_RuoGu = { "若谷1", "若谷2", "若谷3" };
    // public static final String[] ZhangZhou_RuoGuCode = { "01", "02", "03" };
    //
    // public static final String[] ZhangZhou_LingYun = { "凌云1", "凌云2", "凌云3" };
    // public static final String[] ZhangZhou_LingYunCode = { "01", "02", "03"
    // };
    //
    // public static final String[] ZhangZhou_FengTing = { "丰庭1", "丰庭2", "丰庭4",
    // "丰庭5", "丰庭6", "丰庭7", "丰庭8", "丰庭9", "丰庭10",
    // "丰庭11", "丰庭12", "丰庭13" };
    // public static final String[] ZhangZhou_FengTingCode = { "01", "02", "03",
    // "04", "05", "06", "07", "08", "09", "10",
    // "11", "12" };
    //
    // public static final String[] ZhangZhou_NanAn = { "南安1", "南安2", "南安3",
    // "南安4", "南安5", "南安6", "南安7" };
    // public static final String[] ZhangZhou_NanAnCode = { "01", "02", "03",
    // "04", "05", "06", "07" };
    //
    // public static final String[] ZhangZhou_NanGuang = { "南光1", "南光2", "南光3",
    // "南光4", "南光5", "南光6", "南光7", "南光8", "南光9",
    // "南光10", "南光11", "南光12", "南光13" };
    // public static final String[] ZhangZhou_NanGuangCode = { "01", "02", "03",
    // "04", "05", "06", "07", "08", "09", "10",
    // "11", "12", "13" };
    //
    // public static final String[] ZhangZhou_JiaGengRuoGu = { "若谷贰", "若谷叁" };
    // public static final String[] ZhangZhou_JiaGengRuoGuCode = { "02", "03" };

    //
    public static HashMap<String, String[]> yuanqu2Louming = new HashMap<String, String[]>();
    public static HashMap<String[], String[]> loumingMap = new HashMap<String[], String[]>();

    static {
        yuanqu2Louming.put("本部芙蓉区", BengBu_FuRong);
        yuanqu2Louming.put("本部石井区", BengBu_ShiJing);
        yuanqu2Louming.put("本部南光区", BengBu_NanGuang);
        yuanqu2Louming.put("本部凌云区", BengBu_LingYun);
        yuanqu2Louming.put("本部勤业区", BengBu_QinYe);
        yuanqu2Louming.put("本部海滨新区", BengBu_Haibing);
        yuanqu2Louming.put("本部丰庭区", BengBu_FengTing);
        yuanqu2Louming.put("海韵学生公寓", HaiYun);
        yuanqu2Louming.put("曾厝安学生公寓", ZengCuoAn);
        yuanqu2Louming.put("翔安校区芙蓉区", XiangAn_FuRong);
        yuanqu2Louming.put("翔安校区南安区", XiangAn_NanAn);
        yuanqu2Louming.put("翔安校区南光区", XiangAn_NanGuang);
        // yuanqu2Louming.put("漳州校区博学园", ZhangZhou_BoXue);
        // yuanqu2Louming.put("漳州校区囊萤园", ZhangZhou_NangYing);
        // yuanqu2Louming.put("漳州校区笃行园", ZhangZhou_DuXing);
        // yuanqu2Louming.put("漳州校区映雪园", ZhangZhou_YingXue);
        // yuanqu2Louming.put("漳州校区勤业园", ZhangZhou_QinYe);
        // yuanqu2Louming.put("漳州校区芙蓉园", ZhangZhou_FuRong);
        // yuanqu2Louming.put("漳州校区若谷园", ZhangZhou_RuoGu);
        // yuanqu2Louming.put("漳州校区凌云园", ZhangZhou_LingYun);
        // yuanqu2Louming.put("漳州校区丰庭园", ZhangZhou_FengTing);
        // yuanqu2Louming.put("漳州校区南安园", ZhangZhou_NanAn);
        // yuanqu2Louming.put("漳州校区南光园", ZhangZhou_NanGuang);
        // yuanqu2Louming.put("漳州校区嘉庚若谷园", ZhangZhou_JiaGengRuoGu);

        //
        loumingMap.put(BengBu_FuRong, BengBu_FuRongCode);
        loumingMap.put(BengBu_ShiJing, BengBu_ShiJingCode);
        loumingMap.put(BengBu_NanGuang, BengBu_NanGuangCode);
        loumingMap.put(BengBu_LingYun, BengBu_LingYunCode);
        loumingMap.put(BengBu_QinYe, BengBu_QinYeCode);
        loumingMap.put(BengBu_Haibing, BengBu_HaibingCode);
        loumingMap.put(BengBu_FengTing, BengBu_FengTingCode);
        loumingMap.put(HaiYun, HaiYunCode);
        loumingMap.put(ZengCuoAn, ZengCuoAnCode);
        loumingMap.put(XiangAn_FuRong, XiangAn_FuRongCode);
        loumingMap.put(XiangAn_NanAn, XiangAn_NanAnCode);
        loumingMap.put(XiangAn_NanGuang, XiangAn_NanGuangCode);
//		loumingMap.put(ZhangZhou_BoXue, ZhangZhou_BoXueCode);
//		loumingMap.put(ZhangZhou_NangYing, ZhangZhou_NangYingCode);
//		loumingMap.put(ZhangZhou_DuXing, ZhangZhou_DuXingCode);
//		loumingMap.put(ZhangZhou_YingXue, ZhangZhou_YingXueCode);
//		loumingMap.put(ZhangZhou_QinYe, ZhangZhou_QinYeCode);
//		loumingMap.put(ZhangZhou_FuRong, ZhangZhou_FuRongCode);
//		loumingMap.put(ZhangZhou_RuoGu, ZhangZhou_RuoGuCode);
//		loumingMap.put(ZhangZhou_LingYun, ZhangZhou_LingYunCode);
//		loumingMap.put(ZhangZhou_FengTing, ZhangZhou_FengTingCode);
//		loumingMap.put(ZhangZhou_NanAn, ZhangZhou_NanAnCode);
//		loumingMap.put(ZhangZhou_NanGuang, ZhangZhou_NanGuangCode);
//		loumingMap.put(ZhangZhou_JiaGengRuoGu, ZhangZhou_JiaGengRuoGuCode);

    }
}

