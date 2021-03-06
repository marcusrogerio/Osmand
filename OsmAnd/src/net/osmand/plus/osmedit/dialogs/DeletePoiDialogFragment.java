package net.osmand.plus.osmedit.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import net.osmand.access.AccessibleToast;
import net.osmand.osm.edit.Node;
import net.osmand.plus.OsmandPlugin;
import net.osmand.plus.R;
import net.osmand.plus.activities.MapActivity;
import net.osmand.plus.osmedit.EditPoiFragment;
import net.osmand.plus.osmedit.OpenstreetmapLocalUtil;
import net.osmand.plus.osmedit.OpenstreetmapUtil;
import net.osmand.plus.osmedit.OsmEditingPlugin;
import net.osmand.plus.osmedit.OsmPoint;

public class DeletePoiDialogFragment extends DialogFragment {
	private static final String KEY_AMENITY_NODE = "amenity_node";

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Activity activity = getActivity();

		OsmEditingPlugin plugin = OsmandPlugin.getPlugin(OsmEditingPlugin.class);
		final OpenstreetmapUtil mOpenstreetmapUtil;
		mOpenstreetmapUtil = new OpenstreetmapLocalUtil(plugin, activity);

		final Bundle args = getArguments();
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(R.string.poi_remove_title);
		builder.setNegativeButton(R.string.shared_string_cancel, null);
		builder.setPositiveButton(R.string.shared_string_delete, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Node node = (Node) args.getSerializable(KEY_AMENITY_NODE);
				EditPoiFragment.commitNode(OsmPoint.Action.DELETE, node,
						mOpenstreetmapUtil.getEntityInfo(), null, false,
						new Runnable() {
							@Override
							public void run() {
								AccessibleToast.makeText(activity,
										getString(R.string.poi_deleted_localy),
										Toast.LENGTH_LONG).show();
								if (activity instanceof MapActivity) {
									((MapActivity) activity).getMapView().refreshMap(true);
								}
							}
						},
						getActivity(), mOpenstreetmapUtil);
			}
		});
		return builder.create();
	}

	public static DeletePoiDialogFragment createInstance(Node amenityNode) {
		DeletePoiDialogFragment fragment = new DeletePoiDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(KEY_AMENITY_NODE, amenityNode);
		fragment.setArguments(bundle);
		return fragment;
	}
}
