# ViewPager
a mini viewpager

#使用方法(Layout中)


 <com.viewpager.MViewPager
    android:id="@+id/iv_image"
    android:layout_width="match_parent"
    android:layout_height="180dp"
    android:background="#FFFFFF"
    android:scaleType="center" />
    
    
    
#代码中
    MViewPager isImages = (MViewPager) findViewById(R.id.iv_image);
		List<String> pic_list = new ArrayList<String>();
		pic_list.add("http://image.zcool.com.cn/56/35/1303967876491.jpg");
		pic_list.add("http://image.zcool.com.cn/59/54/m_1303967870670.jpg");
		pic_list.add("http://image.zcool.com.cn/47/19/1280115949992.jpg");
		pic_list.add("http://new-img2.ol-img.com/985x695/70/25/li8q8RDAirgOg.jpg");
		pic_list.add("http://fdc.my0511.com/lphyatt/d_100602/hp_ueO45s28_iUTXzdtqdVvg.jpg");
		pic_list.add("http://image.zcool.com.cn/59/11/m_1303967844788.jpg");
		isImages.setImagePath(pic_list);

		ViewPagerItemCLickListener listener = new ViewPagerItemCLickListener() {

			@Override
			public void onItemClick(View pager, int position) {
				Toast.makeText(XQActivity.this, position + "",
						Toast.LENGTH_SHORT).show();
			}

		};
		isImages.setOnItemClickListener(listener);
