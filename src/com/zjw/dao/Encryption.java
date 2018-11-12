package com.zjw.dao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Encryption {

    public static double progress=0;
    private static double comprogress=0;
    private static String security="security";
    public static String state;

    public static void encryption(File file) throws FileNotFoundException {
        progress=0;
        comprogress=0;
        state="正在加密！";
        byte salt=(byte) (Math.random()*129);
        if (!file.isFile()){
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        String filename=file.getName();
        long size = file.length();
        String absolutePath = file.getParent();
        File newFile=new File(absolutePath+"/1"+filename);
        BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(newFile));
        int by=-1;
        try {
            bos.write(0);
            bos.write(security.getBytes());
            bos.write(salt);
            bos.write(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            while((by=bis.read())!=-1){
                by=~by;
                by=by^salt;
                bos.write(by);
                comprogress=comprogress+1;
                progress=comprogress/size*100;
            }
            bos.flush();
        }catch (IOException e) {
            boolean delete = file.delete();
            System.out.println(delete);
            e.printStackTrace();
        }finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file.delete();
            newFile.renameTo(new File(file.getAbsolutePath()));
        }
    }
    public static void decrypt(File file) throws FileNotFoundException {
        progress=0;
        comprogress=0;
        state="正在解密！";
        if (!file.isFile()){
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        int salt=0;
        String filename=file.getName();
        long size = file.length()-(3+security.getBytes().length)*100;
        String absolutePath = file.getParent();
        File newFile=new File(absolutePath+"/1"+filename);
        BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(new File(absolutePath+"/1"+filename)));
        int head=-1;
        byte[] buf=security.getBytes();
        int floor=-1;
        try {
            head= bis.read();
            bis.read(buf);
            salt=bis.read();
            floor= bis.read();
            int by=-1;
            while((by=bis.read())!=-1){
                by=by^salt;
                by=~by;
                bos.write(by);
                comprogress=comprogress+1;
                progress=comprogress/size*100;
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file.delete();
            newFile.renameTo(new File(file.getAbsolutePath()));
        }

    }
    public static void start(File file) throws FileNotFoundException {
        if (!file.isFile()){
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        int salt=0;
        BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
        int head=-1;
        byte[] buf=security.getBytes();
        int floor=-1;
        try {
            head= bis.read();
            bis.read(buf);
            salt=bis.read();
            floor= bis.read();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String str=new String(buf);
        if (head==0 && floor==0 && security.equals(str)){
            decrypt(file);
        }else {
            encryption(file);
        }
    }
}
