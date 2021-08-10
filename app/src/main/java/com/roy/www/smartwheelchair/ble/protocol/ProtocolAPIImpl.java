package com.roy.www.smartwheelchair.ble.protocol;


import com.roy.www.smartwheelchair.utils.GsonUtil;


import java.util.HashMap;
import java.util.Map;

public class ProtocolAPIImpl implements ProtocolAPI {
    private static final String TAG = "==" + ProtocolAPIImpl.class.getSimpleName() + "==";
    private static ProtocolAPIImpl mInstance;

    private static Protocol mProtocol;

    public static ProtocolAPIImpl getInstance() {
        if(mInstance == null) {
            mInstance = new ProtocolAPIImpl();
            mProtocol = Protocol.getInstance();
        }
        return mInstance;
    }


    @Override
    public void advance() {
        mProtocol.createCmdPacket(Command.CMD_ACTION_ADVANCE,null);
    }

    @Override
    public void retreat() {
        mProtocol.createCmdPacket(Command.CMD_ACTION_RETREAT,null);
    }

    @Override
    public void left() {
        mProtocol.createCmdPacket(Command.CMD_ACTION_LEFT,null);
    }

    @Override
    public void right() {
        mProtocol.createCmdPacket(Command.CMD_ACTION_RIGHT,null);
    }

    @Override
    public void stop() {
        mProtocol.createCmdPacket(Command.CMD_ACTION_STOP,null);
    }

    @Override
    public void setWifi(String ssid,String password) {
        Map<String, String> map = new HashMap<>();
        map.put("ssid", ssid);
        map.put("password", password);
       mProtocol.createCmdPacket(Command.CMD_ACTION_SET_WIFI, GsonUtil.ObjectToString(map).getBytes());
    }
}
