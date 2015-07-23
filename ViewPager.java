package com.viewpager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.example.qzzm.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * ViewPager
 * 
 * 
 */
public class MViewPager extends FrameLayout {

	private ImageLoader imageLoader = ImageLoader.getInstance();
	// Universal-image的参数设置
	private static DisplayImageOptions options;

	// private String[] imageUrls;
	private List<String> imageUrls = new ArrayList<String>();
	private List<ImageView> imageViewsList;
	private List<View> dotViewsList;

	private ViewPager viewPager;
	private int currentItem = 0;

	private Context context;

	private ViewPagerItemCLickListener itemClickListener;

	private Timer time;
	private LinearLayout dotLayout;

	public MViewPager(Context context) {
		this(context, null);
	}

	public MViewPager(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MViewPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this,
				true);
		dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		initImageLoader(context);
		initData();
		startPlay();
	}

	private void startPlay() {
		autogallery();
	}

	private void initData() {
		imageViewsList = new ArrayList<ImageView>();
		dotViewsList = new ArrayList<View>();

	}

	private void initUI() {
		if (imageUrls.size() == 0) {
			imageUrls.add("");
		}
		currentItem = 0;
		dotLayout.removeAllViews();
		imageViewsList.clear();
		dotViewsList.clear();
		for (int i = 0; i < imageUrls.size(); i++) {
			ImageView dotView = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 4;
			params.rightMargin = 4;
			dotLayout.addView(dotView, params);
			dotViewsList.add(dotView);
		}
		for (int i = 0; i < dotViewsList.size(); i++) {
			if (i == 0) {
				((View) dotViewsList.get(0))
						.setBackgroundResource(R.drawable.dot_focus);
			} else {
				((View) dotViewsList.get(i))
						.setBackgroundResource(R.drawable.dot_blur);
			}
		}

		viewPager.setFocusable(true);
		viewPager.setAdapter(new MyPagerAdapter());
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View container, int position, Object object) {
			Log.i("demo1", position + " ");
			((ViewPager) container).removeView(imageViewsList.get(position));
		}

		@Override
		public Object instantiateItem(View container, final int position) {
			final int pos = position % imageUrls.size();
			ImageView view = new ImageView(context);
			view.setTag(imageUrls.get(pos));
			view.setBackgroundResource(R.drawable.app_default);
			view.setScaleType(ScaleType.FIT_XY);
			imageLoader.displayImage(imageUrls.get(pos), view, options);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (itemClickListener != null) {
						itemClickListener.onItemClick(v, pos);
					}
				}
			});
			imageViewsList.add(view);
			((ViewPager) container).addView(view);

			return view;
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}

	}

	public void autogallery() {
		if (time != null) {
			time.cancel();
			time = null;
		}
		time = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (imageUrls.size() > 1) {
					Message m = new Message();
					handler.sendMessage(m);
				}
			}
		};
		time.schedule(task, 5000, 5000);
	}

	final Handler handler = new Handler() {
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {
			viewPager.setCurrentItem(currentItem + 1);
		}
	};

	private class MyPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
			case 1:
				if (time != null) {
					time.cancel();
					time = null;
				}
				break;
			case 2:
				if (time == null) {
					autogallery();
				}
				break;
			case 0:
				break;
			default:
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			int pos = position % imageUrls.size();
			currentItem = position;
			for (int i = 0; i < dotViewsList.size(); i++) {
				if (i == pos) {
					((View) dotViewsList.get(pos))
							.setBackgroundResource(R.drawable.dot_focus);
				} else {
					((View) dotViewsList.get(i))
							.setBackgroundResource(R.drawable.dot_blur);
				}
			}
		}
	}

	public void setOnItemClickListener(
			ViewPagerItemCLickListener viewPagerItemCLickListener) {
		itemClickListener = viewPagerItemCLickListener;
	}

	public void setImagePath(List<String> images) {
		imageUrls.clear();
		for (int i = 0; i < images.size(); i++) {
			imageUrls.add(images.get(i));
		}
		initUI();
	}

	public static void initImageLoader(Context context) {
		// 设定参数
		options = new DisplayImageOptions.Builder()
		// 设置图片在下载期间显示的图片
				.showStubImage(R.drawable.app_default)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageForEmptyUri(R.drawable.main_header)
				// 设置图片加载/解码过程中错误时候显示的图片
				.showImageOnFail(R.drawable.main_header)
				// 设置下载的图片是否缓存在内存中
				.cacheInMemory(true)
				// 设置下载的图片是否缓存在SD卡中
				.cacheOnDisc(true)
				// 设置图片以如何的编码方式显示
				.imageScaleType(ImageScaleType.NONE)
				// 设置为RGB565比起默认的ARGB_8888要节省大量的内存
				.bitmapConfig(Bitmap.Config.RGB_565)
				// 载入图片前稍做延时可以提高整体滑动的流畅度
				.delayBeforeLoading(100)
				// 显示图片时渐变
				.displayer(new FadeInBitmapDisplayer(500)).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();

		ImageLoader.getInstance().init(config);
	}

	public interface ViewPagerItemCLickListener {
		public abstract void onItemClick(View pager, int position);
	}
}
