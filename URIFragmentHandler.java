package com.example.breadcrumb;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;

public class URIFragmentHandler {

	public void buildURIMap() {

		// first get the container associated with tree
		HierarchicalContainer hContainer = (HierarchicalContainer) menu
				.getContainerDataSource();
		List<?> nodeList = hContainer.getItemIds();
		for (int i = 0; i < nodeList.size(); i++) {
			URIStack uriStack = new URIStack();
			String sNodeName = nodeList.get(i).toString();
			uriStack.layURIFragmentOnStack(sNodeName);
			Object parent = hContainer.getParent(nodeList.get(i));
			while (parent != null) {
				uriStack.layURIFragmentOnStack(parent.toString());
				parent = hContainer.getParent(parent.toString());
			}
			this.itemURI.put(sNodeName, uriStack);
		}

	}

	public String getItemId(String sURIFragment) {

		for (Entry<String, URIStack> entry : this.itemURI.entrySet()) {
			if (entry.getValue().getURIStr().equals(sURIFragment))
				return entry.getKey();
		}
		return null;
	}

	/*
	 * Returns the urifragment relevant to a specific item in the tree if there
	 * is a baskslash at the end of the id, it'll be removed first
	 */
	public String getURIVal(String sItemId) {

		// remove the last character backslash.
		if (sItemId.length() > 0 && sItemId.charAt(sItemId.length() - 1) == 'x') {
			sItemId = sItemId.substring(0, sItemId.length() - 1);
		}

		for (Entry<String, URIStack> entry : this.itemURI.entrySet()) {
			if (entry.getKey().equals(sItemId))
				return entry.getValue().getURIStr();
		}
		return null;
	}

	/*
	 * Retruns the breakdown structure of a uri each uri element is stuffed to a
	 * list
	 */
	public LinkedList<String> getURIStruct(String sURIFragment) {
		for (Entry<String, URIStack> entry : this.itemURI.entrySet()) {
			if (entry.getValue().getURIStr().equals(sURIFragment))
				return entry.getValue().getURIElements();
		}
		return null;
	}

	public URIFragmentHandler(Tree menu) {

		this.itemURI = new HashMap<String, URIStack>();
		this.menu = menu;

		buildURIMap();

		menu.addItemClickListener(new ItemClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void itemClick(ItemClickEvent event) {
				String sEventItm = event.getItemId().toString();
				URIStack uristack = itemURI.get(sEventItm);
				Page.getCurrent().setUriFragment(uristack.getURIStr());
			}

		});

	}

	private HashMap<String, URIStack> itemURI;
	private Tree menu;

}
