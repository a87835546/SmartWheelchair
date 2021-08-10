package com.roy.www.smartwheelchair.ble.protocol;

import android.util.Log;

import com.roy.www.smartwheelchair.utils.CRC16;
import com.roy.www.smartwheelchair.utils.HexDump;


/**
 * Created by 李杨
 * On 2021/6/20
 * Email: 631934797@qq.com
 * Description:
 */

public class PackData {


    private byte head = Command.HEAD;
    private byte addr = 0x01;
    private byte len;
    private byte cmd;
    private byte status = (byte) 0xFF;
    private byte[] dataBody = new byte[256];;
    private byte[] crc = new byte[2];
    private byte tial = Command.TAIL;

    public byte getHead() {
        return head;
    }

    public void setHead(byte head) {
        this.head = head;
    }

    public byte getAddr() {
        return addr;
    }

    public void setAddr(byte addr) {
        this.addr = addr;
    }

    public byte getLen() {
        return len;
    }

    public void setLen(byte len) {
        this.len = len;
    }

    public byte getCmd() {
        return cmd;
    }

    public void setCmd(byte cmd) {
        this.cmd = cmd;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte[] getDataBody() {
        return HexDump.getSubArray(dataBody,0,len-2 );
    }

    public void setDataBody(byte[] data) {
        System.arraycopy(data, 0, this.dataBody, 0, data.length);
    }

    public byte[] getCrc() {
        return crc;
    }

    public void setCrc(byte[] crcBytes) {
        System.arraycopy(crcBytes, 0, this.crc, 0, crcBytes.length);
    }

    public byte getTial() {
        return tial;
    }

    public void setTial(byte tial) {
        this.tial = tial;
    }

    /**
     * 创建指令包
     *
     * @param bytes
     * @return
     */
    public static PackData createCommPacket(byte[] bytes) {
        PackData packet = new PackData();
        packet.setHead(bytes[0]);
        packet.setAddr(bytes[1]);
        packet.setLen(bytes[2]);
        packet.setCmd(bytes[3]);
        packet.setStatus(bytes[4]);
        if (bytes.length >8)
            packet.setDataBody(HexDump.getSubArray(bytes, 5, (bytes[2]&0xFF)-2));
        packet.setCrc(new byte[]{bytes[bytes.length-3],bytes[bytes.length-2]});
        packet.setTial(bytes[bytes.length - 1]);
        Log.i("packet","packet :" + packet.toString());
        return packet;
    }

    /**
     * 将CommPack转换成bytes
     *
     * @param commPack
     * @return
     */
    public static byte[] toStreamPack(PackData commPack) {
        int dataLen = (commPack.getLen() & 0xFF);
        byte[] commStream = new byte[dataLen + 6];

        commStream[0] = commPack.getHead();
        commStream[1] = commPack.getAddr();
        commStream[2] = commPack.getLen();
        commStream[3] = commPack.getCmd();
        commStream[4] = commPack.getStatus();

        byte[] dataBody = commPack.getDataBody();

        if (dataBody != null){
            for (int i = 0; i < dataBody.length ; i++) {
                commStream[i+5] = dataBody[i];
            }
            byte[] crc16_modbus = CRC16.getCRC16_Modbus(HexDump.getSubArray(commStream, 1, commStream.length-4));

            commStream[commStream.length - 3] = crc16_modbus[0];
            commStream[commStream.length - 2] = crc16_modbus[1];
            commStream[commStream.length - 1] =  commPack.getTial();
        }else {
            byte[] crc16_modbus = CRC16.getCRC16_Modbus(HexDump.getSubArray(commStream, 1, commStream.length-4));

            commStream[commStream.length - 3] = crc16_modbus[0];
            commStream[commStream.length - 2] = crc16_modbus[1];
            commStream[commStream.length - 1] =  commPack.getTial();
        }

        return commStream;
    }





    @Override
    public String toString() {
        return "CommPack{" +
                "head=" + HexDump.byteTo16String(head)+
                ", addr=" + HexDump.byteTo16String(addr) +
                ", len=" + HexDump.byteTo16String(len) +
                ", cmd=" + HexDump.byteTo16String(cmd) +
                ", status=" + HexDump.byteTo16String(status) +
                ", dataBody=" + HexDump.byteTo16String(dataBody) +
                ", crc=" + HexDump.byteTo16String(crc) +
                ", tial=" + HexDump.byteTo16String(tial) +
                '}';
    }
}
