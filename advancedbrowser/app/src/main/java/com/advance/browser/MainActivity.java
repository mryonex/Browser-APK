package com.advance.browser;

import android.app.*;
import android.os.*;
import android.webkit.*;
import android.widget.*;
import android.graphics.*;
import android.view.*;
import android.content.*;
import android.net.*;

public class MainActivity extends Activity 
{
	ProgressBar pg;
	WebView web;
	ImageView img;
	LinearLayout lt;
	String currenturl;

	private DownloadManager downloadManager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		pg = (ProgressBar) findViewById(R.id.mainProgressBar);
		web = (WebView) findViewById(R.id.mainwebview);
		img = (ImageView) findViewById(R.id.mainImageView);
		lt = (LinearLayout) findViewById(R.id.mainLinearLayout);
	
		
		pg.setMax(100);
		
		web.loadUrl("https://www.google.com");
		web.setWebViewClient(new WebViewClient(){
			
		public void	onPageStarted(WebView w,String url,Bitmap favicon){
				lt.setVisibility(View.VISIBLE);
				currenturl=url;
			}
			
			public void onPageFinished(WebView w,String url){
				lt.setVisibility(View.GONE);
			}
		});
		
		web.getSettings().setJavaScriptEnabled(true);
		web.setWebChromeClient(new WebChromeClient(){
	
			public void onProgressChanged(WebView w,int newprogress){
				super.onProgressChanged(w,newprogress);
				pg.setProgress(newprogress);
			}
			
			public void	onReceivedTitle(WebView w,String title){
				super.onReceivedTitle(w,title);
				getActionBar().setTitle(title);
			}
			
			public void onReceivedIcon(WebView view,Bitmap icon){
				super.onReceivedIcon(view,icon);
				img.setImageBitmap(icon);
			}
		});
		
	
		web.setDownloadListener(new DownloadListener(){

				@Override
				public void onDownloadStart(String p1, String p2, String p3, String p4, long p5)
				{
					DownloadManager.Request myrequest = new DownloadManager.Request(Uri.parse(p1));
					myrequest.allowScanningByMediaScanner();
					myrequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
					
					DownloadManager mymanager =  (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
					
					mymanager.enqueue(myrequest);
					Toast.makeText(MainActivity.this,"Downloading is started",Toast.LENGTH_SHORT).show();
					
				}
		});
		
		
		DownloadManager.Query query = null;
		downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
		query = new DownloadManager.Query();
		query.setFilterByStatus(DownloadManager.STATUS_FAILED|DownloadManager.STATUS_PENDING|DownloadManager.STATUS_RUNNING|DownloadManager.STATUS_SUCCESSFUL);
		
		Toast.makeText(getApplicationContext()," "+query,Toast.LENGTH_LONG).show();
    }



	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
	
		getMenuInflater().inflate(R.menu.option_menu,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	
		switch(item.getItemId()){
		
			case R.id.b:
				onBackPressed();
		     return true;
			 
		    case R.id.f:
				onForwardPressed();
			return true;
			
			case R.id.r:
				web.reload();
			return true;
		
			case R.id.s:
			
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_TEXT,currenturl);
				i.putExtra(Intent.EXTRA_SUBJECT,"copied url");
				startActivity(Intent.createChooser(i,"share with friend"));
				return true;	
				
			default:
		return super.onOptionsItemSelected(item);
		}
	}
	

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		if(web.canGoBack()){
			web.goBack();
		}
		else {
			finish();
		}
	}
	
	
	public void onForwardPressed()
	{
		if(web.canGoForward()){
			web.goBack();
		}
		else {
			Toast.makeText(MainActivity.this,"can't go further",Toast.LENGTH_SHORT).show();
		}
	}
	
}
