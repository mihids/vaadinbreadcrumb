package com.example.breadcrumb;

import java.util.LinkedList;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;

public class BreadCrumb extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BreadCrumb(Tree menu, UI ui) {
		this.ui = ui;
		uriHandler = new URIFragmentHandler(menu);
		breadcrumb = new HorizontalLayout();
		init();
		addComponent(breadcrumb);
		setSizeFull();
	}

	private void init() {

		ui.getPage().addUriFragmentChangedListener(
				new UriFragmentChangedListener() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					/*
					 * (non-Javadoc)
					 * 
					 * @see com.vaadin.server.Page.UriFragmentChangedListener#
					 * uriFragmentChanged
					 * (com.vaadin.server.Page.UriFragmentChangedEvent) Listens
					 * for any changes in the URI fragment and modifies the
					 * breadcrumb displayed
					 */
					@Override
					public void uriFragmentChanged(UriFragmentChangedEvent event) {

						// get the uri fragment after the #
						String sURIFragment = event.getUriFragment();

						// clear the breadcurmb
						breadcrumb.removeAllComponents();

						// add a new address bar corresponding to the changed
						// uri
						breadcrumb.addComponent(new AddressBar(sURIFragment,
								uriHandler));
					}
				});
	}

	private UI ui;
	private URIFragmentHandler uriHandler;
	private HorizontalLayout breadcrumb;

}

class ClickableURI extends HorizontalLayout {

	private static final String URI_FRAGMENT_HEADER = "#";

	public ClickableURI(String sText, String sLink) {
		Link myLink = new Link(sText, new ExternalResource(URI_FRAGMENT_HEADER
				+ sLink)); // needs to add the #
		addComponent(myLink);
	}
}

class AddressBar extends HorizontalLayout {

	private static final String URI_FRAGMENT_SEPERATOR = ">";

	public AddressBar(String sURIFragment, URIFragmentHandler uriHandler) {

		// fist get the breakdown of the uri
		LinkedList<String> uriElemts = uriHandler.getURIStruct(sURIFragment);

		// for each element separated by a / we have to build a clickable uri
		// these clickable uris comprise the breadcrumb
		for (int i = uriElemts.size() - 1; i >= 0; i--) { // iterate from top to
															// bottom of the
															// list
			String uriElmnt = uriElemts.get(i);
			ClickableURI uriElemLink = new ClickableURI(uriElmnt,
					uriHandler.getURIVal(uriElmnt)); // a clickable uri for each
														// element of the uri
			// add the urli link
			addComponent(uriElemLink);
			// hack in adding
			if (i != 0) {
				Label lbl = new Label(URI_FRAGMENT_SEPERATOR);
				addComponent(lbl);
			}
		}

	}

}
