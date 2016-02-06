package org.tathva.triloaded.announcements;

import java.util.List;

import org.tathva.triloaded.gcm.Notification;
import org.tathva.triloaded.mainmenu.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AnnExpandableListAdapter extends BaseExpandableListAdapter {

	private Context _context;
    int type;
	private List<Notification> notices;
	int[] nImage = { R.drawable.bullet_black, R.drawable.bullet_black, R.drawable.bullet_black, R.drawable.bullet_black };
	
	public AnnExpandableListAdapter(Context context, List<Notification> notices, int type) {

		this._context = context;
		this.notices = notices;
        this.type = type;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return notices.get(groupPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {

			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.ann_childview, null);
		}

		TextView ccontent = (TextView) convertView
				.findViewById(R.id.tvCContent);

		Typeface hFont = Typeface.createFromAsset(_context.getAssets(), "fonts/OpenSans-Regular.ttf");
		
		ccontent.setText(notices.get(groupPosition).getAnnouncementText());
		ccontent.setTypeface(hFont);
		return convertView;

	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return notices;
	}

	@Override
	public int getGroupCount() {
		return notices.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.ann_expgroupview, null);
		}

		ImageView npic = (ImageView) convertView.findViewById(R.id.ivAnnPic);
		TextView nheading = (TextView) convertView
				.findViewById(R.id.tvAnnTitle);
		TextView ntimedate = (TextView) convertView
				.findViewById(R.id.tvAnnDateTime);
		TextView ncontent = (TextView) convertView
				.findViewById(R.id.tvAnnContent);

		//npic.setImageResource(nImage[type]);

		Typeface hFont = Typeface.createFromAsset(_context.getAssets(), "fonts/OpenSans-Regular.ttf");
		
		nheading.setTypeface(hFont);
		ncontent.setTypeface(hFont);

		nheading.setText(notices.get(groupPosition).getAnnouncementHeading());
		ntimedate.setText(notices.get(groupPosition).getAnnouncementTime());
		ncontent.setText(notices.get(groupPosition).getAnnouncementText());


		/**To make the content disappear when its expanded**/
		if (notices.get(groupPosition) != null)
			if (isExpanded)
				ncontent.setVisibility(View.INVISIBLE);
			else
				ncontent.setVisibility(View.VISIBLE);

		return convertView;

	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
