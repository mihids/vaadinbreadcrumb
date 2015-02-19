package com.example.myproject;

import javax.servlet.annotation.WebServlet;

import com.example.breadcrumb.BreadCrumb;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("mytheme")
public class MyprojectUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = MyprojectUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
	
		VerticalLayout layout = new VerticalLayout();
		
		Tree myTree = new Tree();
		for (int i = 1; i < 6; i++) {
			String item = "item" + i;
			String childItem = "subitem" + i;
			myTree.addItem(item);
			myTree.addItem(childItem);
			myTree.setParent(childItem, item);												
			if ( i == 3) { //adding a sub-sub item
				String childChildItem = "subsubitem1";
				myTree.addItem(childChildItem);
				myTree.setParent(childChildItem, childItem);
				myTree.setChildrenAllowed(childChildItem, false);
			}
			else
				myTree.setChildrenAllowed(childItem, false);
		}
				
		BreadCrumb bCrumb = new BreadCrumb(myTree, this); 
		layout.addComponent(myTree);
		layout.addComponent(bCrumb);
		
		setContent(layout);
		
	}
}
