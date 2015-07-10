package com.nobug.android.library.view.image.picasso;

public class Notification {

/*    private static final int NOTIFICATION_ID = 666;

    public static void show(Context context, Activity activity, String url, int drawableIcon, int size) {
        RemoteViews remoteViews =
                new RemoteViews(context.getPackageName(), R.layout_notification_view);

        Intent intent = new Intent(context, activity.getClass());

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(activity).setSmallIcon(drawableIcon)
                        .setContentIntent(PendingIntent.getActivity(activity, -1, intent, 0))
                        .setContent(remoteViews);

        android.app.Notification notification = builder.getNotification();
        // Bug in NotificationCompat that does not set the content.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            notification.contentView = remoteViews;
        }

        NotificationManager notificationManager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);

        // Now load an image for this notification.
        Picasso.with(activity) //
                .load(url) //
                .resizeDimen(size, size) //
                .into(remoteViews, R.id.notificationPhoto, NOTIFICATION_ID, notification);
    }*/

}
