package com.wei.customviews.test.designpattern.facade;

/**
 * author: WEI
 * date: 2017/3/14
 */

public class MobilePhone
{
    private Phone mPhone = new PhoneImpl();
    private Camera mCamera = new XiaomiCamera();

    public void callup()
    {
        mPhone.callup();
    }

    public void videoChat()
    {
        System.out.println("-->视频聊天接通中");
        mCamera.open();
        mPhone.callup();
    }

    public void hangup()
    {
        mPhone.hangup();
    }

    public void takePicture()
    {
        mCamera.open();
        mCamera.takePicture();
    }

    public void closeCamera()
    {
        mCamera.close();
    }

}
