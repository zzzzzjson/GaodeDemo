package soexample.umeng.com.gaodemapdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

public class AddrChangeActivity extends AppCompatActivity implements View.OnClickListener, GeocodeSearch.OnGeocodeSearchListener {

    private EditText Addr_Edit;
    private Button Zheng_Btn;
    private EditText JinDU_Edit;
    private EditText WeiDu_Edit;
    private Button Fan_Btn;
    private TextView Get_Text;
    private GeocodeSearch geocodeSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addr_change);
        initView();
        //构造 GeocodeSearch 对象，并设置监听。
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
//通过GeocodeQuery设置查询参数,调用getFromLocationNameAsyn(GeocodeQuery geocodeQuery) 方法发起请求。
//address表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode都ok
//        GeocodeQuery query = new GeocodeQuery(address, "010");
//        geocoderSearch.getFromLocationNameAsyn(query);

    }

    private void initView() {
        Addr_Edit = (EditText) findViewById(R.id.Addr_Edit);
        Zheng_Btn = (Button) findViewById(R.id.Zheng_Btn);
        JinDU_Edit = (EditText) findViewById(R.id.JinDU_Edit);
        WeiDu_Edit = (EditText) findViewById(R.id.WeiDu_Edit);
        Fan_Btn = (Button) findViewById(R.id.Fan_Btn);
        Get_Text = (TextView) findViewById(R.id.Get_Text);

        Zheng_Btn.setOnClickListener(this);
        Fan_Btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Zheng_Btn:
                String addr = Addr_Edit.getText().toString().trim();
                if (addr.isEmpty()) {
                    Toast.makeText(this, "请输入地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                //参数1：addr 地址值 参数2：规定一个区域
                GeocodeQuery query = new GeocodeQuery(addr, null);
                geocodeSearch.getFromLocationNameAsyn(query);
                break;
            case R.id.Fan_Btn:
                String jingdu = JinDU_Edit.getText().toString().trim();
                String weidu = WeiDu_Edit.getText().toString().trim();
                if (jingdu.isEmpty() || weidu.isEmpty()) {
                    Toast.makeText(this, "请输入经纬度", Toast.LENGTH_SHORT).show();
                    return;
                }
                //这个是经纬度查询的类
                LatLonPoint point = new LatLonPoint(Double.parseDouble(jingdu), Double.parseDouble(weidu));
                RegeocodeQuery regeocodeQuery = new RegeocodeQuery(point, 2000000000, GeocodeSearch.AMAP);
                GeocodeSearch reSe = new GeocodeSearch(this);
                reSe.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                    @Override
                    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                        Log.e("onRegeocodeSearched", "onRegeocodeSearched");
                        RegeocodeAddress address = regeocodeResult.getRegeocodeAddress();
                        Get_Text.setText(address.getFormatAddress()+"地址");
                    }

                    @Override
                    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                    }
                });
                geocodeSearch.getFromLocationAsyn(regeocodeQuery);
                break;
        }
    }

    //把经纬度转换成地址
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        Log.e("onRegeocodeSearched", "onRegeocodeSearched");
        RegeocodeAddress address = regeocodeResult.getRegeocodeAddress();
        Get_Text.setText(address.getFormatAddress());
    }

    //是吧地址转换成经度纬度
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        Log.e("onGeocodeSearched", "onGeocodeSearched");
        //从查出来的结果集 得到地址对象
        GeocodeAddress address = geocodeResult.getGeocodeAddressList().get(0);
        //从地址对象里面得到 经纬度的类
        LatLonPoint latLonPoint = address.getLatLonPoint();
        //从这个point取经纬度即可
        Get_Text.setText("经度是：" + latLonPoint.getLongitude() + ",纬度是：" + latLonPoint.getLatitude());

    }
}
