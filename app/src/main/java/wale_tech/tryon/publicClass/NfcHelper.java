package wale_tech.tryon.publicClass;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import wale_tech.tryon.trigger.TriggerSet;
import wale_tech.tryon.publicSet.MapSet;

/**
 * Created by KeY on 2016/11/3.
 */

public class NfcHelper {


    public static String parseIntent(Context context, Intent intent) {
        String split_result = "";
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
        if (adapter == null) {
            Toast.makeText(context, "设备不支持NFC", Toast.LENGTH_SHORT).show();
        } else if (!adapter.isEnabled()) {
            Toast.makeText(context, "请在系统设置中启用NFC功能", Toast.LENGTH_SHORT).show();
        }

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            String result = readFromTag(intent);
            String sub_str = "en-US";

            if (result.isEmpty() || !result.startsWith("en-US")) {
                onToastErrorMsg(context);
                return split_result;
            }

            split_result = result.substring(sub_str.length(), result.length());
            Log.i("Result", "split is :" + split_result);

            if (!split_result.startsWith("ws") && !split_result.startsWith("sku")) {
                onToastErrorMsg(context);
                return "";
            }

            return split_result;
        }

        return split_result;
    }
    // intent = getIntent();

//    public static HashMap<String, String> read(Context context, Intent intent) {
//        HashMap<String, String> map = new HashMap<>();
//
//        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
//        if (adapter == null) {
//            Toast.makeText(context, "设备不支持NFC", Toast.LENGTH_SHORT).show();
//        } else if (!adapter.isEnabled()) {
//            Toast.makeText(context, "请在系统设置中启用NFC功能", Toast.LENGTH_SHORT).show();
//        }
//
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
//            String result = readFromTag(intent);
//            String sub_str = "en-US";
//
//            if (result.isEmpty() || !result.startsWith("en-US")) {
//                onToastErrorMsg(context);
//                return null;
//            }
//
//            String split_result = result.substring(sub_str.length() + 1, result.length());
//            Log.i("Result", "split is :" + split_result);
//
//            // 判断是否来自平台
//            if (split_result.startsWith("sku")) {
//                map.put(MapSet.KEY_TRIGGER_RESULT, split_result);
//                map.put(MapSet.KEY_TRIGGER_PATH, TriggerSet.PATH_NFC);
//                map.put(MapSet.KEY_TRIGGER_SPACE, TriggerSet.WORK_SPACE_PLATFORM);
//                return map;
//            }
//
//            // 判断是否来自平凳
//            if (split_result.startsWith("ws")) {
//                map.put(MapSet.KEY_TRIGGER_RESULT, split_result);
//                map.put(MapSet.KEY_TRIGGER_PATH, TriggerSet.PATH_NFC);
//                map.put(MapSet.KEY_TRIGGER_SPACE, TriggerSet.WORK_SPACE_SEAT);
//                return map;
//            }
//
//            // 两者都不是，不是我们的NFC CODE
//            onToastErrorMsg(context);
//            return null;
//        } else {
//            return null;
//        }
//    }

    private static String readFromTag(Intent intent) {
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage message = (NdefMessage) rawArray[0];
        NdefRecord record = message.getRecords()[0];

        if (record != null) {
            try {
                Log.i("Result", "read :" + new String(record.getPayload()).trim());
                return new String(record.getPayload(), "UTF-8").trim();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return "null";
    }

    private static void onToastErrorMsg(Context context) {
        Toast.makeText(context, "无法解析该数据", Toast.LENGTH_SHORT).show();
    }
}
