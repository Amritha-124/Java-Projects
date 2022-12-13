package com.MiniProject.FoodApp2.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;


@Service
public class SmsService {
    private static final String ACCOUNT_SID = "AC296dceb53921f8b0d0fa244ad8f3dac7";
    private static final String AUTH_ID = "b3539a84e5efa7d67c4ff30237343f24";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_ID);
    }

    public boolean sendSms(String mobilenumber, String tfaCode)
    {
        Message.creator(new PhoneNumber(mobilenumber), new PhoneNumber("+12074050113"),
                "Your Two Factor AUthentication code is:" + tfaCode).create();
        return false;
    }
}
