package moksha.com.whereareyou.DialogUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;

/**
 * Created by krishna.khandagale on 1/21/2016.
 */
public class ShowDialog {
    //Create dialog and show to user
    public static void  showDialogWithOkAndCancel(Context context,String title,String messageDescription,String OKMessage,String cancelMessage, final int actionID, final CustomDialogInterface dialogInterface){
        AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(context);
        }else{
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title);
        builder.setMessage(messageDescription);
        builder.setPositiveButton(OKMessage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialogInterface.handleOkAction(actionID);
            }
        });
        builder.setNegativeButton(cancelMessage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialogInterface.handleOnCancelAction(actionID);
            }
        });
        AlertDialog dialog= builder.create();
        dialog.show();

    }
}
