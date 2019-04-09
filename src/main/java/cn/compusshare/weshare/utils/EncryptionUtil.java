package cn.compusshare.weshare.utils;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @Author: LZing
 * @Date: 2019/4/2
 * 加解密工具
 */
public class EncryptionUtil {


    public static byte[] encrypt(String plainText,String strKey)throws Exception{
        SecretKeySpec secretKeySpec=getKey(strKey);
        Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv=new IvParameterSpec("0102030405060708".getBytes());
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,iv);
        byte[] encrypted=cipher.doFinal(plainText.getBytes());
        return encrypted;
    }

    public static String decrypt(byte[] plainText,String strKey)throws Exception{
        SecretKeySpec skeySpec=getKey(strKey);
        Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv=new IvParameterSpec("0102030405060708".getBytes());
        cipher.init(Cipher.DECRYPT_MODE,skeySpec,iv);
        byte[] original=cipher.doFinal(plainText);
        return new String(original);
    }

    private static SecretKeySpec getKey(String strKey){
        byte[] arrBTmp=strKey.getBytes();
        byte[] arrB=new byte[16];

        for(int i=0;i<arrBTmp.length&&i<arrB.length;i++){
            arrB[i]=arrBTmp[i];
        }

        SecretKeySpec skeySpec=new SecretKeySpec(arrB,"AES");

        return skeySpec;

    }

    public static String base64Encode(byte[]  bytes){

        return new BASE64Encoder().encode(bytes);
    }

    public static byte[] base64Decode(String base64Code) throws Exception{
        return base64Code.isEmpty()?null:new BASE64Decoder().decodeBuffer(base64Code);
    }

    public static String aesEncrypt(String plainText,String password)throws Exception{
        return parseByte2HexStr(encrypt(plainText,password));
    }

    public static String aesDncrypt(String plainText,String password)throws Exception{
        return plainText.isEmpty()?null:decrypt(parseHexStr2Byte(plainText),password);
    }

    /**
     * 将二进制转换成16进制
     * @method parseByte2HexStr
     * @param buf
     * @return
     * @throws
     * @since v1.0
     */
    public static String parseByte2HexStr(byte buf[]){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < buf.length; i++){
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     * @method parseHexStr2Byte
     * @param hexStr
     * @return
     * @throws
     * @since v1.0
     */
    public static byte[] parseHexStr2Byte(String hexStr){
        if(hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


}
