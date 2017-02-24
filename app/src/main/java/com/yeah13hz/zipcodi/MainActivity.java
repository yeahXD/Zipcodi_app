package com.yeah13hz.zipcodi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.yeah13hz.zipcodi.gcm.PropertyManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    WebView webView;
    RadioButton btn_i;
    RadioButton btn_f;
    RadioButton btn_b;
    RadioButton btn_inquiry;
    RadioButton btn_my;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;

    int backButtonCount = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri[] results = null;
            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return;
                }
                Uri result = null;
                try {
                    if (resultCode != RESULT_OK) {
                        result = null;
                    } else {
                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e,
                            Toast.LENGTH_LONG).show();
                }
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        toolbar.setNavigationIcon(R.drawable.button_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });

        webView = (WebView)findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebContentsDebuggingEnabled(true);
//        webView.getSettings().setSupportMultipleWindows(true);

        btn_i = (RadioButton)findViewById(R.id.btn_i);
        btn_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "www.zipcodi.com/portfolios?app=true";
                if (!TextUtils.isEmpty(url)) {
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "http://" + url;
                    }
                    webView.loadUrl(url);
                }
            }
        });

        btn_f = (RadioButton)findViewById(R.id.btn_f);
        btn_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "www.zipcodi.com/furnitures?app=true";
                if (!TextUtils.isEmpty(url)) {
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "http://" + url;
                    }
                    webView.loadUrl(url);
                }
            }
        });
        btn_b = (RadioButton)findViewById(R.id.btn_b);
        btn_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "www.zipcodi.com/brandings?app=true";
                if (!TextUtils.isEmpty(url)) {
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "http://" + url;
                    }
                    webView.loadUrl(url);
                }
            }
        });
        btn_inquiry = (RadioButton)findViewById(R.id.btn_inquiry);
        btn_inquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "www.zipcodi.com/inquiries/new?app=true";
                if (!TextUtils.isEmpty(url)) {
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "http://" + url;
                    }
                    webView.loadUrl(url);
                }
            }
        });
        btn_my = (RadioButton)findViewById(R.id.btn_my);
        btn_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "www.zipcodi.com/my?app=true&token=" + PropertyManager.getInstance().getRegistrationToken() + "";
                if (!TextUtils.isEmpty(url)) {
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "http://" + url;
                    }
                    webView.loadUrl(url);
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                // Double check that we don't have any existing callbacks
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.e(TAG, "Unable to create Image File", ex);
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");
                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }
                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
                return true;
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                // Create AndroidExampleFolder at sdcard
                // Create AndroidExampleFolder at sdcard
                File imageStorageDir = new File(
                        Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES)
                        , "AndroidExampleFolder");
                if (!imageStorageDir.exists()) {
                    // Create AndroidExampleFolder at sdcard
                    imageStorageDir.mkdirs();
                }
                // Create camera captured image file path and name
                File file = new File(
                        imageStorageDir + File.separator + "IMG_"
                                + String.valueOf(System.currentTimeMillis())
                                + ".jpg");
                mCapturedImageURI = Uri.fromFile(file);
                // Camera capture image intent
                final Intent captureIntent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                // Create file chooser intent
                Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
                // Set camera intent to file chooser
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                        , new Parcelable[] { captureIntent });
                // On select image call onActivityResult method of activity
                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
            }
            // openFileChooser for Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooser(uploadMsg, "");
            }
            //openFileChooser for other Android versions
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType,
                                        String capture) {
                openFileChooser(uploadMsg, acceptType);
            }
        });

        String url = null;
        Uri uri = getIntent().getData();
        String action = getIntent().getAction();
        if (action != null && action.equals(Intent.ACTION_VIEW)) {
            url = uri.toString();
        }
        if (url == null) {
            url = "http://www.zipcodi.com/portfolios?app=true";
        }

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String n_current_url = url.substring(23);
                Log.i("MainActivity","my_current_url : " + n_current_url);
                if (n_current_url.startsWith("portfolios")) {
                    btn_i.toggle();
                    btn_i.invalidate();
                    Log.i("MainActivity", n_current_url + " clicked");
                }else if (n_current_url.startsWith("furnitures")) {
                    btn_f.toggle();
                    btn_f.invalidate();
                    Log.i("MainActivity", n_current_url + " clicked");
                }else if (n_current_url.startsWith("brandings")) {
                    btn_b.toggle();
                    btn_b.invalidate();
                    Log.i("MainActivity", n_current_url + " clicked");
                }else if (n_current_url.startsWith("inquiries")) {
                    btn_inquiry.toggle();
                    btn_inquiry.invalidate();
                    Log.i("MainActivity", "token serial number : " + PropertyManager.getInstance().getRegistrationToken());
//                    Toast.makeText(MainActivity.this, "Token : " + PropertyManager.getInstance().getRegistrationToken(), Toast.LENGTH_SHORT).show();
                    webView.loadUrl("javascript:(function(){$('#token').val('" + PropertyManager.getInstance().getRegistrationToken() + "')})();");
                    Log.i("MainActivity", n_current_url + " clicked");
                }else if (n_current_url.startsWith("my")) {
                    btn_my.toggle();
                    btn_my.invalidate();
                    Log.i("MainActivity", "token serial number : " + PropertyManager.getInstance().getRegistrationToken());
//                    Toast.makeText(MainActivity.this, "Token : " + PropertyManager.getInstance().getRegistrationToken(), Toast.LENGTH_SHORT).show();
//                    webView.loadUrl("javascript:(function(){$('#f_login').href('/users/auth/facebook?token=" + PropertyManager.getInstance().getRegistrationToken() + "')})();");
                    String f_href = "/users/auth/facebook?token=" + PropertyManager.getInstance().getRegistrationToken();
                    String n_href = "/users/auth/naver?token=" + PropertyManager.getInstance().getRegistrationToken();
                    webView.loadUrl("javascript:(function(){" +
                            "$('#token').val('" + PropertyManager.getInstance().getRegistrationToken() + "');" +
                            "document.getElementById('f_login').setAttribute('href', '" + f_href + "');" +
                            "document.getElementById('n_login').setAttribute('href', '" + n_href + "');" +
                            "})();");
//                    webView.loadUrl("javascript:(function(){$('#n_login').setAttribute('href', '" + n_href + "')})();");
//                    webView.loadUrl("javascript:(function(){$('#f_login').setAttribute('href', '" + f_href + "')})();");
//                    webView.loadUrl("javascript:(function(){alert(\"\"+$('#f_login').href);");
                    Log.i("MainActivity", n_current_url + " clicked");
                }
                super.onPageFinished(view, url);
            }
        });

        webView.loadUrl(url);

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.resumeTimers();
        backButtonCount = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.pauseTimers();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuitem = menu.findItem(R.id.btn_search);
        SearchView searchView = (SearchView)menuitem.getActionView();

        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.icon_search);

        final int searchTextId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        final TextView textView = (TextView) searchView.findViewById(searchTextId);
        textView.setTextColor(getResources().getColor(R.color.colorZipcodiMain));
//        textView.setHint("키워드를 입력해주세요");

        ImageView searchClose = (ImageView) searchView.findViewById(getResources().getIdentifier("android:id/search_close_btn", null, null));
        searchClose.setImageResource(R.drawable.icon_close);
        final ImageView toolbar_image = (ImageView) findViewById(R.id.toolbar_image);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu != null) {
                    menu.findItem(R.id.btn_alarm).setVisible(false);
                    toolbar_image.setVisibility(View.INVISIBLE);
                    textView.requestFocus();
                }
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // re-show the action button
                if (menu != null) {
                    menu.findItem(R.id.btn_alarm).setVisible(true);
                    toolbar_image.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                webView.loadUrl("http://www.zipcodi.com/search?app=true&keyword="+s);
                btn_i.toggle();
                btn_i.invalidate();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        menu.findItem(R.id.btn_alarm).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                webView.loadUrl("http://www.zipcodi.com/alarm?app=true");
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action ar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//
//        if(item.getItemId() == R.id.btn_search){
//            String url = webView.getUrl() + "search=true";
//            if (!TextUtils.isEmpty(url)) {
//                if (!url.startsWith("http://") && !url.startsWith("https://")) {
//                    url = "http://" + url;
//                }
//                webView.loadUrl(url);
//            }
//        }
//        if(item.getItemId() == R.id.btn_alarm){
//
//        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed()
    {
        if (webView.canGoBack()) {
            webView.goBack();
        }else if(backButtonCount >= 1){
            finish();
        }else{
            Toast.makeText(this, "종료하시려면 한 번 더 터치해주세요", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
}
