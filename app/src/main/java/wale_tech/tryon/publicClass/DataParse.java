package wale_tech.tryon.publicClass;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import wale_tech.tryon.trigger.TriggerList_Act;
import wale_tech.tryon.trigger.TriggerSet;
import wale_tech.tryon.publicObject.ObjectTrigger;
import wale_tech.tryon.publicSet.IntentSet;

/**
 * Created by lenovo on 2016/11/22.
 */

public class DataParse {
    public ObjectTrigger parseTriggerIntent(Context context, Intent intent) {
//        TaskStackBuilder builder = TaskStackBuilder.create(context);
//
//        builder.addParentStack(TriggerList_Act.class);
//        builder.addNextIntent(lockinfo_int);

        if (intent.getAction().equals(IntentSet.ACTION_SCAN)) {
            return setupScanTrigger(intent);
        } else if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            return setupNfcTrigger(context, intent);
        } else if (intent.getAction().equals(IntentSet.ACTION_NFC)) {
            return setupNfcLoginTrigger(intent);
        }

        return null;
    }

    private ObjectTrigger setupScanTrigger(Intent intent) {
        Log.i("Result", "scan trigger");
        ObjectTrigger trigger = new ObjectTrigger();

        trigger.setResult(intent.getStringExtra(IntentSet.KEY_TRIGGER_RESULT));
        trigger.setPath(TriggerSet.PATH_SCAN);
        trigger.setWorkSpace(intent.getStringExtra(IntentSet.KEY_TRIGGER_WORK_SPACE));

        return trigger;
    }

    private ObjectTrigger setupNfcTrigger(Context context, Intent intent) {
        Log.i("Result", "nfc trigger");

        ObjectTrigger trigger = new ObjectTrigger();
        String trigger_result = NfcHelper.parseIntent(context, intent);

        trigger.setResult(trigger_result);
        trigger.setPath(TriggerSet.PATH_NFC);

        if (trigger_result.startsWith("sku")) {
            trigger.setWorkSpace(TriggerSet.WORK_SPACE_PLATFORM);
        }

        if (trigger_result.startsWith("ws")) {
            trigger.setWorkSpace(TriggerSet.WORK_SPACE_SEAT);
        }

        if (trigger_result.isEmpty()) {
            return null;
        }

        return trigger;
    }

    private ObjectTrigger setupNfcLoginTrigger(Intent intent) {
        ObjectTrigger trigger = new ObjectTrigger();

        trigger.setResult(intent.getStringExtra(IntentSet.KEY_TRIGGER_RESULT));
        trigger.setPath(TriggerSet.PATH_SCAN);
        trigger.setWorkSpace(intent.getStringExtra(IntentSet.KEY_TRIGGER_WORK_SPACE));

        return trigger;
    }

}
