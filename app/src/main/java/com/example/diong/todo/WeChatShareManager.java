package com.example.diong.todo;


import android.content.Context;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by wangyunwen on 16/12/19.
 */
public class WechatShareManager {
    public static final int WEIXIN_SHARE_TYPE_FRENDS = SendMessageToWX.Req.WXSceneTimeline;
    private static WechatShareManager instance;
    private static String weixinAppId;
    private IWXAPI wxApi;
    private Context context;
    String appID = "wx2a66ee775ea965af";

    public WechatShareManager(Context context) {
        this.context = context;
        if (wxApi == null) {
            wxApi = WXAPIFactory.createWXAPI(context, appID, true);
        }
        wxApi.registerApp(appID);
    }

    public static WechatShareManager getInstance(Context context){
        if(instance == null){
            instance = new WechatShareManager(context);
        }
        return instance;
    }

    public void shareText(TodoItem todoItem) {
        String text;
        if(todoItem.getFinish() == 1) {
            // 已经完成分享文字
            text = "在Execution的督促下，我完成了 "+ todoItem.getToDoContent() + " 这一任务\n比心(´▽`ʃ♡ƪ)";
        } else {
            // 未完成分享文字
            text = "Execution提醒我还有完成 "+todoItem.getToDoContent()+" 这一任务\n要加油呀(๑•̀ㅂ•́)و✧";
        }
        WXTextObject textObj = new WXTextObject();

        textObj.text = text;

        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        // 发送文本类型的消息时，title字段不起作用
        // msg.title = "Will be ignored";
        msg.description = text;

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "transaction"+System.currentTimeMillis(); // transaction字段用于唯一标识一个请求
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;

        // 调用api接口发送数据到微信
        wxApi.sendReq(req);
        // finish();
    }

    public boolean isWebchatAvaliable() {
        //检测手机上是否安装了微信
        if (!wxApi.isWXAppInstalled()) {
            Toast.makeText(context, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
