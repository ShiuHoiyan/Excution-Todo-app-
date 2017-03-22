package  com.example.diong.todo.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.diong.todo.R;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler{
    private IWXAPI api;
    String appID = "wx2a66ee775ea965af";
    Button share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        
        getButton();

        api = WXAPIFactory.createWXAPI(this, appID, false);

        api.handleIntent(getIntent(), this);

//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                api.registerApp(appID);
//
//                WXTextObject textObj = new WXTextObject();
//
//                textObj.text = "hallo";
//
//                // 用WXTextObject对象初始化一个WXMediaMessage对象
//                WXMediaMessage msg = new WXMediaMessage();
//                msg.mediaObject = textObj;
//                // 发送文本类型的消息时，title字段不起作用
//                // msg.title = "Will be ignored";
//                msg.description = "hallo";
//
//                // 构造一个Req
//                SendMessageToWX.Req req = new SendMessageToWX.Req();
//                req.transaction = "transaction"+System.currentTimeMillis(); // transaction字段用于唯一标识一个请求
//                req.message = msg;
//                req.scene = true ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//
//                // 调用api接口发送数据到微信
//                api.sendReq(req);
//                finish();
//            }
//        });
    }

    private void getButton() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.activity_item_details,null);
        share = (Button) layout.findViewById(R.id.share);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        TextView textView = (TextView) findViewById(R.id.error);
        textView.setText(baseResp.errCode);

    }
}
