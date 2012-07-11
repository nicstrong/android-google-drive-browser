//package com.nicstrong;
//
//import android.app.ProgressDialog;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.ArrayAdapter;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.model.File;
//import com.google.api.services.drive.model.FileList;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//class AsyncLoadFiles extends AsyncTask<Void, Void, List<String>> {
//    private static final String TAG = "GoogleDriveTest";
//
//    private final ProgressDialog dialog;
//    private Drive service;
//    private GoogleDriveTestActivity activity;
//
//    AsyncLoadFiles(GoogleDriveTestActivity activity) {
//        this.activity = activity;
//        service = activity.service;
//        dialog = new ProgressDialog(activity);
//    }
//
//    @Override
//    protected void onPreExecute() {
//        dialog.setMessage("Loading files...");
//        dialog.show();
//    }
//
//    @Override
//    protected List<String> doInBackground(Void... arg0) {
//        try {
//            List<File> result = new ArrayList<File>();
//            Drive.Files.List request = service.files().list();
//
//
//            do {
//                try {
//                    FileList files = request.execute();
//                    result.addAll(files.getItems());
//
//                    request.setPageToken(files.getNextPageToken());
//                } catch (IOException e) {
//                    System.out.println("An error occurred: " + e);
//                    request.setPageToken(null);
//                }
//            } while (request.getPageToken() != null && request.getPageToken().length() > 0);
//
//
//            List<String> items = new ArrayList<String>();
//            for (File file : result) {
//                items.add(file.getTitle());
//                Log.i(TAG, "File: " + file);
//            }
//            return items;
//        } catch (IOException e) {
//            activity.handleGoogleException(e);
//            return Collections.singletonList(e.getMessage());
//        } finally {
//            activity.onRequestCompleted();
//        }
//    }
//
//    @Override
//    protected void onPostExecute(List<String> result) {
//        dialog.dismiss();
//        activity.setListAdapter(
//                new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, result));
//    }
//}