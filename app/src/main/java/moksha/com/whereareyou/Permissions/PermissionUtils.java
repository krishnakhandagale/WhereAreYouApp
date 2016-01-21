package moksha.com.whereareyou.Permissions;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by krishna.khandagale on 1/21/2016.
 */
public class PermissionUtils {

    public static void requestPermission(Context context,String permissionID,int requestID,String [] permissionsToBeGranted) {

        // Assume thisActivity is the current activity

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context,
                permissionID)
                != PackageManager.PERMISSION_GRANTED ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    permissionID)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions((Activity)context,
                        permissionsToBeGranted,
                        requestID);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
}
