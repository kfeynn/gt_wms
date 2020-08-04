package com.grand.gt_wms.service;


import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.android.rfid.DevSettings;
import com.grand.gt_wms.R;
import com.grand.gt_wms.bean.AKbox;
import com.grand.gt_wms.bean.BKbox;
import com.grand.gt_wms.bean.Box;
import com.grand.gt_wms.bean.ImgsBox;
import com.grand.gt_wms.bean.ImptAndEmptItem;
import com.grand.gt_wms.bean.IqcItem;
import com.grand.gt_wms.bean.PKbox;
import com.grand.gt_wms.bean.PN;
import com.grand.gt_wms.bean.CKbox;
import com.grand.gt_wms.bean.PNSub;
import com.grand.gt_wms.bean.RnBox;
import com.grand.gt_wms.bean.User;
import com.grand.gt_wms.bean.WhrBox;
import com.grand.gt_wms.module.ApartManagerService;
import com.grand.gt_wms.module.AllotManagerService;
import com.grand.gt_wms.module.DumpingManagerService;
import com.grand.gt_wms.module.GoodsIptAndEptManagerService;
import com.grand.gt_wms.module.GoodsMoveManagerService;
import com.grand.gt_wms.module.GroundManagerService;
import com.grand.gt_wms.module.IQCManagerService;
import com.grand.gt_wms.module.Join1ManagerService;
import com.grand.gt_wms.module.JoinManagerService;
import com.grand.gt_wms.module.LoginManagerService;
import com.grand.gt_wms.module.PackManagerService;
import com.grand.gt_wms.module.ReceivingManagerService;
import com.grand.gt_wms.module.ReturnManagerService;
import com.grand.gt_wms.module.WhReturnManagerService;
import com.grand.gt_wms.module.WoReturnManagerService;
import com.grand.gt_wms.tool.PopUtil;
import com.grand.gt_wms.ui.activity.BaseActivity;
import com.grand.gt_wms.ui.activity.GoodsMoveActivity;
import com.grand.gt_wms.ui.activity.ReceivingActivity;
import com.grand.gt_wms.ui.activity.WHReturnActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/7/25.
 */

public class wmsService extends Service {
    public static User USER;
    public static PN pn;
    public static List<PKbox> PKBoxList;    //存放发料数据
    public static List<AKbox> AKBoxList;    //存放调拨数据
    public static List<BKbox> BKBoxList;    //存放报废数据
    public static List<CKbox> CKBoxList;    //存放拆解数据
    public static List<RnBox> RnBoxList;    //存放收货单数据
    public static List<ImgsBox> imgsBoxList;
    public static List<WhrBox> whrBoxList;    //存放仓退数据
    public static List<ImptAndEmptItem> imptAndEmptItemsList;   //物料进出
    public static List<IqcItem> iqcItemList;
    public static String START_TIME = "";
    public static String END_TIME = "";
    public static String TLF902="";       //仓库
    public static PopUtil popUtil;
    public static int i = 0;
    public static ArrayList<Box> scanedBox = new ArrayList<Box>();//存放已扫描箱子
    public static SoundPool sp;
    public static HashMap<Integer, Integer> soundPoolMap = new HashMap<Integer, Integer>();
    public static boolean ifScan = true;
    private DevSettings dev ;
    private LoginManagerService loginManager;
    private ReceivingManagerService receivingManager;
    private ReturnManagerService returnManager;    //定义退货单服务类变量
    private GroundManagerService groundManager;
    private PackManagerService packManager;
    private WoReturnManagerService woReturnManager;
    private IQCManagerService iqcManager;
    private WhReturnManagerService whReturnManager;
    private GoodsIptAndEptManagerService goodsIptAndEptManager;
    private GoodsMoveManagerService goodsMoveManager;
    private AllotManagerService allotManagerService;    //定义调拨服类务对象
    private DumpingManagerService dumpingManagerService;  //定义下阶报废服务类对象
    private ApartManagerService apartManagerService;  //定义拆解服务类对象
    private JoinManagerService joinManagerService;    //定义仓退交接服务类对象
    private Join1ManagerService join1ManagerService;   //定义送货单交接服务类对象
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("service start");
        dev = new DevSettings(this) ;
        dev.lockStatusBar(true) ;//屏蔽下滑通知栏
        dev.setMenuKey(true) ;//屏蔽菜单键
        dev.lockHome(true) ;//屏蔽home键
        loginManager = new LoginManagerService(this);
        receivingManager = new ReceivingManagerService(this);
        returnManager = new ReturnManagerService(this); //创建一个退货单服务类
        groundManager = new GroundManagerService(this);
        packManager = new PackManagerService(this);
        woReturnManager = new WoReturnManagerService(this);
        iqcManager = new IQCManagerService(this);
        whReturnManager = new WhReturnManagerService(this);
        goodsIptAndEptManager = new GoodsIptAndEptManagerService(this);
        goodsMoveManager = new GoodsMoveManagerService(this);
        allotManagerService=new AllotManagerService(this);     //调拨对象声明
        dumpingManagerService=new DumpingManagerService(this);  //声明下阶报废对象并初始化
        apartManagerService=new ApartManagerService(this);  //声明拆解对象并初始化
        joinManagerService=new JoinManagerService(this);  //声明仓退交接对象并初始化
        join1ManagerService=new Join1ManagerService(this);   //声明送货单交接对象
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .penaltyLog().build());
        wmsBroadcastReceiver.getInstance().registerReceiver(this);
        wmsService.popUtil = PopUtil.createPopUtil(this);
        sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
        soundPoolMap.put(1, sp.load(this, R.raw.bibi, 1));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dev.lockStatusBar(false) ;
        dev.setMenuKey(false) ;
        dev.lockHome(false) ;
    }




}
